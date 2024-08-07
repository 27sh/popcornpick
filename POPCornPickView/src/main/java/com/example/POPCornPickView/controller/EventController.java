package com.example.POPCornPickView.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/event")
public class EventController {
	
	@RequestMapping("/sideBar")
	public String sideBar() {
		return "layout/serviceSideBar";
	}

	@RequestMapping("/regist")
	public String eventRegistForm() {
		return "event/admin/eventForm";
	}
	
	@RequestMapping("/allList")
	public String eventAllList() {
		return "event/admin/eventList";
	}
	
	@RequestMapping("/detail")
	public String eventDetail() {
		return "event/admin/eventDetail";
	}
	// 공통, 회원 
	@RequestMapping("/commonList")
	public String eventCommonList() {
		return "event/member/eventList";
	}
	
	@RequestMapping("/commonDetail")
	public String eventCommonDetail() {
		return "event/member/eventDetail";
	}
	
	@RequestMapping("/participationList")
	public String eventMemberParticipationList() {
		return "event/admin/eventMemberList";
	}
	
	@RequestMapping("/endList")
	public String eventEndList() {
		return "event/admin/eventEndList";
	}
	

}
