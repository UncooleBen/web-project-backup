package com.j2ee.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.j2ee.dao.LoginDao;
import com.j2ee.model.Admin;
import com.j2ee.model.User;
import com.j2ee.util.DbUtil;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	LoginDao loginDao = new LoginDao();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String remember = request.getParameter("remember");
		String userType = request.getParameter("userType");
		
		Connection con = null;
		try {
			con=dbUtil.getCon();
			Admin currentAdmin = null;
			User currentUser = null;
			if("admin".equals(userType)) {
				Admin admin = new Admin(userName, password);
				currentAdmin = loginDao.Login(con, admin);
				if(currentAdmin == null) {
					request.setAttribute("admin", admin);
					request.setAttribute("error", "用户名或密码错误！");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				} else {
					if("remember-me".equals(remember)) {
						rememberMe(userName, password, userType,response);
					} else {
						deleteCookie(userName, request, response);
					}
					session.setAttribute("currentUserType", "admin");
					session.setAttribute("currentUser", currentAdmin);
					request.setAttribute("mainPage", "admin/blank.jsp");
					request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				}
			} else if("user".equals(userType)) {
				User user = new User(userName, password);
				currentUser = loginDao.Login(con, user);
				if(currentUser == null) {
					request.setAttribute("user", user);
					request.setAttribute("error", "用户名或密码错误！");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				} else {
					if("remember-me".equals(remember)) {
						rememberMe(userName, password, userType,response);
					} else {
						deleteCookie(userName, request, response);
					}
					session.setAttribute("currentUserType", "user");
					session.setAttribute("currentUser", currentUser);
					request.setAttribute("mainPage", "user/blank.jsp");
					request.getRequestDispatcher("mainUser.jsp").forward(request, response);
				}
			} 
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void rememberMe(String userName, String password, String userType, HttpServletResponse response) {
		Cookie user = new Cookie("user", userName+"-"+password+"-"+userType+"-"+"yes");
		user.setMaxAge(1*60*60*24*7);
		response.addCookie(user);
	}
	
	private void deleteCookie(String userName, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies=request.getCookies();
		for(int i=0;cookies!=null && i<cookies.length;i++){
			if(cookies[i].getName().equals("user")){
				if(userName.equals(userName=cookies[i].getValue().split("-")[0])) {
					Cookie cookie = new Cookie(cookies[i].getName(), null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					break;
				}
			}
		}
	}
}
