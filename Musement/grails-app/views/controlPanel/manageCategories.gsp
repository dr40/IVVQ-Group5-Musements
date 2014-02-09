<%@ page import="musement.Category" %>
<!-- Alerts Info/Error -->
<g:render template="../category/alerts" model="[category: Category.findById(categoryId)]"/>

<div class="pull-left" style="text-align: center; width: 27%; margin: 20px auto">
    <h1>${message(code: "musement.control.panel.categories.title")}</h1>

    <div class="well well-large">
        <div class="btn-group-vertical">
            <g:each in="${Category.findAll().sort{it.id}}" var="category">
                <g:if test="${category.id == params.getInt("categoryId")}">
                    <g:link class="btn btn-success" controller="controlPanel" action="index" params='[editMode: "category", categoryId: category.id]'>${category.name}</g:link>
                </g:if>
                <g:else>
                    <g:link class="btn" controller="controlPanel" action="index" params='[editMode: "category", categoryId: category.id]'>${category.name}</g:link>
                </g:else>
            </g:each>
        </div>
    </div>
</div>

<div class="pull-right" style="width: 67%; overflow: auto;">
    <div style="margin: 50px auto auto 50px">
        <div class="hero-unit">
            <g:set var="cat" value="${Category.findById(params.getInt('categoryId'))}" />
            <h4 class="alert alert-info">${cat.description}</h4>
            <h4>${message(code: "musement.control.panel.categories.users")}<span class="badge badge-info" >${cat.users.size()}</span></h4>
            <h4>${message(code: "musement.control.panel.categories.posts")}<span class="badge badge-info" >${cat.posts.size()}</span></h4>
            <g:link class="btn btn-danger" controller="category" action="deleteCategory" onclick="return confirm(${message(code: "musement.control.panel.categories.sure")})" params='[categoryId: cat.id]'>${message(code: "default.button.delete.label")}</g:link>
            <g:link class="btn btn-warning" url="[resource: cat, action: 'edit']" controller="category">${message(code: "default.button.update.label")}</g:link>
        </div>
    </div>
</div>