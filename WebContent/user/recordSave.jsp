<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">
	function checkForm(){
        var buildName=document.getElementById("buildName").value;
        var roomName=document.getElementById("roomName").value;
        var date=document.getElementById("date").value;
        if(buildName==null||buildName==""){
            document.getElementById("error").innerHTML="楼不能为空！";
            return false;
        }
        if(roomName==null||roomName==""){
            document.getElementById("error").innerHTML="房间不能为空！";
            return false;
        }
        if(date==null){
            document.getElementById("error").innerHTML="日期不能为空！";
            return false;
        }
        return true;
	}
	
	$(document).ready(function(){
		$("ul li:eq(2)").addClass("active");
	});
</script>
<div class="data_list">
		<div class="data_list_title">
		<c:choose>
			<c:when test="${user.userId!=null }">
				预约记录
			</c:when>
			<c:otherwise>
				预约记录
			</c:otherwise>
		</c:choose>
		</div>
		<form action="record?action=save" method="post" onsubmit="return checkForm()">
			<div class="data_form" >
				<input type="hidden" id="recordId" name="recordId" value="${record.recordId }"/>
				<table align="center">
					<tr>
						<td><font color="red">*</font>日期：</td>
						<td><input type="text" id="date"  name="date" value="${record.date }"  style="margin-top:5px;height:30px;" /></td>
					</tr>
					<tr>
						<td><font color="red">*</font>楼：</td>
						<td>
							<select id="buildId" name="buildId" style="margin-top:5px;height:30px;">
								<c:forEach var="build" items="${buildList }">
									<option value="${build.buildId }" ${buildToSelect==build.buildId?'selected':'' }>${build.buildName }</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td><font color="red">*</font>房间：</td>
						<td><input type="text" id="roomName" name="roomName" value="${record.roomName }" style="margin-top:5px;height:30px;" /></td>
					</tr>
					<tr>
						<td>&nbsp;备注：</td>
						<td><textarea id="detail" name="detail" rows="10">${build.detail }</textarea></td>
					</tr>
				</table>
					<div align="center">
						<input type="submit" class="btn btn-primary" value="保存"/>
						&nbsp;<button class="btn btn-primary" type="button" onclick="javascript:window.location='record'">返回</button>
					</div>
					<div align="center">
						<font id="error" color="red">${error }</font>
					</div>
			</div>
		</form>
</div>