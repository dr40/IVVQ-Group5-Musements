<%--
  Created by IntelliJ IDEA.
  User: John
  Date: 2/1/14
  Time: 9:11 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Musement Registration</title>
</head>

<body>
    <sec:ifNotLoggedIn>
        <div class="col-lg-6">
            <div class="container" style="width: 90%; margin: auto">
                <h3><g:message code="musement.user.register"/></h3>
                <g:form controller="userManagement" action="register">
                    <fieldset>

                        <g:if test="${user?.hasErrors()}">
                            <div class="alert alert-warning">
                                <g:eachError bean="${user}">
                                    <li><g:message error="${it}"/></li>
                                </g:eachError>
                            </div>
                        </g:if>

                        <div class="form-group">
                            <input type="text" class="form-control" id="email" name="email"
                                   placeholder="Email"
                                   value="${fieldValue(bean: user, field: 'email')}">
                        </div>

                        <div class="form-group">
                            <input type="text" class="form-control"
                                   id="username" placeholder="Username"
                                   name="username"
                                   value="${fieldValue(bean: user, field: 'username')}">
                        </div>


                        <div class="form-group">
                            <input type="password" class="form-control"
                                   id="password" placeholder="Password" name="password">
                        </div>

                        <div class="form-group">
                            <input type="password" class="form-control"
                                   id="password2" placeholder="Confirm your password"
                                   name="password2">
                        </div>
                        <div class="form-group">
                            <g:each var="category" in="${musement.Category.findAll().sort { it.id }}">
                                <g:if test="${category?.name.equals('Musement')}">
                                    <label><input type="checkbox" class="form-control" name="categories" checked="checked" disabled="true" value="${category.name}">${category.name}</label>
                                </g:if>
                                <g:else>
                                    <label><input type="checkbox" class="form-control" name="categories" value="${category.name}">${category.name}</label>
                                </g:else>
                            </g:each>
                        </div>
                        <button type="submit"
                                class="btn btn-primary pull-left"><g:message code="musement.user.register"/></button>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </sec:ifNotLoggedIn>
</body>
</html>