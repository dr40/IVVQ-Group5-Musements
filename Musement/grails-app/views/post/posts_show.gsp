<%--
  Created by IntelliJ IDEA.
  User: Dorian
  Date: 04/02/14
  Time: 21:47
--%>

<%@ page contentType="text/html;charset=UTF-8" %>


<g:render template="postDeleteAlert" model="[categoryId:categoryId]" />

<g:render template="sendPostForm" model="[categoryId:categoryId]" />

<g:each in="${posts}" var="post">
    <g:include controller="post" action="renderAPost" params="[categoryId:categoryId, postId: post.id]"/>
</g:each>
