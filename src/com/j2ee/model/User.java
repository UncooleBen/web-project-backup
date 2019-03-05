package com.j2ee.model;

public class User {
	private int userId;
	private String userNumber;
	private String userName;
	private String password;
	private int buildId = 0;
	private String buildName;
	private String roomName;
	private String name;
	private String sex;
	private String tel;
	
	public User() {
	}

    public User(String userName, String password) {
        this.userNumber = userName;
        this.userName = userName;
        this.password = password;
    }

    public User(String userNumber, String password, int buildId, String roomName, String name, String sex, String tel) {
        this.userNumber = userNumber;
        this.password = password;
        this.buildId = buildId;
        this.roomName = roomName;
        this.name = name;
        this.tel = tel;
        this.sex = sex;
    }

    public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getBuildId() {
		return buildId;
	}

	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
