package com.example.POPCornPickApi.controller.seowon0825;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.POPCornPickApi.dto.CinemaDto;
import com.example.POPCornPickApi.dto.CinemaRoomDto;
import com.example.POPCornPickApi.entity.Cinema;
import com.example.POPCornPickApi.entity.Room;
import com.example.POPCornPickApi.entity.RoomType;
import com.example.POPCornPickApi.service.CinemaService;
import com.example.POPCornPickApi.service.RoomService;
import com.example.POPCornPickApi.service.SeatService;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/cinema")
public class CinemaController {

	@Value("${spring.servlet.multipart.location}")
	private String uploadDir;
	
	@Autowired
	private CinemaService cinemaService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private SeatService seatService;
	
	@PostMapping
	public @ResponseBody String registCinema(CinemaDto cinemaDto) {
		
		Cinema cinema = new Cinema();
		String originName = cinemaDto.getFileNema();
		
		cinema.setCinemaIntro(cinemaDto.getCinemaIntro());
		cinema.setCinemaLocation(cinemaDto.getCinemaLocation());
		cinema.setCinemaAddr(cinemaDto.getCinemaAddr());
		cinema.setCinemaTel(cinemaDto.getCinemaTel());
		cinema.setCinemaName(cinemaDto.getCinemaName());
		
		cinema.setCinemaImgOriginName(originName);
		String newName = UUID.randomUUID().toString() + "_" + originName;
		cinema.setCinemaImgNewName(newName);
		File file = new File(uploadDir, newName);
		
		try {
			cinemaDto.getImgfile().transferTo(file);
			System.out.println("파일 업로드 성공");
			boolean result = cinemaService.registCinema(cinema);
			if(result) {
				System.out.println("true");
				return "등록이 완료되었습니다.";
			} else {
				System.out.println("false");
				return "다시 등록해 주세요.";
			}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return	"파일 저장 중 에러 발생 : " + e.getMessage();
		}		
	}
	
	@GetMapping("/room")
	public ResponseEntity<List<RoomType>> roomList() {
		List<RoomType> list = roomService.getRoomList();
		System.out.println(list);
		if(!list.isEmpty()) {
			return ResponseEntity.ok(list);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<Cinema>> cinemaList(){
		List<Cinema> list = cinemaService.getCinemaList();
		if(!list.isEmpty()) {
			return ResponseEntity.ok(list);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/choiceList")
	public ResponseEntity<List<Cinema>> choiceCinemaList(@RequestParam("cinemaLocation") String cinemaLocation){
		try {
			List<Cinema> list= cinemaService.getCinemaLocationList(cinemaLocation);
			return ResponseEntity.ok(list);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
//	@GetMapping("/searchResult")
//	public ResponseEntity<List<Cinema>> getCinemaList(@RequestParam("searchContent") String searchContent){
//		List<Cinema> list = cinemaService.getSearchCinemaList(searchContent);
//		System.out.println(list);
//		return ResponseEntity.ok(list);
//	}
	
	@GetMapping("/detail")
	public ResponseEntity<Cinema> cinemaDetail(@RequestParam("cinemaNo") Long cinemaNo){
		System.out.println("cinemaNo : " + cinemaNo);
		Cinema detail = cinemaService.getCinemaDetail(cinemaNo);
		
		if(detail != null) {
			return ResponseEntity.ok(detail);
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	
	@PutMapping
	public @ResponseBody String cinemaModify(CinemaDto cinemaDto){
		Cinema cinema = new Cinema();
		String originName = cinemaDto.getFileNema();
		
		cinema.setCinemaNo(cinemaDto.getCinemaNo());
		cinema.setCinemaIntro(cinemaDto.getCinemaIntro());
		cinema.setCinemaLocation(cinemaDto.getCinemaLocation());
		cinema.setCinemaAddr(cinemaDto.getCinemaAddr());
		cinema.setCinemaTel(cinemaDto.getCinemaTel());
		cinema.setCinemaName(cinemaDto.getCinemaName());
		
		//파일업로드 안할 시 이미지 변경x
		if(originName != null && !originName.isEmpty()) {
			cinema.setCinemaImgOriginName(originName);
			String newName = UUID.randomUUID().toString() + "_" + originName;
			cinema.setCinemaImgNewName(newName);
			File file = new File(uploadDir, newName);
			try {
				cinemaDto.getImgfile().transferTo(file);
				cinemaService.cinemaImgModify(cinema);
				System.out.println("파일 업로드 성공");
				return "수정이 완료되었습니다.";
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				return	"파일 저장 중 에러 발생 : " + e.getMessage();
			}
		} else {
			boolean result = cinemaService.cinemaModify(cinema);
			if(result) {
				System.out.println("true");
				return "수정이 완료되었습니다.";
			} else {
				System.out.println("false");
				return "다시 수정해 주세요.";
			}
		}
	}
	
	@GetMapping("/roomDetail")
	public List<CinemaRoomDto> cinemaRoomDetail(@RequestParam("cinemaName")String cinemaName){
		//cinemaNo값 추출
		Long cinemaNo = cinemaService.getCinemaNo(cinemaName);
		System.out.println(cinemaNo);
		
		//cinemaNo값을 이용해서 room_no추출
		List<Long> roomNoList = roomService.getRoomNo(cinemaNo);
		System.out.println("roomNoList : " + roomNoList);
		
		//반복문을 이용해서 roomNo에 해당하는 seat데이터 추출
		List<CinemaRoomDto> list = new ArrayList<>();
		
		for(Long roomNo : roomNoList) {
			System.out.println("roomNo : " + roomNo);
			try {
				//상영관별 총 좌석 수
				int total = seatService.getCinemaRoomSeat(roomNo);
				//예매 가능한 좌석 수
				int bookedCnt = seatService.getBookedSeat(roomNo);
				List<Room> roomInfo = roomService.getRoomInfo(roomNo);
				System.out.println(total);
				System.out.println(bookedCnt);
				System.out.println(roomInfo);
				String smallType = roomService.getBigType(roomInfo.get(0).getRoomType().getRoomTypeNo());
				System.out.println("smallType : " + smallType);
				
				String cinemaName1 = cinemaService.getCinemaName(cinemaNo);
				if(smallType.equals("*")) {
					smallType = "일반관";
				} 
				CinemaRoomDto cinemaRoomDto = new CinemaRoomDto(
						total, 
						bookedCnt, 
						roomNo, 
						roomInfo.get(0).getCinema().getCinemaNo(), 
						roomInfo.get(0).getRoomType().getRoomTypeNo(),
						smallType,
						cinemaName1
						);
				list.add(cinemaRoomDto);
				System.out.println("cinemaRoomDto : " + cinemaRoomDto);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
//	@GetMapping("/roomList")
//	public ResponseEntity<List<Cinema>> cinemaroomList(@RequestParam("cinemaLocation") String cinemaLocation){
//		System.out.println("controller location : " + cinemaLocation);
//		List<Cinema> cineRoomList = cinemaService.getCinemaRoomList(cinemaLocation);
//		System.out.println(cineRoomList);
//		if(!cineRoomList.isEmpty()) {
//			return ResponseEntity.ok(cineRoomList);
//		} else {
//			return ResponseEntity.badRequest().body(null);
//		}
//	}
}
