<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<title>啟用帳號</title>
</head>
<body>
	<c:choose>
	    <c:when test="${requestScope.acct.present}">
	        <h1>帳號啟用成功</h1>
	    </c:when>
	    <c:otherwise>
	        <h1>帳號啟用失敗</h1>
	    </c:otherwise>
	</c:choose>
	<a href='/gossip'>回首頁</a>
</body>
</html>