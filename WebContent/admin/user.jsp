<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">

$(document).ready(function(){
	$("ul li:eq(2)").addClass("active");
	$('.datatable').dataTable( {        				
		 "oLanguage": {
				"sUrl": "/DormManage/media/zh_CN.json"
		 },
		"bLengthChange": false, //改变每页显示数据数量
		"bFilter": false, //过滤功能
		"aoColumns": [
			null,
			null,
			null,
			null,
			null,
			{ "asSorting": [ ] },
			{ "asSorting": [ ] }
		]
	});
});

window.onload = function(){ 
	$("#DataTables_Table_0_wrapper .row-fluid").remove();
};
	function userDelete(userId) {
		if(confirm("您确定要删除这个用户吗？")) {
			window.location="user?action=delete&userId="+userId;
		}
	}
</script>
<style type="text/css">
	.span6 {
		width:0px;
		height: 0px;
		padding-top: 0px;
		padding-bottom: 0px;
		margin-top: 0px;
		margin-bottom: 0px;
	}

</style>
<div class="data_list">
		<div class="data_list_title">
			用户管理
		</div>
		<form name="myForm" class="form-search" method="post" action="user?action=search" style="padding-bottom: 0px">
				<button class="btn btn-success" type="button" style="margin-right: 50px;" onclick="javascript:window.location='user?action=preSave'">添加</button>
				<span class="data_search">
					<select id="buildToSelect" name="buildToSelect" style="width: 110px;">
					<option value="">全部场地</option>
					<c:forEach var="build" items="${buildList }">
						<option value="${build.buildId }" ${buildToSelect==build.buildId?'selected':'' }>${build.buildName }</option>
					</c:forEach>
					</select>
					<select id="searchType" name="searchType" style="width: 80px;">
					<option value="name">姓名</option>
					<option value="number" ${searchType eq "number"?'selected':'' }>客户</option>
					<option value="dorm" ${searchType eq "dorm"?'selected':'' }>天数</option>
					</select>
					&nbsp;<input id="s_userText" name="s_userText" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${s_userText }">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span>
		</form>
		<div>
			<table class="table table-striped table-bordered table-hover datatable">
				<thead>
					<tr>
					<!-- <th>编号</th> -->
					<th>客户</th>
					<th>姓名</th>
					<th>性别</th>
					<th>场地</th>
					<th>天数</th>
					<th>电话</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach  varStatus="i" var="user" items="${userList }">
					<tr>
						<%-- <td>${i.count+(page-1)*pageSize }</td> --%>
						<td>${user.userNumber }</td>
						<td>${user.name }</td>
						<td>${user.sex }</td>
						<td>${user.buildName==null?"无":user.buildName }</td>
						<td>${user.roomName==null||user.roomName=="0"?"无":user.roomName }</td>
						<td>${user.tel }</td>
						<td><button class="btn btn-mini btn-info" type="button" onclick="javascript:window.location='user?action=preSave&userId=${user.userId }'">修改</button>&nbsp;
							<button class="btn btn-mini btn-danger" type="button" onclick="userDelete(${user.userId })">删除</button></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<div align="center"><font color="red">${error }</font></div>
		<%-- <div class="pagination pagination-centered">
			<ul>
				${pageCode }
			</ul>
		</div> --%>
</div>