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

    %{--<h2><g:message code="${message(code: 'category.label', default: 'Category')}"/></h2>--}%


    <h2><g:message code="Add Category"/></h2>
    <sec:ifLoggedIn>
        <g:form class="hero-unit" url="[resource: categoryInstance, action: 'save']" controller="category" action="create" style="width: 300px; margin: auto">
            <fieldset>

                <div id="create-category" class="content scaffold-create" role="main">
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${categoryInstance}">
                        <ul class="errors" role="alert">
                            <g:eachError bean="${categoryInstance}" var="error">
                                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                                        error="${error}"/></li>
                            </g:eachError>
                        </ul>
                    </g:hasErrors>
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


%{--
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'category.label', default: 'Category')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<a href="#create-category" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                 default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="create-category" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${categoryInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${categoryInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form url="[resource: categoryInstance, action: 'save']">
        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
--}%
