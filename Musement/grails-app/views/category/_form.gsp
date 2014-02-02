<%@ page import="musement.Category" %>



<div class="fieldcontain ${hasErrors(bean: categoryInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="category.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" pattern="${categoryInstance.constraints.name.matches}" required="" value="${categoryInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: categoryInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="category.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${categoryInstance?.description}"/>
</div>

%{--<div class="fieldcontain ${hasErrors(bean: categoryInstance, field: 'posts', 'error')} ">
	<label for="posts">
		<g:message code="category.posts.label" default="Posts" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${categoryInstance?.posts?}" var="p">
    <li><g:link controller="post" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="post" action="create" params="['category.id': categoryInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'post.label', default: 'Post')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: categoryInstance, field: 'users', 'error')} ">
	<label for="users">
		<g:message code="category.users.label" default="Users" />
		
	</label>
	
</div>--}%

