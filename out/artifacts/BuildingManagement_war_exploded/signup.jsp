<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page import="com.j2ee.model.Admin" %>
<%@ page import="com.j2ee.model.User" %>
<%
    if(request.getAttribute("user")==null){
        String userName=null;
        String password=null;
        String userType=null;


        if(userName==null){
            userName="";
        }

        if(password==null){
            password="";
        }

        if(userType==null){
            userType="";
        } else if("user".equals(userType)) {
            pageContext.setAttribute("user", new User(userName,password));
            pageContext.setAttribute("userType", 3);
        }

    }
%>
<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>场地预约与管理系统登录</title>
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/bootstrap/js/jQuery.js"></script>
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">
        function checkForm() {
            var userName = document.getElementById("userName").value;
            var password = document.getElementById("password").value;
            var sex = document.getElementById("sex").value;
            var tel = document.getElementById("tel").value;
            var name = document.getElementById("name").value;
            var userTypes = document.getElementsByName("userType");
            var userType = null;
            for(var i=0;i<userTypes.length;i++) {
                if(userTypes[i].checked) {
                    userType=userTypes[i].value;
                    break;
                }
            }
            if (userName == null || userName == "") {
                document.getElementById("error").innerHTML = "用户名不能为空";
                return false;
            }
            if (password == null || password == "") {
                document.getElementById("error").innerHTML = "密码不能为空";
                return false;
            }
            if (name == null || name == "") {
                document.getElementById("error").innerHTML = "姓名不能为空";
                return false;
            }
            if (sex == null || sex == "") {
                document.getElementById("error").innerHTML = "性别不能为空";
                return false;
            }
            if (tel == null || tel == "") {
                document.getElementById("error").innerHTML = "电话不能为空";
                return false;
            }
            return true;
        }
    </script>

    <style type="text/css">
        body {
            padding-top: 200px;
            padding-bottom: 40px;
            background-image: url('images/cd.jpg');
            background-position: center;
            background-repeat: no-repeat;
            background-attachment: fixed;
        }

        .radio {
            padding-top: 10px;
            padding-bottom:10px;
        }

        .form-signin-heading{
            text-align: center;
        }

        .form-signin {
            max-width: 300px;
            padding: 19px 29px 0px;
            margin: 0 auto 20px;
            background-color: #fff;
            border: 1px solid #e5e5e5;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
            -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
            -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
            box-shadow: 0 1px 2px rgba(0,0,0,.05);
        }
        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }
        .form-signin input[type="text"],
        .form-signin input[type="password"] {
            font-size: 16px;
            height: auto;
            margin-bottom: 15px;
            padding: 7px 9px;
        }
    </style>

</head>
<body>
<div class="container">
    <form name="myForm" class="form-signin" action="signup" method="post" onsubmit="return checkForm()">
        <h2 class="form-signin-heading"><font color="gray">场地预约与管理系统</font></h2>
        <input id="userName" name="userName" value="${user.userName }" type="text" class="input-block-level" placeholder="账号">
        <input id="name" name="name" value="${user.name }" type="text" class="input-block-level" placeholder="姓名">
        <input id="password" name="password" value="${user.password }" type="password" class="input-block-level" placeholder="密码" >
        <label class="radio inline">
            <input id="sex" type="radio" name="sex" value="男"  checked/> 男
        </label>
        <label class="radio inline">
            <input id="sex" type="radio" name="sex" value="女"  /> 女
        </label>
        <input id="tel" name="tel" value="${user.tel }" type="text" class="input-block-level" placeholder="电话">
        <font id="error" color="red">${error }</font><br>
        <div align="center" >
            <button class="btn btn-large btn-primary" type="submit">注册</button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button class="btn btn-large btn-primary" type="reset" >重置</button>
        </div>
        <p align="center" style="padding-top: 15px;"></p>
    </form>
</div>
</body>
</html>