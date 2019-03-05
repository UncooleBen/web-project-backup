package com.j2ee.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.j2ee.dao.LoginDao;
import com.j2ee.model.Admin;
import com.j2ee.model.User;
import com.j2ee.util.DbUtil;

public class PasswordServlet extends HttpServlet{

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
		String action = request.getParameter("action");
		
		if("preChange".equals(action)) {
			passwordPreChange(request, response);
			return;
		} else if("change".equals(action)) {
			passwordChange(request, response);
			return;
		}
	}

	private void passwordChange(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			
			if("admin".equals((String)currentUserType)) {
				Admin admin = (Admin)(session.getAttribute("currentUser"));
				if(oldPassword.equals(admin.getPassword())) {
					loginDao.adminUpdate(con, admin.getAdminId(), newPassword);
					admin.setPassword(newPassword);
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "修改成功 ");
					request.setAttribute("mainPage", "admin/passwordChange.jsp");
					request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				} else {
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "原密码错误");
					request.setAttribute("mainPage", "admin/passwordChange.jsp");
					request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				}
			} else if("user".equals((String)currentUserType)) {
				User user = (User)(session.getAttribute("currentUser"));
				if(oldPassword.equals(user.getPassword())) {
					loginDao.adminUpdate(con, user.getUserId(), newPassword);
					user.setPassword(newPassword);
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "修改成功 ");
					request.setAttribute("mainPage", "user/passwordChange.jsp");
					request.getRequestDispatcher("mainUser.jsp").forward(request, response);
				} else {
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "原密码错误");
					request.setAttribute("mainPage", "user/passwordChange.jsp");
					request.getRequestDispatcher("mainUser.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void passwordPreChange(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		if("admin".equals((String)currentUserType)) {
			request.setAttribute("mainPage", "admin/passwordChange.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
		} else if("user".equals((String)currentUserType)) {
			request.setAttribute("mainPage", "user/passwordChange.jsp");
			request.getRequestDispatcher("mainUser.jsp").forward(request, response);
		}
	}
	
}
