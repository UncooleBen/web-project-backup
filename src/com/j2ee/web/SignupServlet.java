package com.j2ee.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.j2ee.dao.LoginDao;
import com.j2ee.model.User;
import com.j2ee.util.DbUtil;

public class SignupServlet extends HttpServlet {

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
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String tel = request.getParameter("tel");
        String sex = request.getParameter("sex");
        Connection con = null;
        try {
            con=dbUtil.getCon();
            User currentUser = null;
            User user = new User(userName, password);
            user.setName(name);
            user.setSex(sex);
            user.setTel(tel);
            currentUser = loginDao.Signup(con, user);
            if(currentUser == null) {
                request.setAttribute("user", user);
                request.setAttribute("error", "用户名已经存在！");
                request.getRequestDispatcher("signup.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "注册成功请登录！");
                request.getRequestDispatcher("login.jsp").forward(request, response);
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

}
