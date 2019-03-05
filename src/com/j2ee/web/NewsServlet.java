package com.j2ee.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.j2ee.dao.NewsDao;
import com.j2ee.model.News;
import com.j2ee.model.User;
import com.j2ee.util.DbUtil;
import com.j2ee.util.StringUtil;

public class NewsServlet extends HttpServlet{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    DbUtil dbUtil = new DbUtil();
    NewsDao newsDao = new NewsDao();

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

        News news = new News();
        if("add".equals(action)) {
            newsPreSave(request, response);
            return;
        } else if ("preSave".equals(action)) {
            newsPreSave(request, response);
            return;
        } else if("save".equals(action)){
            newsSave(request, response);
            return;
        } else if("delete".equals(action)){
            newsDelete(request, response);
            return;
        } else if("list".equals(action)) {
            session.removeAttribute("s_userText");
            session.removeAttribute("searchType");
            request.setAttribute("s_userText", s_userText);
            request.setAttribute("searchType", searchType);
        } else if("detail".equals(action)) {
            newsDetail(request, response);
            return;
        } else if("search".equals(action)){
            if(StringUtil.isNotEmpty(s_userText)) {
                session.setAttribute("s_userText", s_userText);
                session.setAttribute("searchType", searchType);
            } else {
                session.removeAttribute("s_userText");
                session.removeAttribute("searchType");
            }
            if(StringUtil.isNotEmpty(startDate)) {
                news.setStartDate(startDate);
                session.setAttribute("startDate", startDate);
            } else {
                session.removeAttribute("startDate");
            }
            if(StringUtil.isNotEmpty(endDate)) {
                news.setEndDate(endDate);
                session.setAttribute("endDate", endDate);
            } else {
                session.removeAttribute("endDate");
            }
        }
        Connection con = null;
        try {
            con=dbUtil.getCon();
            if("admin".equals((String)currentUserType)) {
                List<News> newsList = newsDao.newsList(con, news);
                request.setAttribute("newsList", newsList);
                request.setAttribute("mainPage", "admin/news.jsp");
                request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
            } else if("user".equals((String)currentUserType)) {
                User user = (User)(session.getAttribute("currentUser"));
                List<News> newsList = newsDao.newsList(con, news);
                request.setAttribute("newsList", newsList);
                request.setAttribute("mainPage", "user/news.jsp");
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

    private void newsDelete(HttpServletRequest request,
                               HttpServletResponse response) {
        String newsId = request.getParameter("newsId");
        Connection con = null;
        try {
            con = dbUtil.getCon();
            newsDao.newsDelete(con, newsId);
            request.getRequestDispatcher("news?action=list").forward(request, response);
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
    private void newsDetail(HttpServletRequest request,
                            HttpServletResponse response) {
        String newsId = request.getParameter("newsId");
        Connection con = null;
        try {
            con = dbUtil.getCon();
            NewsDao newsDao = new NewsDao();
            News news = newsDao.newsShow(con, newsId);
            request.setAttribute("news", news);
            HttpSession session = request.getSession();
            User user = (User)(session.getAttribute("currentUser"));
            List<News> newsList = newsDao.newsList(con, news);
            request.setAttribute("mainPage", "user/newsDetail.jsp");
            request.getRequestDispatcher("mainUser.jsp").forward(request, response);
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
    private void newsSave(HttpServletRequest request,
                             HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession();
        String newsId = request.getParameter("newsId");
        String date = request.getParameter("date");
        String detail = request.getParameter("detail");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        News news = new News(title, author, date, detail);
        if(StringUtil.isNotEmpty(newsId)) {
            if(Integer.parseInt(newsId)!=0) {
                news.setNewsId(Integer.parseInt(newsId));
            }
        }
        Connection con = null;
        try {
            con = dbUtil.getCon();
            int saveNum = 0;
            if (news.getDate()==null) {
                Calendar rightNow = Calendar.getInstance();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String sysDatetime = fmt.format(rightNow.getTime());
                news.setDate(sysDatetime);
            }
            saveNum = newsDao.newsUpdate(con, news);
            if (saveNum==0) {
                saveNum = newsDao.newsAdd(con, news);
            }
            if(saveNum > 0) {
                request.getRequestDispatcher("news?action=list").forward(request, response);
            } else {
                request.setAttribute("news", news);
                request.setAttribute("error", "����ʧ��");
                request.setAttribute("mainPage", "admin/newsSave.jsp");
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

    private void newsPreSave(HttpServletRequest request,
                                HttpServletResponse response)throws ServletException, IOException {
        String newsId = request.getParameter("newsId");
        Connection con = null;
        try {
            con = dbUtil.getCon();
            if (StringUtil.isNotEmpty(newsId)) {
                News news = newsDao.newsShow(con, newsId);
                request.setAttribute("news", news);
            } else {
                Calendar rightNow = Calendar.getInstance();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String sysDatetime = fmt.format(rightNow.getTime());
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
        request.setAttribute("mainPage", "admin/newsSave.jsp");
        request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
    }
}