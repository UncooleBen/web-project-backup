package com.j2ee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.j2ee.model.Build;
import com.j2ee.model.User;
import com.j2ee.util.StringUtil;

public class UserDao {
	public List<User> userList(Connection con, User s_user)throws Exception {
		List<User> userList = new ArrayList<User>();
		StringBuffer sb = new StringBuffer("select * from t_user t1");
		if(StringUtil.isNotEmpty(s_user.getName())) {
			sb.append(" and t1.name like '%"+ s_user.getName()+"%'");
		} else if(StringUtil.isNotEmpty(s_user.getUserNumber())) {
			sb.append(" and t1.userNum like '%"+ s_user.getUserNumber()+"%'");
		} else if(StringUtil.isNotEmpty(s_user.getRoomName())) {
			sb.append(" and t1.roomName like '%"+ s_user.getRoomName()+"%'");
		}
		if(s_user.getBuildId()!=0) {
			sb.append(" and t1.buildId="+ s_user.getBuildId());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			User user =new User();
			user.setUserId(rs.getInt("userId"));
			int buildId = rs.getInt("buildId");
			user.setBuildId(buildId);
			user.setBuildName(BuildDao.buildName(con, buildId));
			user.setRoomName(rs.getString("roomName"));
			user.setName(rs.getString("name"));
			user.setSex(rs.getString("sex"));
			user.setUserNumber(rs.getString("userNum"));
			user.setTel(rs.getString("tel"));
			user.setPassword(rs.getString("password"));
			userList.add(user);
		}
		return userList;
	}
	
	public static User getNameById(Connection con, String userNumber, int buildId)throws Exception {
		String sql = "select * from t_user t1 where t1.userNum=? and t1.buildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, userNumber);
		pstmt.setInt(2, buildId);
		ResultSet rs=pstmt.executeQuery();
		User user = new User();
		if(rs.next()) {
			user.setName(rs.getString("name"));
			user.setBuildId(rs.getInt("buildId"));
			user.setRoomName(rs.getString("roomName"));
		}
		return user;
	}
	
	public boolean haveNameByNumber(Connection con, String userNumber)throws Exception {
		String sql = "select * from t_user t1 where t1.userNum=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, userNumber);
		ResultSet rs=pstmt.executeQuery();
		User user = new User();
		if(rs.next()) {
			user.setName(rs.getString("name"));
			user.setBuildId(rs.getInt("buildId"));
			user.setRoomName(rs.getString("roomName"));
			return true;
		}
		return false;
	}
	
	public List<User> userListWithBuild(Connection con, User s_user, int buildId)throws Exception {
		List<User> userList = new ArrayList<User>();
		StringBuffer sb = new StringBuffer("select * from t_user t1");
		if(StringUtil.isNotEmpty(s_user.getName())) {
			sb.append(" and t1.name like '%"+ s_user.getName()+"%'");
		} else if(StringUtil.isNotEmpty(s_user.getUserNumber())) {
			sb.append(" and t1.userNum like '%"+ s_user.getUserNumber()+"%'");
		} else if(StringUtil.isNotEmpty(s_user.getRoomName())) {
			sb.append(" and t1.roomName like '%"+ s_user.getRoomName()+"%'");
		}
		sb.append(" and t1.buildId="+buildId);
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			User user =new User();
			user.setUserId(rs.getInt("userId"));
			buildId = rs.getInt("buildId");
			user.setBuildId(buildId);
			user.setBuildName(BuildDao.buildName(con, buildId));
			user.setRoomName(rs.getString("roomName"));
			user.setName(rs.getString("name"));
			user.setSex(rs.getString("sex"));
			user.setUserNumber(rs.getString("userNum"));
			user.setTel(rs.getString("tel"));
			user.setPassword(rs.getString("password"));
			userList.add(user);
		}
		return userList;
	}
	
	public List<Build> buildList(Connection con)throws Exception {
		List<Build> buildList = new ArrayList<Build>();
		String sql = "select * from t_build";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Build build =new Build();
			build.setBuildId(rs.getInt("buildId"));
			build.setBuildName(rs.getString("buildName"));
			build.setDetail(rs.getString("buildDetail"));
			buildList.add(build);
		}
		return buildList;
	}
	
	public int userCount(Connection con, User s_user)throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) as total from t_user t1");
		if(StringUtil.isNotEmpty(s_user.getName())) {
			sb.append(" and t1.name like '%"+ s_user.getName()+"%'");
		} else if(StringUtil.isNotEmpty(s_user.getUserNumber())) {
			sb.append(" and t1.userNum like '%"+ s_user.getUserNumber()+"%'");
		} else if(StringUtil.isNotEmpty(s_user.getRoomName())) {
			sb.append(" and t1.roomName like '%"+ s_user.getRoomName()+"%'");
		}
		if(s_user.getBuildId()!=0) {
			sb.append(" and t1.buildId="+ s_user.getBuildId());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}
	
	public User userShow(Connection con, String userId)throws Exception {
		String sql = "select * from t_user t1 where t1.userId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, userId);
		ResultSet rs=pstmt.executeQuery();
		User user = new User();
		if(rs.next()) {
			user.setUserId(rs.getInt("userId"));
			int buildId = rs.getInt("buildId");
			user.setBuildId(buildId);
			user.setBuildName(BuildDao.buildName(con, buildId));
			user.setRoomName(rs.getString("roomName"));
			user.setName(rs.getString("name"));
			user.setSex(rs.getString("sex"));
			user.setUserNumber(rs.getString("userNum"));
			user.setTel(rs.getString("tel"));
			user.setPassword(rs.getString("password"));
		}
		return user;
	}
	
	public int userAdd(Connection con, User user)throws Exception {
		String sql = "insert into t_user values(null,?,?,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, user.getUserNumber());
		pstmt.setString(2, user.getPassword());
		pstmt.setString(3, user.getName());
		pstmt.setInt(4, user.getBuildId());
		pstmt.setString(5, user.getRoomName());
		pstmt.setString(6, user.getSex());
		pstmt.setString(7, user.getTel());
		return pstmt.executeUpdate();
	}
	
	public int userDelete(Connection con, String userId)throws Exception {
		String sql = "delete from t_user where userId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, userId);
		return pstmt.executeUpdate();
	}
	
	public int userUpdate(Connection con, User user)throws Exception {
		String sql = "update t_user set userNum=?,password=?,name=?,buildId=?,roomName=?,sex=?,tel=? where userId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, user.getUserNumber());
		pstmt.setString(2, user.getPassword());
		pstmt.setString(3, user.getName());
		pstmt.setInt(4, user.getBuildId());
		pstmt.setString(5, user.getRoomName());
		pstmt.setString(6, user.getSex());
		pstmt.setString(7, user.getTel());
		pstmt.setInt(8, user.getUserId());
		return pstmt.executeUpdate();
	}
	
	
}
