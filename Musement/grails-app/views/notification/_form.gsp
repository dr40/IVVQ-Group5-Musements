<%@ page import="musement.Notification" %>



<div class="fieldcontain ${hasErrors(bean: notificationInstance, field: 'user', 'error')} required">
    <label for="user">
        <g:message code="notification.user.label" default="User"/>
        <span class="required-indicator">*</span>
    </label>
    <g:select id="user" name="user.id" from="${musement.user.User.list()}" optionKey="id" required=""
              value="${notificationInstance?.user?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: notificationInstance, field: 'posts', 'error')} ">
    <label for="posts">
        <g:message code="notification.posts.label" default="Posts"/>

    </label>
    <g:select name="posts" from="${musement.Post.list()}" multiple="multiple" optionKey="id" size="5"
              value="${notificationInstance?.posts*.id}" class="many-to-many"/>
</div>

