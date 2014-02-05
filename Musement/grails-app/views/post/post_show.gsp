<%--
  Created by IntelliJ IDEA.
  User: Dorian
  Date: 04/02/14
  Time: 21:47
--%>

<%@ page contentType="text/html;charset=UTF-8" %>

<g:render template="sendPostForm" model="[categoryId:categoryId]" />

<g:each in="${posts}" var="post">
    <g:render template="post" model="[categoryId:categoryId, post:post]"/>
</g:each>
