<%@ page import="musement.Category" %>
<%@ page import="musement.user.User" %>
<%@ page import="musement.user.Roles" %>
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
        <div class="hero-unit">
            <g:set var="user" value="${User.findById(params.getInt('userId'))}" />
                    <h4 class="alert alert-info">${user?.email}</h4>
                    <h4>${message(code: "musement.control.panel.users.categories")}<span class="badge badge-info">${user?.categories.size()}</span></h4>
                    <h4>${message(code: "musement.control.panel.users.posts")}<span class="badge badge-info">${user?.posts.size()}</span></h4>
            <g:link class="btn btn-danger" controller="userManagement" action="deleteUser" onclick="return confirm(${message(code: "musement.control.panel.users.sure")})" params='[userId: params.userId]'>${message(code: "musement.control.panel.categories.delete")}</g:link>
            <g:if test="${user.authorities.contains(Roles.ROLE_USER.role)}">
                <g:link class="btn btn-primary" controller="userManagement" action="makeAdmin" params='[userId: user.id]'>${message(code: "musement.control.panel.users.admin.make")}</g:link>
            </g:if>
            <g:if test="${user.authorities.contains(Roles.ROLE_ADMIN.role)}">
                <g:link class="btn btn-warning" controller="userManagement" action="removeAdmin" params='[userId: user.id]'>${message(code: "musement.control.panel.users.admin.remove")}</g:link>
            </g:if>
        </div>
    </div>
</div>