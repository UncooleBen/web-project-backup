package com.j2ee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.j2ee.model.Build;
import com.j2ee.model.Record;
import com.j2ee.util.StringUtil;

public class RecordFinalDao {
    public List<Record> recordList(Connection con, Record s_record)throws Exception {
        List<Record> recordList = new ArrayList<Record>();
        StringBuffer sb = new StringBuffer("select * from t_record_final t1");
        if(StringUtil.isNotEmpty(s_record.getUserNumber())) {
            sb.append(" and t1.userNumber like '%"+s_record.getUserNumber()+"%'");
        } else if(StringUtil.isNotEmpty(s_record.getUserName())) {
            sb.append(" and t1.userName like '%"+s_record.getUserName()+"%'");
        }
        if(s_record.getBuildId()!=0) {
            sb.append(" and t1.buildId="+s_record.getBuildId());
        }
        if(StringUtil.isNotEmpty(s_record.getDate())) {
            sb.append(" and t1.date="+s_record.getDate());
        }
        if(StringUtil.isNotEmpty(s_record.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_record.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_record.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_record.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Record record=new Record();
            record.setRecordId(rs.getInt("recordId"));
            record.setUserNumber(rs.getString("userNumber"));
            record.setUserName(rs.getString("userName"));
            int buildId = rs.getInt("buildId");
            record.setBuildId(buildId);
            record.setBuildName(BuildDao.buildName(con, buildId));
            record.setRoomName(rs.getString("roomName"));
            record.setDate(rs.getString("date"));
            record.setDetail(rs.getString("detail"));
            recordList.add(record);
        }
        return recordList;
    }

    public List<Record> recordListWithBuild(Connection con, Record s_record, int buildId)throws Exception {
        List<Record> recordList = new ArrayList<Record>();
        StringBuffer sb = new StringBuffer("select * from t_record_final t1");
        if(StringUtil.isNotEmpty(s_record.getUserNumber())) {
            sb.append(" and t1.userNumber like '%"+s_record.getUserNumber()+"%'");
        } else if(StringUtil.isNotEmpty(s_record.getUserName())) {
            sb.append(" and t1.userName like '%"+s_record.getUserName()+"%'");
        }
        sb.append(" and t1.buildId="+buildId);
        if(StringUtil.isNotEmpty(s_record.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_record.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_record.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_record.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Record record=new Record();
            record.setRecordId(rs.getInt("recordId"));
            record.setUserNumber(rs.getString("userNumber"));
            record.setUserName(rs.getString("userName"));
            buildId = rs.getInt("buildId");
            record.setBuildId(buildId);
            record.setBuildName(BuildDao.buildName(con, buildId));
            record.setRoomName(rs.getString("roomName"));
            record.setDate(rs.getString("date"));
            record.setDetail(rs.getString("detail"));
            recordList.add(record);
        }
        return recordList;
    }

    public List<Record> recordListWithNumber(Connection con, Record s_record, String userNumber)throws Exception {
        List<Record> recordList = new ArrayList<Record>();
        StringBuffer sb = new StringBuffer("select * from t_record_final t1");
        if(StringUtil.isNotEmpty(userNumber)) {
            sb.append(" and t1.userNumber ="+userNumber);
        }
        if(StringUtil.isNotEmpty(s_record.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_record.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_record.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_record.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Record record=new Record();
            record.setRecordId(rs.getInt("recordId"));
            record.setUserNumber(rs.getString("userNumber"));
            record.setUserName(rs.getString("userName"));
            int buildId = rs.getInt("buildId");
            record.setBuildId(buildId);
            record.setBuildName(BuildDao.buildName(con, buildId));
            record.setRoomName(rs.getString("roomName"));
            record.setDate(rs.getString("date"));
            record.setDetail(rs.getString("detail"));
            recordList.add(record);
        }
        return recordList;
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

    public Record recordShow(Connection con, String recordId)throws Exception {
        String sql = "select * from t_record_final t1 where t1.recordId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, recordId);
        ResultSet rs=pstmt.executeQuery();
        Record record = new Record();
        if(rs.next()) {
            record.setRecordId(rs.getInt("recordId"));
            record.setUserNumber(rs.getString("userNumber"));
            record.setUserName(rs.getString("userName"));
            int buildId = rs.getInt("buildId");
            record.setBuildId(buildId);
            record.setBuildName(BuildDao.buildName(con, buildId));
            record.setRoomName(rs.getString("roomName"));
            record.setDate(rs.getString("date"));
            record.setDetail(rs.getString("detail"));
        }
        return record;
    }

    public int recordAdd(Connection con, Record record)throws Exception {
        String sql = "insert into t_record_final values(null,?,?,?,?,?,?)";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, record.getUserNumber());
        pstmt.setString(2, record.getUserName());
        pstmt.setInt(3, record.getBuildId());
        pstmt.setString(4, record.getRoomName());
        pstmt.setString(5, record.getDate());
        pstmt.setString(6, record.getDetail());
        return pstmt.executeUpdate();
    }

    public int recordDelete(Connection con, String recordId)throws Exception {
        String sql = "delete from t_record_final where recordId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, recordId);
        return pstmt.executeUpdate();
    }

    public int recordUpdate(Connection con, Record record)throws Exception {
        String sql = "update t_record_final set userNumber=?,userName=?,buildId=?,roomName=?,detail=? where recordId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, record.getUserNumber());
        pstmt.setString(2, record.getUserName());
        pstmt.setInt(3, record.getBuildId());
        pstmt.setString(4, record.getRoomName());
        pstmt.setString(5, record.getDetail());
        pstmt.setInt(6, record.getRecordId());
        return pstmt.executeUpdate();
    }


}
