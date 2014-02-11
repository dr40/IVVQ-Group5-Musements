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
    <h3>${user?.username}</h3>

    <sec:ifLoggedIn>
        <g:link class="btn btn-danger btn-large" controller="userManagement" action="unregister" onclick="return confirm(${message(code: "musement.user.delete.account")})" ><g:message code="musement.user.unregister"/></g:link>
    </sec:ifLoggedIn>


    <!-- Alerts Info/Error -->
    <g:render template="alerts" model="[user: user]"/>

    <sec:ifLoggedIn >
        <div class="container" style="margin: 20px auto">
        <div class="hero-unit pull-left" style="width: 300px; margin: auto">
            <g:form controller="userManagement" action="updatePassword" >
                <fieldset>
                    <div class="form-group">
                        <input type="password" class="form-control" id="password" name="password"
                               required="required"
                               placeholder='${message(code: "musement.user.update.password.old")}' >
                    </div>

                    <div class="form-group">
                        <input type="password" class="form-control" id="password2" name="password2"
                               pattern="^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$"
                               required="required"
                               oninvalid="setCustomValidity(${message(code: "musement.user.password.strenght")})"
                               oninput="setCustomValidity('')"
                               placeholder='${message(code: "musement.user.update.password.new")}' >
                    </div>

                    <div class="form-group">
                        <input type="password" class="form-control" id="password3" name="password3"
                               pattern="^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$"
                               required="required"
                               oninvalid="setCustomValidity(${message(code: "musement.user.password.strenght")})"
                               oninput="setCustomValidity('')"
                               placeholder='${message(code: "musement.user.register.password2")}' >
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn btn-primary"><g:message code="musement.user.update.change.password"/></button>
                        <button type="button" class="btn btn-warning" onclick="window.history.back()"><g:message code="musement.user.cancel"/></button>
                    </div>

                </fieldset>
            </g:form>
        </div>
        <div class="hero-unit pull-right" style="width: 300px; margin: auto">
            <g:form controller="userManagement" action="updateEmail" >
                <fieldset>

                    <div class="form-group fieldcontain ${hasErrors(bean: user, field: 'email', 'error')} required">
                        <input type="email" class="form-control" id="email" name="email"
                               placeholder='${message(code: "musement.user.register.email")}'
                               required="required"
                               oninvalid="this.setCustomValidity(${message(code: 'musement.user.register.email.match')})"
                               oninput="setCustomValidity('')"
                               value="${fieldValue(bean: user, field: 'email')}">
                    </div>

                    <div class="form-group">
                        <input type="password" class="form-control" id="password4" name="password4"
                               required="required"
                               placeholder='${message(code: "musement.user.update.password.old")}' >
                    </div>


                    <div class="form-group">
                        <button type="submit" class="btn btn-primary"><g:message code="musement.user.update.change.email"/></button>
                        <button type="button" class="btn btn-warning" onclick="window.history.back()"><g:message code="musement.user.cancel"/></button>
                    </div>

                </fieldset>
            </g:form>
        </div>
        </div>
    </sec:ifLoggedIn>

</div> <!-- /container -->
</body>
</html>