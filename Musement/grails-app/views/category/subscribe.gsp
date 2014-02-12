<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="../layouts/musement"/>
    <sec:ifLoggedIn>
        <title>Musement | ${user?.username} Subscribe</title>
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

    <h2><g:message code="musement.category.subscribe"/></h2>


    <sec:ifLoggedIn>
        <g:form class="hero-unit" controller="category" action="doSubscribe" style="width: 300px; margin: auto">
            <fieldset>

                <div class="form-group">
                    <g:each var="category" in="${musement.Category.findAll().sort { it.id }}">
                        <g:if test="${category.name.equals("Musement")}">
                            <label><g:checkBox class="form-control" name="categories" checked="true" disabled="true" value="${category.name}" />${category.name}</label>
                        </g:if>
                        <g:elseif test="${user?.categories?.contains(category)}">
                            <label><g:checkBox class="form-control" name="categories" checked="true" value="${category.name}" />${category.name}</label>
                        </g:elseif>
                        <g:else>
                            <label><g:checkBox class="form-control" name="categories" checked="false" value="${category.name}" />${category.name}</label>
                        </g:else>
                    </g:each>
                </div>

                <div class="form-group" >
                    <button type="submit" class="btn btn-primary"><g:message code="musement.category.subscribe"/></button>
                    <button type="button" class="btn btn-warning" onclick="window.history.back()"><g:message code="musement.user.cancel"/></button>
                </div>

            </fieldset>
        </g:form>
    </sec:ifLoggedIn>

</div> <!-- /container -->
</body>
</html>