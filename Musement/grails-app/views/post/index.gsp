<%@ page import="musement.Post" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'post.label', default: 'Post')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                           default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-post" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:include controller="post" action="getPosts" params="[categoryId: musement.Category.findByName('Musement').id]"/>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <th><g:message code="post.sender.label" default="Sender"/></th>

            <th><g:message code="post.category.label" default="Category"/></th>

            <g:sortableColumn property="content" title="${message(code: 'post.content.label', default: 'Content')}"/>

            <g:sortableColumn property="postDate"
                              title="${message(code: 'post.postDate.label', default: 'Post Date')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${postInstanceList}" status="i" var="postInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${postInstance.id}">${fieldValue(bean: postInstance, field: "sender")}</g:link></td>

                <td>${fieldValue(bean: postInstance, field: "category")}</td>

                <td>${fieldValue(bean: postInstance, field: "content")}</td>

                <td><g:formatDate date="${postInstance.postDate}"/></td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${postInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
