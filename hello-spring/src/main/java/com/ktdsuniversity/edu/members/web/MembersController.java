package com.ktdsuniversity.edu.members.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.common.utils.ServletUtils;
import com.ktdsuniversity.edu.members.service.MembersService;
import com.ktdsuniversity.edu.members.vo.MembersVO;
import com.ktdsuniversity.edu.members.vo.request.MemberSearchVO;
import com.ktdsuniversity.edu.members.vo.request.RegistVO;
import com.ktdsuniversity.edu.members.vo.request.UpdateVO;
import com.ktdsuniversity.edu.members.vo.response.DuplicateResultVO;
import com.ktdsuniversity.edu.members.vo.response.SearchResultVO;

import jakarta.validation.Valid;

/**
 * EndPoint 생성/관리.
 * + Validation Check
 */

@Controller
public class MembersController {

	private static final Logger logger = LoggerFactory.getLogger(MembersController.class);
	
	@Autowired
	private MembersService membersService;
	
	@ResponseBody
	@GetMapping("/regist/check/duplicate/{email}")
	public DuplicateResultVO doCheckDuplicateEmailAction(@PathVariable String email) {
		
		// email이 이미 사용중인지 확인한다.
		MembersVO membersVO = this.membersService.findMemberByEmail(email);
		
		// 확인된 결과를 브라우저에게 JSON으로 전송한다.
		// 이미 사용중 ==> {email: "test@gmail", duplicate: true}
		// 사용중이지 않음 ==> {email: "test@gmail", duplicate: false}
		DuplicateResultVO result = new DuplicateResultVO();
		result.setEmail(email);
		result.setDuplicate( membersVO != null );
		return result;
	}
	
	@GetMapping("/regist")
	public String viewRegistPage(Authentication authentication) {
		// 인증 토큰이 존재하면!
		if (authentication != null) {
			return "redirect:/";
		}
		
		return "members/regist";
	}
	
	@PreAuthorize("isAnonymous()")
	@PostMapping("/regist")
	public String doRegistAction(
			@Valid @ModelAttribute RegistVO registVO,
			BindingResult bindingResult,
			Model model) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("inputData", registVO);
			return "members/regist";
		}
		boolean createResult = this.membersService.createNewMember(registVO);
		logger.debug("회원 가입 결과? {}", createResult);
		return "redirect:/login";
	}
	
	/*
	 * /member/view/사용자아이디 ==> 회원 정보 조회 하기.
	 * /member/update/사용자아이디 ==> 회원 정보 수정 페이지 보기.
	 * /member/update/사용자아이디 ==> 회원 정보 수정 하기.
	 * /member/delete?id=사용자아이디 ==> 회원 정보 삭제 하기.
	 */
	// 본인의 정보만 조회 가능하도록 개선.
	// 다른 사람의 정보를 조회하려 할 경우 예외 발생 ==> 잘못된 접근입니다.
	@PreAuthorize("isAuthenticated() and #email == authentication.principal.email") // 메소드의 파라미터로 전달된 값(email)과 authentication에 할당된 email값을 비교한다.
	@GetMapping("/member/view/{email}")
	public String viewMemberPage(@PathVariable String email, 
			Model model) {
		MembersVO searchReuslt = this.membersService.findMemberByEmail(email);
		model.addAttribute("member", searchReuslt);
		return "members/view";
	}
	
	// 본인의 정보만 조회 가능하도록 개선.
	// 다른 사람의 정보를 조회하려 할 경우 예외 발생 ==> 잘못된 접근입니다.
	@PreAuthorize("isAuthenticated() and #email == authentication.principal.email") // 메소드의 파라미터로 전달된 값(email)과 authentication에 할당된 email값을 비교한다.
	@GetMapping("/member/update/{email}")
	public String viewUpdatePage(@PathVariable String email,
			Model model) {
		MembersVO searchReuslt = this.membersService.findMemberByEmail(email);
		model.addAttribute("member", searchReuslt);
		return "members/update";
	}
	
	// 본인의 정보만 조회 가능하도록 개선.
	// 다른 사람의 정보를 조회하려 할 경우 예외 발생 ==> 잘못된 접근입니다.
	@PreAuthorize("isAuthenticated() and #email == authentication.principal.email")
	@PostMapping("/member/update/{email}")
	public String doUpdateAction(@PathVariable String email,
			UpdateVO updateVO) {
		updateVO.setEmail(email);
		boolean updateResult = this.membersService.updateMemberByEmail(updateVO);
		logger.debug("수정 결과? {}", updateResult);
		return "redirect:/member/view/" + email;
	}
	
	// 본인의 정보만 조회 가능하도록 개선.
	// 다른 사람의 정보를 조회하려 할 경우 예외 발생 ==> 잘못된 접근입니다.
	@PreAuthorize("isAuthenticated() and #id == authentication.principal.email")
	@GetMapping("/member/delete")
	public String doDeleteAction(@RequestParam String id) {
		boolean updateResult = this.membersService.deleteMemberByEmail(id);
		logger.debug("삭제 결과? {}", updateResult);
		return "redirect:/member";
	}
	
	// /member ==> 회원들의 목록이 조회되도록 코드를 작성.
	//     ==> 회원 목록 조회.
	//     ==> members/list.jsp : 회원 목록 반복.
	//                          : 회원의 수 출력
	//                          : 회원의 수가 없을 때, "등록된 회원이 없습니다" 출력
	//                          : 목록 아래에는 "새로운 회원 등록" 링크 추가.
	// 관리자 계정에서만 볼 수 있도록 개선.
	@PreAuthorize("hasRole('RL-20260414-000002')")
	@GetMapping("/member")
	public String viewMembersPage(Model model, MemberSearchVO membersSearchVO) {
		SearchResultVO searchResult = this.membersService.findMembersList(membersSearchVO);
		model.addAttribute("searchList", searchResult.getResult());
		model.addAttribute("searchCount", searchResult.getCount());
		model.addAttribute("pagination", membersSearchVO);
		return "members/newlist";
	}
	
	@GetMapping("/login")
	public String viewLoginPage(Authentication authentication) {
		
		// 인증 토큰이 존재하면!
		if (authentication != null) {
			return "redirect:/";
		}
		
		return "members/login"; 
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/logout")
	public String doLogoutAction(Authentication authentication) {
		
		// SecurityContext에서 인증받은 Authentication을 제거하는 객체.
		LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(
				ServletUtils.getRequest(), 
				ServletUtils.getResponse(), 
				authentication);
		
		return "redirect:/login";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete-me")
	public String doDeleteAction(Authentication authentication) {
		// 1. 로그인 세션에서 회원의 이메일을 가져온다.
		MembersVO loginUser = (MembersVO) authentication.getPrincipal();
		String email = loginUser.getEmail();
		
		// 2. MEMBERS 테이블에서 회원의 정보를 이메일을 이용해 삭제한다.
		boolean deleteSuccess = this.membersService.deleteMemberByEmail(email);
		logger.debug("탈퇴 성공? {}", deleteSuccess);
		
		// 3. 현재 로그인된 사용자를 로그아웃 시킨다.
		LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(
				ServletUtils.getRequest(), 
				ServletUtils.getResponse(), 
				authentication);
		
		// 4. "members/deletesuccess" 페이지를 보여준다.
		//    "탈퇴가 완료됐습니다. 다음에 다시 만나요!"
		return "members/deletesuccess";
	}
}






