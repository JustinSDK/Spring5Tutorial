<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Gossip 微網誌</title>
        <link rel="stylesheet" href="css/gossip.css" type="text/css">
    </head>
    <body>
        <div id="login">
            <div>
                <img src='images/caterpillar.jpg' alt='Gossip 微網誌'/>
            </div>    
            <a href='register'>還不是會員？</a>
            <p></p>
       
        <c:if test="${requestScope.errors != null}">
            <ul style='color: rgb(255, 0, 0);'>
            <c:forEach var="error" items="${requestScope.errors}">
                <li>${error}</li>
            </c:forEach>
            </ul>
        </c:if>

            <form method='post' action='login'>
                <table>
                    <tr>
                        <td colspan='2'>會員登入</td>
                    <tr>
                        <td>名稱：</td>
                        <td><input type='text' name='username' value='${param.username}'></td>
                    </tr>
                    <tr>
                        <td>密碼：</td>
                        <td><input type='password' name='password'></td>
                    </tr>
                    <tr>
                        <td colspan='2' align='center'><input type='submit' value='登入'></td>
                    </tr>
                    <tr>
                        <td colspan='2'><a href='forgotPwd.html'>忘記密碼？</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <div>
            <h1>Gossip ... XD</h1>
            <ul>
                <li>談天說地不奇怪</li>
                <li>分享訊息也可以</li>
                <li>隨意寫寫表心情</li>
            </ul>
            
		    <table style='background-color:#ffffff;'> 
		        <thead>
		            <tr>
		                <th><hr></th>
		            </tr>
		        </thead>
		        <tbody>            
                <c:forEach var="message" items="${requestScope.newest}">
		            <tr>
		                <td style='vertical-align: top;'>${message.username}<br>
		                    ${message.blabla}<br> ${message.localDateTime}
		                    <hr>
		                </td>
		            </tr>        
		        </c:forEach>
		        </tbody>
             </table>
    
        </div>
    </body>
</html>