package com.j2ee.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.j2ee.dao.CommentFinalDao;
import com.j2ee.dao.CommentDao;
import com.j2ee.model.Comment;
import com.j2ee.model.User;
import com.j2ee.util.DbUtil;
import com.j2ee.util.StringUtil;

public class CommentServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	CommentDao commentDao = new CommentDao();
	CommentFinalDao commentFinalDao = new CommentFinalDao();
	
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
		String searchType = request.getParameter("searchType");
		String action = request.getParameter("action");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		Comment comment = new Comment();
		if("add".equals(action)) {
		    commentPreSave(request, response);
		    return;
        } else if ("approve".equals(action)) {
		    commentApprove(request, response);
		    return;
        } else if ("preSave".equals(action)) {
			commentPreSave(request, response);
			return;
		} else if("save".equals(action)){
			commentSave(request, response);
			return;
		} else if("delete".equals(action)){
            commentDelete(request, response);
            return;
        } else if("finalDelete".equals(action)){
            commentFinalDelete(request, response);
            return;
        } else if("list".equals(action)) {
			if(StringUtil.isNotEmpty(s_userText)) {
				if("number".equals(searchType)) {
					comment.setUserNumber(s_userText);
				}
			}
			session.removeAttribute("s_userText");
			session.removeAttribute("searchType");
			request.setAttribute("s_userText", s_userText);
			request.setAttribute("searchType", searchType);
		} else if("search".equals(action)){
			if(StringUtil.isNotEmpty(s_userText)) {
				if("number".equals(searchType)) {
					comment.setUserNumber(s_userText);
				}
				session.setAttribute("s_userText", s_userText);
				session.setAttribute("searchType", searchType);
			} else {
				session.removeAttribute("s_userText");
				session.removeAttribute("searchType");
			}
			if(StringUtil.isNotEmpty(startDate)) {
				comment.setStartDate(startDate);
				session.setAttribute("startDate", startDate);
			} else {
				session.removeAttribute("startDate");
			}
			if(StringUtil.isNotEmpty(endDate)) {
				comment.setEndDate(endDate);
				session.setAttribute("endDate", endDate);
			} else {
				session.removeAttribute("endDate");
			}
		} 
		Connection con = null;
		try {
			con=dbUtil.getCon();
			if("admin".equals((String)currentUserType)) {
				List<Comment> commentList = commentDao.commentList(con, comment);
				List<Comment> commentFinalList = commentFinalDao.commentList(con, comment);
				request.setAttribute("commentList", commentList);
                request.setAttribute("commentFinalList", commentFinalList);
				request.setAttribute("mainPage", "admin/comment.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
			} else if("user".equals((String)currentUserType)) {
				User user = (User)(session.getAttribute("currentUser"));
				List<Comment> commentFinalList = commentFinalDao.commentList(con, comment);
				request.setAttribute("commentFinalList", commentFinalList);
				request.setAttribute("mainPage", "user/comment.jsp");
				request.getRequestDispatcher("mainUser.jsp").forward(request, response);
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

	private void commentDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String commentId = request.getParameter("commentId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			commentDao.commentDelete(con, commentId);
			request.getRequestDispatcher("comment?action=list").forward(request, response);
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

    private void commentFinalDelete(HttpServletRequest request,
                               HttpServletResponse response) {
        String commentId = request.getParameter("commentId");
        Connection con = null;
        try {
            con = dbUtil.getCon();
            commentFinalDao.commentDelete(con, commentId);
            request.getRequestDispatcher("comment?action=list").forward(request, response);
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

	private void commentSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		HttpSession session = request.getSession();
		String commentId = request.getParameter("commentId");
		User user1 = (User) (session.getAttribute("currentUser"));
		String userNumber = user1.getUserNumber();
		String date = request.getParameter("date");
		String detail = request.getParameter("detail");
		Comment comment = new Comment(userNumber, date, detail);
		if(StringUtil.isNotEmpty(commentId)) {
			if(Integer.parseInt(commentId)!=0) {
				comment.setCommentId(Integer.parseInt(commentId));
			}
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			if (comment.getDate()==null) {
                Calendar rightNow = Calendar.getInstance();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String sysDatetime = fmt.format(rightNow.getTime());
                comment.setDate(sysDatetime);
            }
			saveNum = commentDao.commentAdd(con, comment);
			if(saveNum > 0) {
				request.getRequestDispatcher("comment?action=list").forward(request, response);
			} else {
				request.setAttribute("comment", comment);
				request.setAttribute("error", "±£¥Ê ß∞‹");
				request.setAttribute("mainPage", "user/commentSave.jsp");
				request.getRequestDispatcher("mainUser.jsp").forward(request, response);
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

	private void commentApprove(HttpServletRequest request,
							 HttpServletResponse response)throws ServletException, IOException {
		HttpSession session = request.getSession();
		String commentId = request.getParameter("commentId");
		Comment comment = new Comment();
		if(StringUtil.isNotEmpty(commentId)) {
			if(Integer.parseInt(commentId)!=0) {
				comment.setCommentId(Integer.parseInt(commentId));
			}
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			Statement stmt = con.createStatement();
			int saveNum = 0;
			ResultSet rs;
			rs = stmt.executeQuery("select commentId, userNumber, date, detail from t_comment");
			while (rs.next()) {
			    if (rs.getInt(1)==comment.getCommentId()) {
			        comment.setUserNumber(rs.getString(2));
			        comment.setDate(rs.getString(3));
			        comment.setDetail(rs.getString(4));
                }
            }
			saveNum = commentFinalDao.commentAdd(con, comment);
			commentDao.commentDelete(con, commentId);
			if(saveNum > 0) {
				request.getRequestDispatcher("comment?action=list").forward(request, response);
			} else {
				request.setAttribute("comment", comment);
				request.setAttribute("error", "±£¥Ê ß∞‹");
				request.setAttribute("mainPage", "user/commentSave.jsp");
				request.getRequestDispatcher("mainUser.jsp").forward(request, response);
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

	private void commentPreSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String commentId = request.getParameter("commentId");
		String userNumber = request.getParameter("userNumber");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			if (StringUtil.isNotEmpty(commentId)) {
				Comment comment = commentDao.commentShow(con, commentId);
				request.setAttribute("comment", comment);
			} else {
				Calendar rightNow = Calendar.getInstance();
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				String sysDatetime = fmt.format(rightNow.getTime());
				request.setAttribute("userNumber", userNumber);
				request.setAttribute("date", sysDatetime);
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
		request.setAttribute("mainPage", "user/commentSave.jsp");
		request.getRequestDispatcher("mainUser.jsp").forward(request, response);
	}
}