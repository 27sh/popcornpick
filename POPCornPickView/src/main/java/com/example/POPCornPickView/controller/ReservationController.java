package com.example.POPCornPickView.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

	@RequestMapping("/main")
	public String reservation() {
		return "member/reservePage_main";
	}
	
	@RequestMapping("/seat/{scheduleNo}/{movieTitle}/{cinemaName}/{date}")
	public String seatSelection(@PathVariable("scheduleNo") Long scheduleNo, @PathVariable("movieTitle") String movieTitle,
			@PathVariable("cinemaName") String cinemaName, @PathVariable("date") String date,  RedirectAttributes redirectAttributes) {
		
		redirectAttributes.addFlashAttribute("scheduleNo", scheduleNo);
		redirectAttributes.addFlashAttribute("movieTitle", movieTitle);
		redirectAttributes.addFlashAttribute("cinemaName", cinemaName);
		redirectAttributes.addFlashAttribute("date", date);
		return "redirect:/reservation/seat";
	}
	
	@RequestMapping("/seat")
	public String showSeatPage(Model model) {

		Long scheduleNo = (Long) model.getAttribute("scheduleNo");
		String movieTitle = (String) model.getAttribute("movieTitle");
		String cinemaName = (String) model.getAttribute("cinemaName");
		String date = (String) model.getAttribute("date");
		
		model.addAttribute("scheduleNo", scheduleNo);
		model.addAttribute("movieTitle", movieTitle);
		model.addAttribute("cinemaName", cinemaName);
		model.addAttribute("date", date);
		
		return "member/reservePage_seatSelection";
	}
	
	@GetMapping("/pay/{scheduleNo}/{movieTitle}/{cinemaName}/{date}/{time}/{typeCount}/{seatSelected}")
	public String givePayParameters(@PathVariable("scheduleNo") Long scheduleNo, @PathVariable("movieTitle") String movieTitle,
			@PathVariable("cinemaName") String cinemaName, @PathVariable("date") String date, @PathVariable("time") String time, 
			@PathVariable("typeCount") String typeCount, @PathVariable("seatSelected") String seatSelected, RedirectAttributes redirectAttributes) {
		
		redirectAttributes.addFlashAttribute("scheduleNo", scheduleNo);
		redirectAttributes.addFlashAttribute("movieTitle", movieTitle);
		redirectAttributes.addFlashAttribute("cinemaName", cinemaName);
		redirectAttributes.addFlashAttribute("date", date);
		redirectAttributes.addFlashAttribute("time", time);
		redirectAttributes.addFlashAttribute("typeCount", typeCount);
		redirectAttributes.addFlashAttribute("seatSelected", seatSelected);
		
		return "redirect:/reservation/pay";
	}
	
	@RequestMapping("/pay")
	public String goToPay(Model model) {
		
		Long scheduleNo = (Long) model.getAttribute("scheduleNo");
		String movieTitle = (String) model.getAttribute("movieTitle");
		String cinemaName = (String) model.getAttribute("cinemaName");
		String date = (String) model.getAttribute("date");
		String time = (String) model.getAttribute("time");
		String typeCount = (String) model.getAttribute("typeCount");
		String seatSelected = (String) model.getAttribute("seatSelected");
		
		model.addAttribute("scheduleNo", scheduleNo);
		model.addAttribute("movieTitle", movieTitle);
		model.addAttribute("cinemaName", cinemaName);
		model.addAttribute("date", date);
		model.addAttribute("time", time);
		model.addAttribute("typeCount", typeCount);
		model.addAttribute("seatSelected", seatSelected);
		
		return "member/reservePage_paySeats";
	}
}
