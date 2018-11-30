<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<title>Gossip 微網誌</title>
<link rel='stylesheet' href='../css/member.css' type='text/css'>
</head>
<body>

    <div class='leftPanel'>
        <img src='../images/caterpillar.jpg' alt='Gossip 微網誌' /><br>
        <br>${requestScope.username} 的微網誌
    </div>
    <table border='0' cellpadding='2' cellspacing='2'>
        <thead>
            <tr>
                <th><hr></th>
            </tr>
        </thead>
        <tbody>

       <c:choose>
	       <c:when test="${requestScope.errors != null}">
	            <ul>
	            <c:forEach var="error" items="${requestScope.errors}">
	                <li>${error}</li>
	            </c:forEach>
	            </ul>
	       </c:when>
	       <c:otherwise>
		        <c:forEach var="message" items="${requestScope.messages}">
		            <tr>
		                <td style='vertical-align: top;'>${message.username}<br>
		                    ${message.blabla}<br> ${message.localDateTime}
		                    <hr>
		                </td>
		            </tr>        
		        </c:forEach>	       
	       </c:otherwise>
       </c:choose>

        </tbody>
    </table>
    <hr>
</body>
</html>