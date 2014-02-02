<%--
  Created by IntelliJ IDEA.
  User: John
  Date: 2/1/14
  Time: 8:37 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Update account details</title>
</head>

<body>
    <sec:ifLoggedIn>
        <div class="col-lg-6">
            <div class="container" style="width: 90%; margin: auto">
                <h3><g:message code="musement.user.update"/></h3>
                <g:form controller="userManagement" action="update">
                    <fieldset>

                        <g:if test="${user?.hasErrors()}">
                            <div class="alert alert-warning">
                                <g:eachError bean="${user}">
                                    <li><g:message error="${it}"/></li>
                                </g:eachError>
                            </div>
                        </g:if>

                        <div class="form-group"><g:message code="musement.username"/>: ${user.username}</div>
                        <div class="form-group"><g:message code="musement.user.register.email"/>: ${user.email}</div>

                        <div class="form-group">
                            <input type="password" class="form-control"
                                   id="password" placeholder="Current password" name="password">
                        </div>

                        <div class="form-group">
                            <input type="password" class="form-control"
                                   id="password2" placeholder="New password"
                                   name="password2">
                        </div>

                        <div class="form-group">
                            <input type="password" class="form-control"
                                   id="password3" placeholder="Re-type password"
                                   name="password3">
                        </div>

                        <button type="submit"
                                class="btn btn-primary pull-left"><g:message code="musement.user.update"/></button>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </sec:ifLoggedIn>
</body>
</html>