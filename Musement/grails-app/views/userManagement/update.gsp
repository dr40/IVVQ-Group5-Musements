<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="../layouts/musement"/>
    <sec:ifLoggedIn>
        <title>Musement | ${user?.username} Update</title>
    </sec:ifLoggedIn>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/Musement">Musement</a>
            <sec:ifLoggedIn>
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li><g:link controller="userManagement" action="home" ><g:message code="musement.user.home"/></g:link></li>
                    </ul>
                </div>
            </sec:ifLoggedIn>
        </div>
    </div>
</div>

<div class="container" style="margin-top: 50px; text-align: center">

    <h2><g:message code="musement.user.update"/></h2>

    <!-- Alerts Info/Error -->
    <g:render template="alerts" model="[user: user]"/>

    <sec:ifLoggedIn>
        <g:form class="hero-unit" controller="userManagement" action="update" style="width: 300px; margin: auto">
            <fieldset>

                <div class="form-group">
                    <h3>${user.username}</h3>
                </div>
                <div class="form-group">
                    <h4>${user.email}</h4>
                </div>

                <div class="form-group">
                    <input type="password" class="form-control"
                           id="password" placeholder='${message(code: "musement.user.update.password.old")}' name="password">
                </div>

                <div class="form-group">
                    <input type="password" class="form-control"
                           id="password2" placeholder='${message(code: "musement.user.update.password.new")}' name="password2">
                </div>

                <div class="form-group">
                    <input type="password" class="form-control"
                           id="password3" placeholder='${message(code: "musement.user.register.password2")}' name="password3">
                </div>

                <div class="form-group">
                    <button type="submit" class="btn btn-primary"><g:message code="musement.user.update"/></button>
                    <button type="button" class="btn btn-warning" onclick="window.history.back()"><g:message code="musement.user.cancel"/></button>
                </div>

            </fieldset>
        </g:form>
    </sec:ifLoggedIn>

</div> <!-- /container -->
</body>
</html>