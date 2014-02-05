<%@ page import="musement.Post" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'post.label', default: 'Post')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                           default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-post" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list post">

        <g:if test="${postInstance?.sender}">
            <li class="fieldcontain">
                <span id="sender-label" class="property-label"><g:message code="post.sender.label"
                                                                          default="Sender"/></span>

                <span class="property-value" aria-labelledby="sender-label"><g:link controller="user" action="show"
                                                                                    id="${postInstance?.sender?.id}">${postInstance?.sender?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.category}">
            <li class="fieldcontain">
                <span id="category-label" class="property-label"><g:message code="post.category.label"
                                                                            default="Category"/></span>

                <span class="property-value" aria-labelledby="category-label"><g:link controller="category"
                                                                                      action="show"
                                                                                      id="${postInstance?.category?.id}">${postInstance?.category?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.content}">
            <li class="fieldcontain">
                <span id="content-label" class="property-label"><g:message code="post.content.label"
                                                                           default="Content"/></span>

                <span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${postInstance}"
                                                                                           field="content"/></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.postDate}">
            <li class="fieldcontain">
                <span id="postDate-label" class="property-label"><g:message code="post.postDate.label"
                                                                            default="Post Date"/></span>

                <span class="property-value" aria-labelledby="postDate-label"><g:formatDate
                        date="${postInstance?.postDate}"/></span>

            </li>
        </g:if>

    </ol>
    <g:form url="[resource: postInstance, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${postInstance}"><g:message code="default.button.edit.label"
                                                                                     default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
