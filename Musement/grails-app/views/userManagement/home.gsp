<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="../layouts/musement"/>
    <sec:ifLoggedIn>
        <title>Musement | ${user?.username} - Home</title>
    </sec:ifLoggedIn>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar pull-right"></span>
            </button>
            <a class="brand" href="/Musement">Musement</a>
            <sec:ifNotLoggedIn>
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li><g:link controller="login" ><g:message code="musement.login"/></g:link></li>
                        <li><g:link controller="userManagement" action="register" ><g:message code="musement.user.register"/></g:link></li>
                    </ul>
                </div>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li><g:link controller="userManagement" action="update" ><g:message code="musement.user.update"/></g:link></li>
                        <li><g:link controller="logout" ><g:message code="musement.logout"/></g:link></li>
                        <li><g:link controller="category" ><g:message code="musement.home.notifications"/></g:link></li>
                    </ul>
                </div>
            </sec:ifLoggedIn>
        </div>
    </div>
</div>

<div class="container" style="margin-top: 50px">

    <!-- Alerts Info/Error -->
    <g:if test='${flash.info}'>
        <div class='alert alert-info alert-dismissable'>
            ${flash.info}
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        </div>
    </g:if>
    <g:if test='${flash.message}'>
        <div class='alert alert-error alert-dismissable'>
            ${flash.message}
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        </div>
    </g:if>
    <g:if test="${user?.hasErrors()}">
        <div class='alert alert-warning alert-dismissable'>
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            <g:eachError bean="${user}">
                <li><g:message error="${it}"/></li>
            </g:eachError>
        </div>
    </g:if>

    <div class="pull-left" style="text-align: center; width: 30%; margin: 20px auto">
        <h2>${user?.username}</h2>

        <div id="categories">
            <ul>
                <g:each in="${user.categories}" var="cat">
                    <li><g:link controller="category" action="getPosts">${cat}</g:link></li>
                </g:each>
            </ul>
        </div>
    </div>

    <div class="pull-right" style="text-align: center; width: 50%; margin: 20px auto">
        <ul>
            <li>Test</li>
            <li>Test</li>
            <li>Test</li>
            <li>Test</li>
            <li>Test</li>
        </ul>
    </div>

</div> <!-- /container -->
</body>
</html>