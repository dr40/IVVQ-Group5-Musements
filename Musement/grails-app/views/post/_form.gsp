<%@ page import="musement.Post" %>



<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'sender', 'error')} required">
    <label for="sender">
        <g:message code="post.sender.label" default="Sender"/>
        <span class="required-indicator">*</span>
    </label>
    <g:select id="sender" name="sender.id" from="${musement.user.User.list()}" optionKey="id" required=""
              value="${postInstance?.sender?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'category', 'error')} required">
    <label for="category">
        <g:message code="post.category.label" default="Category"/>
        <span class="required-indicator">*</span>
    </label>
    <g:select id="category" name="category.id" from="${musement.Category.list()}" optionKey="id" required=""
              value="${postInstance?.category?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'content', 'error')} required">
    <label for="content">
        <g:message code="post.content.label" default="Content"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="content" required="" value="${postInstance?.content}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'postDate', 'error')} required">
    <label for="postDate">
        <g:message code="post.postDate.label" default="Post Date"/>
        <span class="required-indicator">*</span>
    </label>
    <g:datePicker name="postDate" precision="day" value="${postInstance?.postDate}"/>
</div>

