<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="../layouts/musement"/>
    <title>Musement | Update</title>
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

    <h2><g:message code="${message(code: 'musement.category.add', default: 'Category')}"/></h2>

<!-- Alerts Info/Error -->
    <g:render template="alerts" model="[category: categoryInstance]"/>

    <sec:ifLoggedIn>
        <g:form class="hero-unit" url="[resource: categoryInstance, action: 'save']" controller="category" action="create" style="width: 300px; margin: auto">
            <fieldset>

                <div id="create-category" class="content scaffold-create" role="main">
                    <g:form url="[resource: categoryInstance, action: 'save']">
                        <fieldset class="form">
                            <g:render template="form"/>
                        </fieldset>
                    </g:form>
                </div>



                <div class="form-group">
                    <button type="submit" class="btn btn-primary"><g:message code="${message(code: 'default.button.create.label', default: 'Create')}"/></button>
                    <button type="button" class="btn btn-warning" onclick="window.history.back()"><g:message code="musement.user.cancel"/></button>
                </div>

            </fieldset>
        </g:form>
    </sec:ifLoggedIn>

</div> <!-- /container -->

</body>
</html>
