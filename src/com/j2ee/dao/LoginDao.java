package com.j2ee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.j2ee.model.Admin;
import com.j2ee.model.User;

public class LoginDao {

	public Admin Login(Connection con, Admin admin)throws Exception {
		Admin resultAdmin = null;
		String sql = "select * from t_admin where userName=? and password=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, admin.getUserName());
		pstmt.setString(2, admin.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			resultAdmin = new Admin();
			resultAdmin.setAdminId(rs.getInt("adminId"));
			resultAdmin.setUserName(rs.getString("userName"));
			resultAdmin.setPassword(rs.getString("password"));
			resultAdmin.setName(rs.getString("name"));
			resultAdmin.setSex(rs.getString("sex"));
			resultAdmin.setTel(rs.getString("tel"));
		}
		return resultAdmin;
	}

	
	public User Login(Connection con, User user)throws Exception {
		User resultUser = null;
		String sql = "select * from t_user where userNum=? and password=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, user.getUserNumber());
		pstmt.setString(2, user.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			resultUser = new User();
			resultUser.setUserId(rs.getInt("userId"));
			resultUser.setUserNumber(rs.getString("userNum"));
			resultUser.setPassword(rs.getString("password"));
			int buildId = rs.getInt("buildId");
			resultUser.setBuildId(buildId);
			resultUser.setBuildName(BuildDao.buildName(con, buildId));
			resultUser.setRoomName(rs.getString("roomName"));
			resultUser.setName(rs.getString("name"));
			resultUser.setSex(rs.getString("sex"));
			resultUser.setTel(rs.getString("tel"));
		}
		return resultUser;
	}

	public User Signup(Connection con, User user) throws Exception {
		User resultUser = null;
		String sql = "select * from t_user where userNum=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, user.getUserNumber());

		ResultSet rs = pstmt.executeQuery();
		if(!rs.next()) {
            UserDao userDao = new UserDao();
            int addSum = userDao.userAdd(con, user);
            if (addSum>0) resultUser = user;
        }
		return resultUser;
	}

	public int adminUpdate(Connection con, int adminId, String password)throws Exception {
		String sql = "update t_admin set password=? where adminId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, password);
		pstmt.setInt(2, adminId);
		return pstmt.executeUpdate();
	}

	public int userUpdate(Connection con, int userId, String password)throws Exception {
		String sql = "update t_user set password=? where userId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, password);
		pstmt.setInt(2, userId);
		return pstmt.executeUpdate();
	}
	
}
