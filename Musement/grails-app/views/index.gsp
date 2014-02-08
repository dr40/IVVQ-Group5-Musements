<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="../layouts/musement"/>
    <title>Welcome to Musement</title>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/Musement">Musement</a>
            <sec:ifLoggedIn>
                <div class="nav-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li><g:link controller="userManagement" action="home" ><g:message code="musement.user.home"/></g:link></li>
                        <li><g:link controller="logout" ><g:message code="musement.logout"/></g:link></li>
                    </ul>
                </div>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <div class="nav-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li><g:link controller="login" ><g:message code="musement.login"/></g:link></li>
                    </ul>
                </div>
            </sec:ifNotLoggedIn>
        </div>
    </div>
</div>

<div class="container" style="margin-top: 40px;">

    <div class="pull-left" style="text-align: center; width: 27%; margin: 20px auto; position: fixed">

        <h2>${message(code: "musement.index.controllers")}</h2>

        <div class="well well-large">
            <div class="btn-group-vertical">
                <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                    <g:link class="btn" controller="${c.logicalPropertyName}">${c.fullName}</g:link>
                </g:each>
            </div>
        </div>
    </div>

    <div class="pull-right" style="width: 67%; overflow: auto;">
        <div style="margin: 20px auto auto 20px; text-align: center">
            <sec:ifNotLoggedIn>
                <h2><g:message code="musement.user.register"/></h2>

                <!-- Alerts Info/Error -->
                <g:render template="/userManagement/alerts" model="[user: user]"/>

                <g:form class="hero-unit" controller="userManagement" action="doRegister" style="width: 300px; margin: auto">
                    <fieldset>

                        <div class="form-group fieldcontain ${hasErrors(bean: user, field: 'email', 'error')} required">
                            <input type="text" class="form-control" id="email" name="email"
                                   placeholder='${message(code: "musement.user.register.email")}'
                                   pattern="^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$"
                                   required="required"
                                   oninvalid="this.setCustomValidity(${message(code: 'musement.user.register.email.match')})"
                                   oninput="setCustomValidity('')"
                                   value="${fieldValue(bean: user, field: 'email')}">
                        </div>

                        <div class="form-group fieldcontain ${hasErrors(bean: user, field: 'username', 'error')} required">
                            <input type="text" class="form-control" id="username" name="username"
                                   placeholder='${message(code: "musement.username")}'
                                   pattern="^[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*$"
                                   required="required"
                                   oninvalid="setCustomValidity(${message(code: "musement.user.register.username.match")})"
                                   oninput="setCustomValidity('')"
                                   value="${fieldValue(bean: user, field: 'username')}">
                        </div>


                        <div class="form-group fieldcontain ${hasErrors(bean: user, field: 'password', 'error')} required">
                            <input type="password" class="form-control" id="password" name="password"
                                   pattern="^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$"
                                   required="required"
                                   oninvalid="setCustomValidity(${message(code: "musement.user.password.strenght")})"
                                   oninput="setCustomValidity('')"
                                   placeholder='${message(code: "musement.password")}' >
                        </div>

                        <div class="form-group fieldcontain ${hasErrors(bean: user, field: 'password', 'error')} required">
                            <input type="password" class="form-control" id="password2" name="password2"
                                   pattern="^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$"
                                   required="required"
                                   oninvalid="setCustomValidity(${message(code: "musement.user.password.strenght")})"
                                   oninput="setCustomValidity('')"
                                   placeholder='${message(code: "musement.user.register.password2")}'>
                        </div>

                        <div class="form-group">
                            <g:each var="category" in="${musement.Category.findAll().sort { it.id }}">
                                <g:if test="${category?.name.equals('Musement')}">
                                    <label><g:checkBox class="form-control" name="cats" checked="true" disabled="true" value="${category.name}" />${category.name}</label>
                                </g:if>
                                <g:else>
                                    <label><g:checkBox class="form-control" name="cats" value="${category.name}" />${category.name}</label>
                                </g:else>
                            </g:each>
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary"><g:message code="musement.user.register"/></button>
                            <button type="button" class="btn btn-warning" onclick="window.history.back()"><g:message code="musement.user.cancel"/></button>
                        </div>

                    </fieldset>
                </g:form>
            </sec:ifNotLoggedIn>

            <sec:ifLoggedIn>
                <div class='alert alert-info' style="text-align: left">
                    ${message(code: "musement.index.loggedin")}
                </div>
            </sec:ifLoggedIn>
        </div>
    </div>

</div> <!-- /container -->
</body>
</html>