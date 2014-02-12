<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="musement"/>
    <title>Musement | Login</title>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/Musement">Musement</a>
            <sec:ifNotLoggedIn>
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li><g:link controller="userManagement" action="register" ><g:message code="musement.user.register"/></g:link></li>
                    </ul>
                </div>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
                <div class="nav-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li><g:link controller="logout" ><g:message code="musement.logout"/></g:link></li>
                    </ul>
                </div>
            </sec:ifLoggedIn>
        </div>
    </div>
</div>

<div class="container" style="margin-top: 50px; text-align: center" >

    <h2><g:message code="musement.login"/></h2>

    <!-- Alerts Info/Error -->
    <g:render template="../userManagement/alerts" model="[user: user]"/>

    <form class="hero-unit" style="width: 300px; margin: auto" action='${postUrl}' method='POST' id='loginForm' autocomplete='off'>
        <fieldset>

            <div class="form-group">
                <input type="text" class="form-control" name='j_username'
                       id='username' placeholder='${message(code: "musement.username")}' >
            </div>

            <div class="form-group">
                <input type="password" class="form-control" name='j_password'
                       id='password' placeholder='${message(code: "musement.password")}' >
            </div>

            <div class="form-group" id="remember_me_holder" style="width: 50%; margin: auto">
                <g:if test='${hasCookie}'>
                    <label class="checkbox" ><input type="checkbox" name='${rememberMeParameter}' id='remember_me' checked="true"/>${message(code: "musement.remember")}</label>
                </g:if>
                <g:else>
                    <label class="checkbox" ><input type="checkbox" name='${rememberMeParameter}' id='remember_me' />${message(code: "musement.remember")}</label>
                </g:else>
            </div>

            <button type="submit" id="submit" class="btn btn-primary">${message(code: "springSecurity.login.button")}</button>

        </fieldset>
    </form>

</div> <!-- /container -->
<script type='text/javascript'>
    <!--
    (function() {
        document.forms['loginForm'].elements['j_username'].focus();
    })();
    // -->
</script>
</body>
</html>