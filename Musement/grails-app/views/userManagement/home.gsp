<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="../layouts/musement"/>
    <sec:ifLoggedIn>
        <title>Musement | ${user?.username} - Home</title>
    </sec:ifLoggedIn>
    <!-- Init current Category -->
    <g:if test='${params.containsKey("categoryId") == false}'>
        ${params.put("categoryId", 1)}
    </g:if>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
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
                        <sec:ifAllGranted roles="ROLE_ADMIN">
                            <li><g:link controller="controlPanel" action="index" ><g:message code="musement.control.panel"/></g:link></li>
                        </sec:ifAllGranted>
                        <li><g:link controller="userManagement" action="update" ><g:message code="musement.user.update"/></g:link></li>
                        <li><g:link controller="logout" ><g:message code="musement.logout"/></g:link></li>
                    </ul>
                </div>
            </sec:ifLoggedIn>
        </div>
    </div>
</div>

<div class="container" style="margin-top: 40px;">

    <!-- Alerts Info/Error -->
    <g:render template="../userManagement/alerts" model="[user: user]"/>

    <div class="pull-left" style="text-align: center; width: 27%; margin: 20px auto; overflow: auto;">
        <h1>${user?.username}</h1>
        <h3>${user?.email}</h3>

        <div class="well well-large">
            <div class="btn-group-vertical">
                <sec:ifAllGranted roles="ROLE_ADMIN">
                    <g:each in="${musement.Category.findAll().sort{it.id}}" var="category">
                        <g:if test="${category.id == params.getInt("categoryId")}">
                            <g:link class="btn btn-success" controller="userManagement" action="home" params="[categoryId: category.id]">${category.name}</g:link>
                        </g:if>
                        <g:else>
                            <g:link class="btn" controller="userManagement" action="home" params="[categoryId: category.id]">${category.name}</g:link>
                        </g:else>
                    </g:each>
                    <g:link class="btn btn-primary" controller="category" action="create">${message(code: "musement.category.add")}</g:link>
                </sec:ifAllGranted>
                <sec:ifAllGranted roles="ROLE_USER">
                    <g:each in="${user.categories.sort{a,b-> a.id.compareTo(b.id)}}" var="category">
                        <g:if test="${category.id == params.getInt("categoryId")}">
                            <g:link class="btn btn-success" controller="userManagement" action="home" params="[categoryId: category.id]">${category.name}</g:link>
                        </g:if>
                        <g:else>
                            <g:link class="btn" controller="userManagement" action="home" params="[categoryId: category.id]">${category.name}</g:link>
                        </g:else>
                    </g:each>
                    <g:link class="btn btn-primary" controller="category" action="subscribe">${message(code: "musement.category.subscribe")}</g:link>
                </sec:ifAllGranted>
            </div>
        </div>
    </div>

    <div class="pull-right" style="width: 67%; overflow: auto;">
        <div style="margin: 20px auto auto 20px">
            <g:include controller="post" action="getPosts" params='[categoryId: categoryId]' />
        </div>
    </div>

</div> <!-- /container -->
</body>
</html>