package com.j2ee.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.j2ee.dao.UserDao;
import com.j2ee.model.User;
import com.j2ee.util.DbUtil;
import com.j2ee.util.StringUtil;

public class UserServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	UserDao userDao = new UserDao();
	
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
		Object currentUserType = session.getAttribute("currentUserType");
		String s_userText = request.getParameter("s_userText");
		String buildId = request.getParameter("buildToSelect");
		String searchType = request.getParameter("searchType");
		String action = request.getParameter("action");
		User user = new User();
		if("preSave".equals(action)) {
			userPreSave(request, response);
			return;
		} else if("save".equals(action)){
			userSave(request, response);
			return;
		} else if("delete".equals(action)){
			userDelete(request, response);
			return;
		} else if("list".equals(action)) {
			if(StringUtil.isNotEmpty(s_userText)) {
				if("name".equals(searchType)) {
					user.setName(s_userText);
				} else if("number".equals(searchType)) {
					user.setUserNumber(s_userText);
				} else if("dorm".equals(searchType)) {
					user.setRoomName(s_userText);
				}
			}
			if(StringUtil.isNotEmpty(buildId)) {
				user.setBuildId(Integer.parseInt(buildId));
			}
			session.removeAttribute("s_userText");
			session.removeAttribute("searchType");
			session.removeAttribute("buildToSelect");
			request.setAttribute("s_userText", s_userText);
			request.setAttribute("searchType", searchType);
			request.setAttribute("buildToSelect", buildId);
		} else if("search".equals(action)){
			if(StringUtil.isNotEmpty(s_userText)) {
				if("name".equals(searchType)) {
					user.setName(s_userText);
				} else if("number".equals(searchType)) {
					user.setUserNumber(s_userText);
				} else if("dorm".equals(searchType)) {
					user.setRoomName(s_userText);
				}
				session.setAttribute("s_userText", s_userText);
				session.setAttribute("searchType", searchType);
			} else {
				session.removeAttribute("s_userText");
				session.removeAttribute("searchType");
			}
			if(StringUtil.isNotEmpty(buildId)) {
				user.setBuildId(Integer.parseInt(buildId));
				session.setAttribute("buildToSelect", buildId);
			}else {
				session.removeAttribute("buildToSelect");
			}
		} else {
			if("admin".equals((String)currentUserType)) {
				if(StringUtil.isNotEmpty(s_userText)) {
					if("name".equals(searchType)) {
						user.setName(s_userText);
					} else if("number".equals(searchType)) {
						user.setUserNumber(s_userText);
					} else if("dorm".equals(searchType)) {
						user.setRoomName(s_userText);
					}
					session.setAttribute("s_userText", s_userText);
					session.setAttribute("searchType", searchType);
				}
				if(StringUtil.isNotEmpty(buildId)) {
					user.setBuildId(Integer.parseInt(buildId));
					session.setAttribute("buildToSelect", buildId);
				}
				if(StringUtil.isEmpty(s_userText) && StringUtil.isEmpty(buildId)) {
					Object o1 = session.getAttribute("s_userText");
					Object o2 = session.getAttribute("searchType");
					Object o3 = session.getAttribute("buildToSelect");
					if(o1!=null) {
						if("name".equals((String)o2)) {
							user.setName((String)o1);
						} else if("number".equals((String)o2)) {
							user.setUserNumber((String)o1);
						} else if("dorm".equals((String)o2)) {
							user.setRoomName((String)o1);
						}
					}
					if(o3 != null) {
						user.setBuildId(Integer.parseInt((String)o3));
					}
				}
			} else if("dormManager".equals((String)currentUserType)) {
				if(StringUtil.isNotEmpty(s_userText)) {
					if("name".equals(searchType)) {
						user.setName(s_userText);
					} else if("number".equals(searchType)) {
						user.setUserNumber(s_userText);
					} else if("dorm".equals(searchType)) {
						user.setRoomName(s_userText);
					}
					session.setAttribute("s_userText", s_userText);
					session.setAttribute("searchType", searchType);
				}
				if(StringUtil.isEmpty(s_userText)) {
					Object o1 = session.getAttribute("s_userText");
					Object o2 = session.getAttribute("searchType");
					if(o1!=null) {
						if("name".equals((String)o2)) {
							user.setName((String)o1);
						} else if("number".equals((String)o2)) {
							user.setUserNumber((String)o1);
						} else if("dorm".equals((String)o2)) {
							user.setRoomName((String)o1);
						}
					}
				}
			}
		}
		Connection con = null;
		try {
			con=dbUtil.getCon();
			if("admin".equals((String)currentUserType)) {
				List<User> userList = userDao.userList(con, user);
				request.setAttribute("buildList", userDao.buildList(con));
				request.setAttribute("userList", userList);
				request.setAttribute("mainPage", "admin/user.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
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

	private void userDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String userId = request.getParameter("userId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			userDao.userDelete(con, userId);
			request.getRequestDispatcher("user?action=list").forward(request, response);
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

	private void userSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String buildId = request.getParameter("buildId");
		String roomName = request.getParameter("roomName");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String tel = request.getParameter("tel");
		User user = new User(userName, password, Integer.parseInt(buildId), roomName, name, sex, tel);
		if(StringUtil.isNotEmpty(userId)) {
			user.setUserId(Integer.parseInt(userId));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			if(StringUtil.isNotEmpty(userId)) {
				saveNum = userDao.userUpdate(con, user);
			} else if(userDao.haveNameByNumber(con, user.getUserNumber())){
				request.setAttribute("user", user);
				request.setAttribute("error", "该账号已存在");
				request.setAttribute("mainPage", "admin/userSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			} else {
				saveNum = userDao.userAdd(con, user);
			}
			if(saveNum > 0) {
				request.getRequestDispatcher("user?action=list").forward(request, response);
			} else {
				request.setAttribute("user", user);
				request.setAttribute("error", "保存失败");
				request.setAttribute("mainPage", "admin/userSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
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

	private void userPreSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String userId = request.getParameter("userId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			request.setAttribute("buildList", userDao.buildList(con));
			if (StringUtil.isNotEmpty(userId)) {
				User user = userDao.userShow(con, userId);
				request.setAttribute("user", user);
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
		request.setAttribute("mainPage", "admin/userSave.jsp");
		request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
	}

	
}
