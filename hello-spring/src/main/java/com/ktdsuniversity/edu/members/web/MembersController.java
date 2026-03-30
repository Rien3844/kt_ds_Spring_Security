package com.ktdsuniversity.edu.members.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ktdsuniversity.edu.members.service.MembersService;
import com.ktdsuniversity.edu.members.vo.request.RegistVO;

@Controller
public class MembersController {

	@Autowired
	private MembersService membersService;
	
	@GetMapping("/regist")
	public String viewRegistPage() {
		return "members/regist";
	}
	
	@PostMapping("/regist")
	public String doRegistAction(RegistVO registVO) {
		boolean createResult = this.membersService.createNewMember(registVO);
		System.out.println("회원 가입 결과? " + createResult);
		return "redirect:/login";
	}
	
	/*
	 * /member/view/사용자아이디 ==> 회원 정보 조회 하기.
	 * /member/update/사용자아이디 ==> 회원 정보 수정 페이지 보기.
	 * /member/update/사용자아이디 ==> 회원 정보 수정 하기.
	 * /member/delete?id=사용자아이디 ==> 회원 정보 삭제 하기.
	 */
	
}
