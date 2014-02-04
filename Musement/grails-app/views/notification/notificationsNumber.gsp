<%--
  Created by IntelliJ IDEA.
  User: Aleca
  Date: 2/5/14
  Time: 12:48 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
<g:if test="${notificationsNumber > 0}">
    <span class="badge pull-right" style="margin-left: 5px" >${notificationsNumber}</span>
</g:if>
</body>
</html>