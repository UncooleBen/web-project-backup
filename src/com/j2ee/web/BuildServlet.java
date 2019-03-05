package com.j2ee.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.j2ee.dao.BuildDao;
import com.j2ee.model.Build;
import com.j2ee.model.PageBean;
import com.j2ee.util.DbUtil;
import com.j2ee.util.PropertiesUtil;
import com.j2ee.util.StringUtil;

public class BuildServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	BuildDao buildDao = new BuildDao();
	
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
		String s_buildName = request.getParameter("s_buildName");
		String page = request.getParameter("page");
		String action = request.getParameter("action");
		Build build = new Build();
		if("preSave".equals(action)) {
			buildPreSave(request, response);
			return;
		} else if("save".equals(action)){
			buildSave(request, response);
			return;
		} else if("delete".equals(action)){
			buildDelete(request, response);
			return;
		} else if("list".equals(action)) {
			if(StringUtil.isNotEmpty(s_buildName)) {
				build.setBuildName(s_buildName);
			}
			session.removeAttribute("s_buildName");
			request.setAttribute("s_buildName", s_buildName);
		} else if("search".equals(action)){
			if(StringUtil.isNotEmpty(s_buildName)) {
				build.setBuildName(s_buildName);
				session.setAttribute("s_buildName", s_buildName);
			}else {
				session.removeAttribute("s_buildName");
			}
		} else {
			if(StringUtil.isNotEmpty(s_buildName)) {
				build.setBuildName(s_buildName);
				session.setAttribute("s_buildName", s_buildName);
			}
			if(StringUtil.isEmpty(s_buildName)) {
				Object o = session.getAttribute("s_buildName");
				if(o!=null) {
					build.setBuildName((String)o);
				}
			}
		}
		if(StringUtil.isEmpty(page)) {
			page="1";
		}
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
		request.setAttribute("pageSize", pageBean.getPageSize());
		request.setAttribute("page", pageBean.getPage());
		try {
			con=dbUtil.getCon();
			List<Build> buildList = buildDao.buildList(con, pageBean, build);
			int total= buildDao.buildCount(con, build);
			String pageCode = this.genPagation(total, Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			request.setAttribute("pageCode", pageCode);
			request.setAttribute("buildList", buildList);
			if ("admin".equals((String)currentUserType)) {
				request.setAttribute("mainPage", "admin/build.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
			} else {
				request.setAttribute("mainPage", "user/build.jsp");
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

	private void buildDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String buildId = request.getParameter("buildId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			buildDao.buildDelete(con, buildId);
			request.getRequestDispatcher("build?action=list").forward(request, response);
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

	private void buildSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String buildId = request.getParameter("buildId");
		String buildName = request.getParameter("buildName");
		String detail = request.getParameter("detail");
		String price = request.getParameter("buildPrice");
		Build build = new Build(buildName, detail);
		build.setPrice(price);
		if(StringUtil.isNotEmpty(buildId)) {
			build.setBuildId(Integer.parseInt(buildId));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			if(StringUtil.isNotEmpty(buildId)) {
				saveNum = buildDao.buildUpdate(con, build);
			} else {
				saveNum = buildDao.buildAdd(con, build);
			}
			if(saveNum > 0) {
				request.getRequestDispatcher("build?action=list").forward(request, response);
			} else {
				request.setAttribute("build", build);
				request.setAttribute("error", "����ʧ��");
				request.setAttribute("mainPage", "build/buildSave.jsp");
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

	private void buildPreSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String buildId = request.getParameter("buildId");
		if(StringUtil.isNotEmpty(buildId)) {
			Connection con = null;
			try {
				con = dbUtil.getCon();
				Build build = buildDao.buildShow(con, buildId);
				request.setAttribute("build", build);
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
		request.setAttribute("mainPage", "admin/buildSave.jsp");
		request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
	}

	private String genPagation(int totalNum, int currentPage, int pageSize){
		int totalPage = totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<li><a href='build?page=1'>��ҳ</a></li>");
		if(currentPage==1) {
			pageCode.append("<li class='disabled'><a href='#'>��һҳ</a></li>");
		}else {
			pageCode.append("<li><a href='build?page="+(currentPage-1)+"'>��һҳ</a></li>");
		}
		for(int i=currentPage-2;i<=currentPage+2;i++) {
			if(i<1||i>totalPage) {
				continue;
			}
			if(i==currentPage) {
				pageCode.append("<li class='active'><a href='#'>"+i+"</a></li>");
			} else {
				pageCode.append("<li><a href='build?page="+i+"'>"+i+"</a></li>");
			}
		}
		if(currentPage==totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>��һҳ</a></li>");
		} else {
			pageCode.append("<li><a href='build?page="+(currentPage+1)+"'>��һҳ</a></li>");
		}
		pageCode.append("<li><a href='build?page="+totalPage+"'>βҳ</a></li>");
		return pageCode.toString();
	}
	
}
