package com.example.POPCornPickView.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

	@RequestMapping("/mainInfo")
	public String mainInfo() {
		
		return "member/mainInfo";
	}
	
	@RequestMapping("/sideMenu")
	public String sideMenu() {
		
		return "member/sideMenu";
	}
	
	@RequestMapping("/couponList")
	public String couponList() {
		
		return "member/couponList";
	}
	
	@RequestMapping("/giftCardList")
	public String giftCardList() {
		
		return "member/giftCardList";
	}
}
