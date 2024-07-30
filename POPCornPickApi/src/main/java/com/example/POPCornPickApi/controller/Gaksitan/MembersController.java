package com.example.POPCornPickApi.controller.Gaksitan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.POPCornPickApi.dto.CancelListDto;
import com.example.POPCornPickApi.dto.ReservationListDto;
import com.example.POPCornPickApi.entity.CancelList;
import com.example.POPCornPickApi.entity.Cinema;
import com.example.POPCornPickApi.entity.Member;
import com.example.POPCornPickApi.entity.Movie;
import com.example.POPCornPickApi.entity.MovieDetail;
import com.example.POPCornPickApi.entity.ReservatedSeat;
import com.example.POPCornPickApi.entity.Room;
import com.example.POPCornPickApi.entity.Schedule;
import com.example.POPCornPickApi.entity.Ticketing;
import com.example.POPCornPickApi.entity.UnknownMember;
import com.example.POPCornPickApi.jwt.JWTUtil;
import com.example.POPCornPickApi.repository.CancelListRepository;
import com.example.POPCornPickApi.repository.CinemaRepository;
import com.example.POPCornPickApi.repository.MemberRepository;
import com.example.POPCornPickApi.repository.MovieDetailRepository;
import com.example.POPCornPickApi.repository.MovieRepository;
import com.example.POPCornPickApi.repository.MovieShowDetailRepository;
import com.example.POPCornPickApi.repository.PromotionInfoRepository;
import com.example.POPCornPickApi.repository.ReservatedSeatRepository;
import com.example.POPCornPickApi.repository.RoomRepository;
import com.example.POPCornPickApi.repository.ScheduleRepository;
import com.example.POPCornPickApi.repository.TicketingRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/mem")
public class MembersController {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private TicketingRepository ticketingRepository;
	
	@Autowired
	private CancelListRepository cancelListRepository;
	
	@Autowired
	private ReservatedSeatRepository reservatedSeatRepository;
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private MovieShowDetailRepository movieShowDetailRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private CinemaRepository cinemaRepository;
	
	@Autowired
	private PromotionInfoRepository promotionInfoRepository;
	
	@Autowired
	private MovieDetailRepository movieDetailRepository;
	
	@GetMapping("/memberInfo")
	public Member memberInfo(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		String username = jwtUtil.getUsername(token);
		Member member = memberRepository.findByUsername(username);
		
		return member;
	}
	
	// 회원정보 수정
	@PutMapping("/editMyInfo")
	public ResponseEntity<?> editMyInfo(@RequestBody Member member) {
		System.out.println(member);
		if(member != null) {
			Member mem = memberRepository.findByUsername(member.getUsername());
			mem.setTel(member.getTel());
			mem.setEmail(member.getEmail());
			String pw = bCryptPasswordEncoder.encode(member.getPassword());
			mem.setPassword(pw);
			memberRepository.save(mem);
			String str = "회원정보 수정 완료";
			return ResponseEntity.ok(str);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
		}
		
	}
	
	// 회원탈퇴
	@DeleteMapping("/delete")
	public void delete(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		String username = jwtUtil.getUsername(token);
		Member member = memberRepository.findByUsername(username);
		memberRepository.delete(member);
		
		return ;
	}
	
	// 예매내역 불러오기
	@GetMapping("/reservation")
	public List<ReservationListDto> reservation(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		List<ReservationListDto> reservationList = new ArrayList<>();
		
		if(jwtUtil.getPassword2(token) == null) { // 회원 로그인 유저
			String username = jwtUtil.getUsername(token);
			Member member = new Member();
			member.setUsername(username);
			List<Ticketing> ticketingList = ticketingRepository.findByMember(member);
			
			for(int i = 0; i < ticketingList.size() ; i++) {
				// 조회
				Long reservatedSeatNo = ticketingList.get(i).getReservatedSeat().getReservatedSeatNo();
				ReservatedSeat reservatedSeat = reservatedSeatRepository.findByReservatedSeatNo(reservatedSeatNo);
				Long scheduleNo = reservatedSeatRepository.findByReservatedSeatNo(reservatedSeatNo).getSchedule().getScheduleNo();
				Long detailNo = scheduleRepository.findByScheduleNo(scheduleNo).getMovieShowDetail().getDetailNo();
				Long movieDC = movieShowDetailRepository.findByDetailNo(detailNo).getMovie().getMovieDC();
				Long roomNo = movieShowDetailRepository.findByDetailNo(detailNo).getRoom().getRoomNo();
				
				List<Room> roomList = roomRepository.findByRoomNo(roomNo);
				Long cinemaNo = roomList.get(0).getCinema().getCinemaNo();
				
				// 사용
				Cinema cinema = cinemaRepository.getCinemaFindByCinemaNo(cinemaNo);
				Movie movie = movieRepository.findByMovieDC(movieDC);
				Schedule schedule = scheduleRepository.findByScheduleNo(scheduleNo);
				
				MovieDetail movieDetail = movieDetailRepository.findByMovieNm(movie.getTitle());
				
				ReservationListDto reservationListDto = new ReservationListDto(
						ticketingList.get(i).getTicketingNo(), 
						ticketingList.get(i).getRegdate(), 
						movieDetail.getImgUrl(), 
						movie.getTitle(), 
						cinema.getCinemaName(), 
						schedule.getStart(), 
						reservatedSeat.getSeatRow(),
						reservatedSeat.getSeatColumn(),
						ticketingList.get(i).getPayTotalAmount()
						);
				System.out.println(reservationListDto);
				
				reservationList.add(reservationListDto);
			}
			
			return reservationList;
		}else { // 비회원 로그인 유저
			String tel = jwtUtil.getTel(token);
			UnknownMember unknownMember = new UnknownMember();
			unknownMember.setTelephone(tel);
			List<Ticketing> ticketingList = ticketingRepository.findByUnknownMember(unknownMember);
			
			for(int i = 0; i < ticketingList.size(); i++) {
				// 조회
				Long reservatedSeatNo = ticketingList.get(i).getReservatedSeat().getReservatedSeatNo();
				ReservatedSeat reservatedSeat = reservatedSeatRepository.findByReservatedSeatNo(reservatedSeatNo);
				
				Long scheduleNo = reservatedSeatRepository.findByReservatedSeatNo(reservatedSeatNo).getSchedule().getScheduleNo();
				Long detailNo = scheduleRepository.findByScheduleNo(scheduleNo).getMovieShowDetail().getDetailNo();
				
				Long movieDC = movieShowDetailRepository.findByDetailNo(detailNo).getMovie().getMovieDC();
				Long roomNo = movieShowDetailRepository.findByDetailNo(detailNo).getRoom().getRoomNo();
				
				List<Room> roomList = roomRepository.findByRoomNo(roomNo);
				Long cinemaNo = roomList.get(0).getCinema().getCinemaNo();
				
				// 사용
				Cinema cinema = cinemaRepository.getCinemaFindByCinemaNo(cinemaNo);
				Movie movie = movieRepository.findByMovieDC(movieDC);
				Schedule schedule = scheduleRepository.findByScheduleNo(scheduleNo);
				
				MovieDetail movieDetail = movieDetailRepository.findByMovieNm(movie.getTitle());
				
				ReservationListDto reservationListDto = new ReservationListDto(
						ticketingList.get(i).getTicketingNo(), 
						ticketingList.get(i).getRegdate(), 
						movieDetail.getImgUrl(), 
						movie.getTitle(), 
						cinema.getCinemaName(), 
						schedule.getStart(), 
						reservatedSeat.getSeatRow(),
						reservatedSeat.getSeatColumn(),
						ticketingList.get(i).getPayTotalAmount()
						);
				
				reservationList.add(reservationListDto);
			}
			
			return reservationList;
		}
		
		
	}
	
	@GetMapping("/cancelReservation")
	public List<CancelListDto> cancelReservation(HttpServletRequest request){
		String token = request.getHeader("Authorization");
		List<CancelListDto> cancelList = new ArrayList<>();
		
		if(jwtUtil.getPassword2(token) == null) { // 회원
			String username = jwtUtil.getUsername(token);
			Member member = new Member();
			member.setUsername(username);
			List<CancelList> listCancel = cancelListRepository.findByMember(member);
			List<Ticketing> cancelTicketingList = new ArrayList<>();
			
			for(int i = 0; i < listCancel.size(); i++) {
				if(cancelListRepository.existsById(listCancel.get(i).getCancerListNo())) {
					Optional<CancelList> list_cancel = cancelListRepository.findById(listCancel.get(i).getCancerListNo());
					CancelList cancel = list_cancel.get();
					cancel.getTicketing().getTicketingNo();
					Optional<Ticketing> ticket = ticketingRepository.findById(cancel.getTicketing().getTicketingNo());
					Ticketing ticketing = ticket.get();
					cancelTicketingList.add(ticketing);
					
					Long reservatedSeatNo = cancelTicketingList.get(i).getReservatedSeat().getReservatedSeatNo();
					Long scheduleNo = reservatedSeatRepository.findByReservatedSeatNo(reservatedSeatNo).getSchedule().getScheduleNo();
					
					Long detailNo = scheduleRepository.findByScheduleNo(scheduleNo).getMovieShowDetail().getDetailNo();
					Long movieDC = movieShowDetailRepository.findByDetailNo(detailNo).getMovie().getMovieDC();
					Long roomNo = movieShowDetailRepository.findByDetailNo(detailNo).getRoom().getRoomNo();
					Long cinemaNo = roomRepository.findByRoomNo(roomNo).get(0).getCinema().getCinemaNo();
					
					Cinema cinema = cinemaRepository.getCinemaFindByCinemaNo(cinemaNo);
					Schedule schedule = scheduleRepository.findByScheduleNo(scheduleNo);
					Movie movie = movieRepository.findByMovieDC(movieDC);
					
					
					CancelListDto cancelListDto = new CancelListDto(movie.getTitle(), cinema.getCinemaName(), schedule.getStart(), cancelTicketingList.get(i).getRegdate(), cancelTicketingList.get(i).getPayTotalAmount());
					
					cancelList.add(cancelListDto);
				}
			}
			
			
		}else { // 비회원
			String tel = jwtUtil.getTel(token);
			UnknownMember unknownMember = new UnknownMember();
			unknownMember.setTelephone(tel);
			List<CancelList> listCancel = cancelListRepository.findByUnknownMember(unknownMember);
			List<Ticketing> cancelTicketingList = new ArrayList<>();
			
			for(int i = 0; i < listCancel.size(); i++) {
				if(cancelListRepository.existsById(listCancel.get(i).getCancerListNo())) {
					Optional<CancelList> list_cancel = cancelListRepository.findById(listCancel.get(i).getCancerListNo());
					CancelList cancel = list_cancel.get();
					cancel.getTicketing().getTicketingNo();
					Optional<Ticketing> ticket = ticketingRepository.findById(cancel.getTicketing().getTicketingNo());
					Ticketing ticketing = ticket.get();
					cancelTicketingList.add(ticketing);
					
					Long reservatedSeatNo = cancelTicketingList.get(i).getReservatedSeat().getReservatedSeatNo();
					Long scheduleNo = reservatedSeatRepository.findByReservatedSeatNo(reservatedSeatNo).getSchedule().getScheduleNo();
					
					Long detailNo = scheduleRepository.findByScheduleNo(scheduleNo).getMovieShowDetail().getDetailNo();
					Long movieDC = movieShowDetailRepository.findByDetailNo(detailNo).getMovie().getMovieDC();
					Long roomNo = movieShowDetailRepository.findByDetailNo(detailNo).getRoom().getRoomNo();
					Long cinemaNo = roomRepository.findByRoomNo(roomNo).get(0).getCinema().getCinemaNo();
					
					Cinema cinema = cinemaRepository.getCinemaFindByCinemaNo(cinemaNo);
					Schedule schedule = scheduleRepository.findByScheduleNo(scheduleNo);
					Movie movie = movieRepository.findByMovieDC(movieDC);
					
					
					CancelListDto cancelListDto = new CancelListDto(movie.getTitle(), cinema.getCinemaName(), schedule.getStart(), cancelTicketingList.get(i).getRegdate(), cancelTicketingList.get(i).getPayTotalAmount());
					
					cancelList.add(cancelListDto);
				}
			}
		}
		System.out.println("cancelList : " + cancelList);
		
		return cancelList;
	}
	
	@DeleteMapping("/cancelReservation")
	public String deleteCancelReservation(@RequestParam("ticketingNo") Long ticketingNo) {
		String str = "";
		if(ticketingNo != null) {
			Optional<Ticketing> optionalTicketing = ticketingRepository.findById(ticketingNo);
			Ticketing ticketing = optionalTicketing.get();
			CancelList cancelList = new CancelList();
			cancelList.setTicketing(ticketing);
			cancelListRepository.save(cancelList);
			ReservatedSeat reservatedSeat = reservatedSeatRepository.findByReservatedSeatNo(ticketing.getReservatedSeat().getReservatedSeatNo());
			reservatedSeatRepository.delete(reservatedSeat);
			ticketingRepository.delete(ticketing);
			str = "예매취소가 완료되었습니다.";
			
		}else {
			str = "예매취소에 실패했습니다. (ticketingNo is Null)";
		}
		
		return str; 
	}
	
	
	
}