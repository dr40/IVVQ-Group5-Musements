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
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/Musement">Musement</a>
            <sec:ifLoggedIn>
                <div class="nav-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li class="dropdown">
                            <g:link class="dropdown-toggle" data-toggle="dropdown">
                                <!-- Include the view necessary for showing the number of notification -->
                                <g:include controller="notification" action="notificationsNumber" />
                                <g:message code="musement.home.notifications"/>
                            </g:link>
                            <g:include controller="notification" action="showNotifications" />
                        </li>
                        <li><g:link controller="userManagement" action="update" ><g:message code="musement.user.update"/></g:link></li>
                        <li><g:link controller="logout" ><g:message code="musement.logout"/></g:link></li>
                    </ul>
                </div>
            </sec:ifLoggedIn>
        </div>
    </div>
</div>

<div class="container" style="margin-top: 50px">

    <!-- Alerts Info/Error -->
    <g:render template="../userManagement/alerts" model="[user: user]"/>

    <div class="pull-left" style="text-align: center; width: 30%; margin: 20px auto">
        <h1>${user?.username}</h1>
        <h3>${user?.email}</h3>

        <div class="well well-small">
            <ul class="btn-group-vertical">
                <g:each in="${user.categories}" var="category">
                    <li class="btn" ><g:link controller="posts" action="getPosts" params="[currentCategory: category]">${category.name}</g:link></li>
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