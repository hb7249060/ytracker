<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>系统错误</title>
</head>
<body>
    ${param.errMsg}
    <br/>
    <a href="javascript:history.back();">返回</a>
</body>
</html>