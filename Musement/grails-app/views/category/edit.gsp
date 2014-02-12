<!DOCTYPE html>
<html>
<sec:ifAllGranted roles="ROLE_ADMIN">
<head>
    <meta name="layout" content="musement"/>
    <title>Musement | ${categoryInstance?.name} Update</title>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/Musement">Musement</a>
            <div class="nav-collapse collapse">
                <ul class="nav">
                    <li><g:link controller="controlPanel" action="index" params='[editMode: "category"]' ><g:message code="musement.control.panel"/></g:link></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="container" style="margin-top: 50px; text-align: center">

    <h2><g:message code="${message(code: 'musement.category.edit', default: 'Category Edit')}"/></h2>

    <!-- Alerts Info/Error -->
    <g:render template="alerts" model="[category: categoryInstance]"/>

    <g:form class="hero-unit" url="[resource: categoryInstance, action: 'update']" controller="category" action="edit" method="PUT" style="width: 300px; margin: auto">
        <fieldset>

            <div class="form-group">
                <h3>${categoryInstance?.name}</h3>
            </div>

            <div class="form-group fieldcontain ${hasErrors(bean: categoryInstance, field: 'description', 'error')} ">
                <g:textField name="description"
                             pattern="${categoryInstance?.constraints.description.matches}"
                             required="required"
                             placeholder="Description"
                             value="${categoryInstance?.description}"/>
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary"><g:message code="${message(code: 'default.button.update.label', default: 'Update')}"/></button>
                <button type="button" class="btn btn-warning" onclick="window.history.back()"><g:message code="musement.user.cancel"/></button>
            </div>

        </fieldset>
    </g:form>

</div> <!-- /container -->

</body>
</sec:ifAllGranted>
</html>