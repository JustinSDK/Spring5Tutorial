<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<title>Gossip 微網誌</title>
<link rel='stylesheet' href='css/member.css' type='text/css'>
</head>
<body>

    <div class='leftPanel'>
        <img src='images/caterpillar.jpg' alt='Gossip 微網誌' /><br>
        <br> <a href='logout'>登出 ${sessionScope.login}</a>
    </div>
    <form method='post' action='new_message'>
        分享新鮮事...<br>
        
        <c:if test="${param.blabla!=null}">
                   訊息要 140 字以內<br>
        </c:if>
        
        <textarea cols='60' rows='4' name='blabla'>${param.blabla}</textarea>
        <br>
        <button type='submit'>送出</button>
    </form>
    <table border='0' cellpadding='2' cellspacing='2'>
        <thead>
            <tr>
                <th><hr></th>
            </tr>
        </thead>
        <tbody>

        <c:forEach var="message" items="${requestScope.messages}">
            <tr>
                <td style='vertical-align: top;'>${message.username}<br>
                    ${message.blabla}<br> ${message.localDateTime}
                    <form method='post' action='del_message'>
                        <input type='hidden' name='millis' value='${message.millis}'>
                        <button type='submit'>刪除</button>
                    </form>
                    <hr>
                </td>
            </tr>            
        </c:forEach>

        </tbody>
    </table>
    <hr>
</body>
</html>