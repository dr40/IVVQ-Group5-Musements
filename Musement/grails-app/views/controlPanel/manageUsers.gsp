<%@ page import="musement.Category" %>
<%@ page import="musement.user.User" %>
<!-- Alerts Info/Error -->
<g:render template="../category/alerts" model="[category: Category.findById(categoryId)]"/>

<div class="pull-left" style="text-align: center; width: 27%; margin: 20px auto">
    <h1>${message(code: "musement.control.panel.users.title")}</h1>

    <div class="well well-large">
        <div class="btn-group-vertical">
            <g:each in="${User.findAll().sort{it.id}}" var="user">
                <g:if test="${user.id == params.getInt("userId")}">
                    <g:link class="btn btn-success" controller="controlPanel" action="index" params='[editMode: "user", userId: user.id]'>${user.username}</g:link>
                </g:if>
                <g:else>
                    <g:link class="btn" controller="controlPanel" action="index" params='[editMode: "user", userId: user.id]'>${user.username}</g:link>
                </g:else>
            </g:each>
        </div>
    </div>
</div>

<div class="pull-right" style="width: 67%; overflow: auto;">
    <div style="margin: 50px auto auto 50px">
        <h1>${message(code: "musement.control.panel.users.description")}</h1>
        <div class="hero-unit">
            <g:set var="user" value="${User.findById(params.getInt('userId'))}" />
            <div class="badge-info">
                <g:if test="${user!= null}">
                    <span>${message(code: "musement.control.panel.users.categories")}</span>
                    <span>${user.categories.size()}</span>
                    </g:if>
            </div>
            <div class="badge-info">
                <g:if test="${user!= null}">
                    <span>${message(code: "musement.control.panel.users.posts")}</span>
                    <span>${user.posts.size()}</span>
                </g:if>
            </div>
            <g:link class="btn btn-danger" controller="userManagement" action="deleteUser" params='[userId: params.userId]'>${message(code: "musement.control.panel.categories.delete")}</g:link>
        </div>
    </div>
</div>