package com.webproject.betting.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webproject.betting.domain.UserVO;
import com.webproject.betting.service.impl.UserSeviceImpl;

@Controller
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Inject
	private UserSeviceImpl userService;
	
//	로그인(페이지 들어갈 때) 
	@RequestMapping(value = "/login",  method = RequestMethod.GET)
	public String loginGET() {
		logger.info("loginGET.......");
		return "login";
	}
	
//	로그인(페이지에서 폼전송했을 때)
	@RequestMapping(value = "/login",  method = RequestMethod.POST)
	public String loginPOST(@ModelAttribute UserVO userVO, HttpSession session, Model model,HttpServletRequest request,	
			HttpServletResponse response) throws Exception {
		logger.info("loginPOST.......");
		logger.info("userVO...... : " + userVO);
		
		session = request.getSession();
		
		if(session.getAttribute("login") != null) {
			session.removeAttribute("login");
		}
		
		UserVO vo =  userService.login(userVO);
		logger.info("로그인 정보 vo...... : " + vo);
		
		if(vo == null) {
			logger.info("new login fail");
			model.addAttribute("msg", "이메일이나 비밀번호가 잘못되었습니다.");
			return "/login";
			
		}else {
			logger.info("new login success");
			model.addAttribute("msg", "로그인 성공하였습니다.");
			session.setAttribute("login", vo);   	   //로그인 유무 구분갑으로 사용
			return "redirect:/";
		}
	}
	
	// 로그 아웃 기능
		@RequestMapping(value = "/logout", method = RequestMethod.GET)
		public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session,	RedirectAttributes rttr) throws Exception {
			
			logger.info("로그아웃 ");
			Object login_YN = session.getAttribute("login");		//로그인 유무 값
			

			if (login_YN != null) {
				session.removeAttribute("login");
				session.invalidate();
					
				rttr.addFlashAttribute("errorMessageTitle", "로그아웃");
				rttr.addFlashAttribute("errorMessage", "로그아웃하셨습니다");
			} 
			
			return "redirect:/login";
		}
	

	
}
