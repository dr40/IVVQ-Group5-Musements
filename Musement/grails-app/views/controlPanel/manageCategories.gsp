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
            <g:set var="catnr" value="${cat.users.size()}" />
            <g:set var="postnr" value="${cat.posts.size()}" />
            <div class="badge-info">
                <span>${message(code: "musement.control.panel.categories.users")}</span>
                <span>${catnr}</span>
            </div>
            <div class="badge-info">
                <span>${message(code: "musement.control.panel.categories.posts")}</span>
                <span>${postnr}</span>
            </div>
        <g:link class="btn btn-danger" controller="category" action="deleteCategory" params='[categoryId: cat.id]'>${message(code: "musement.control.panel.categories.delete")}</g:link>
        </div>
    </div>
</div>