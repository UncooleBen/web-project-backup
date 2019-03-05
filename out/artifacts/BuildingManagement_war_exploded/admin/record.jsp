<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
$(document).ready(function(){
	$("ul li:eq(4)").addClass("active");
	$('.form_date').datetimepicker({
	    language:  'en',
	    weekStart: 1,
	    todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
	});
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
	$("#DataTables_Table_0_wrapper .row-fluid").remove();
});

window.onload = function(){ 
	$("#DataTables_Table_0_wrapper .row-fluid").remove();
};
function recordDelete(recordId) {
    if(confirm("您确定要删除这条记录吗？")) {
        window.location="record?action=delete&recordId="+recordId;
    }
}
function recordApprove(recordId) {
    if(confirm("您确定审核通过这条记录吗？")) {
        window.location="record?action=approve&recordId="+recordId;
    }
}
</script>

<div class="data_list">
		<div class="data_list_title">
			已审核记录
		</div>
		<form name="myForm1" class="form-search" method="post" action="record?action=search" style="padding-bottom: 0px">
				<span class="data_search">
					<!--span class="controls input-append date form_date" style="margin-right: 10px" data-date="" data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
                    	<input id="startDate" name="startDate" style="width:120px;height: 30px;" placeholder="起始日期" type="text" value="${startDate }" readonly >
                    	<span class="add-on"><i class="icon-remove"></i></span>
						<span class="add-on"><i class="icon-th"></i></span>
               		</span>
					<span class="controls input-append date form_date" style="margin-right: 10px" data-date="" data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
                    	<input id="endDate" name="endDate" style="width:120px;height: 30px;" placeholder="终止日期" type="text" value="${endDate }" readonly>
                    	<span class="add-on"><i class="icon-remove"></i></span>
						<span class="add-on"><i class="icon-th"></i></span>
               		 </span-->
					<select id="buildToSelect1" name="buildToSelect1" style="width: 110px;">
					<option value="">全部场地</option>
					<c:forEach var="build" items="${buildList }">
						<option value="${build.buildId }" ${buildToSelect==build.buildId?'selected':'' }>${build.buildName }</option>
					</c:forEach>
					</select>
					<select id="searchType1" name="searchType1" style="width: 80px;">
					<option value="name">姓名</option>
					<option value="number" ${searchType eq "number"?'selected':'' }>客户</option>
					<option value="dorm" ${searchType eq "dorm"?'selected':'' }>天数</option>
					</select>
					&nbsp;<input id="s_userText1" name="s_userText1" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${s_userText }">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span>
		</form>
		<div>
			<table class="table table-striped table-bordered table-hover datatable">
				<thead>
					<tr>
					<th>起始日期</th>
					<th>客户</th>
					<th>姓名</th>
					<th>场地</th>
					<th>天数</th>
					<th>备注</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach  varStatus="i" var="record" items="${recordFinalList }">
					<tr>
						<td>${record.date }</td>
						<td>${record.userNumber }</td>
						<td>${record.userName }</td>
						<td>${record.buildName==null?"无":record.buildName }</td>
						<td>${record.roomName }</td>
						<td>${record.detail }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<div align="center"><font color="red">${error }</font></div>
    <div class="data_list_title">
        待审核记录
    </div>
    <form name="myForm" class="form-search" method="post" action="record?action=search" style="padding-bottom: 0px">
				<span class="data_search">
					<!--span class="controls input-append date form_date" style="margin-right: 10px" data-date="" data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
                    	<input id="startDate" name="startDate" style="width:120px;height: 30px;" placeholder="起始日期" type="text" value="${startDate }" readonly >
                    	<span class="add-on"><i class="icon-remove"></i></span>
						<span class="add-on"><i class="icon-th"></i></span>
               		</span>
					<span class="controls input-append date form_date" style="margin-right: 10px" data-date="" data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
                    	<input id="endDate" name="endDate" style="width:120px;height: 30px;" placeholder="终止日期" type="text" value="${endDate }" readonly>
                    	<span class="add-on"><i class="icon-remove"></i></span>
						<span class="add-on"><i class="icon-th"></i></span>
               		 </span-->
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
                <th>起始日期</th>
                <th>客户</th>
                <th>姓名</th>
                <th>场地</th>
                <th>天数</th>
                <th>备注</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach  varStatus="i" var="record" items="${recordList }">
                <tr>
                    <td>${record.date }</td>
                    <td>${record.userNumber }</td>
                    <td>${record.userName }</td>
                    <td>${record.buildName==null?"无":record.buildName }</td>
                    <td>${record.roomName }</td>
                    <td>${record.detail }</td>
                    <td>
                        <button class="btn btn-mini btn-danger" type="button" onclick="recordApprove(${record.recordId })">通过</button>
                        <button class="btn btn-mini btn-danger" type="button" onclick="recordDelete(${record.recordId })">取消</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div align="center"><font color="red">${error }</font></div>
</div>