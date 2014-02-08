<!DOCTYPE html>
<html>
<sec:ifAllGranted roles="ROLE_ADMIN">
<head>
    <meta name="layout" content="../layouts/musement"/>
    <title>Musement | ${user?.username} - Control Panel</title>
    <!-- Init current Category -->
    <g:if test='${params.containsKey("editMode") == false}'>
        ${params.put("editMode", "category")}
    </g:if>
    <g:if test='${params.containsKey("categoryId") == false}'>
        ${params.categoryId = 1}
    </g:if>
    <g:if test='${params.containsKey("userId") == false}'>
        ${params.userId = 1}
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
            </button>
            <a class="brand" href="/Musement">Musement</a>
            <div class="nav-collapse collapse">
                <ul class="nav navbar-nav">
                    <g:if test="${params.get("editMode").equals("category")}">
                        <li class="active"><g:link controller="controlPanel" action="index" params='[editMode: "category"]'><g:message code="musement.control.panel.categories"/></g:link></li>
                        <li><g:link controller="controlPanel" action="index" params='[editMode: "user"]'><g:message code="musement.control.panel.users"/></g:link></li>
                    </g:if>
                    <g:else>
                        <li><g:link controller="controlPanel" action="index" params='[editMode: "category"]'><g:message code="musement.control.panel.categories"/></g:link></li>
                        <li class="active"><g:link controller="controlPanel" action="index" params='[editMode: "user"]'><g:message code="musement.control.panel.users"/></g:link></li>
                    </g:else>
                    <li><g:link controller="logout" ><g:message code="musement.logout"/></g:link></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="container" style="margin-top: 40px;">
    
    <g:if test="${params.get("editMode").equals("category")}">
        <g:include controller="controlPanel" action="manageCategories" params='[categoryId: params.categoryId]' />
    </g:if>
    <g:else>
        <g:include controller="controlPanel" action="manageUsers" params='[userId: params.userId]' />
    </g:else>


</div> <!-- /container -->
</body>
</sec:ifAllGranted>
</html>