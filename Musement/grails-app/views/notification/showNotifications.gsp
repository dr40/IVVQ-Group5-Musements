<%@ page import="musement.Notification" %>
<g:if test="${notifications.size() > 0}">
    <ul class="dropdown-menu" aria-labelledby="dropdownMenu">
        <g:each in="${notifications}" var="notif">
            <li>
                <g:if test="${notif.post_count == 1}">
                    <g:link controller="userManagement" action="home" params="[categoryId: notif.category.id, readPost: true]">${message(code: "musement.notification.single.prefix")} ${notif.sender?.username} ${message(code: "musement.notification.single.postfix")} ${notif.category.name}</g:link>
                </g:if>
                <g:else>
                    <g:link controller="userManagement" action="home" params="[categoryId: notif.category.id, readPost: true]">${message(code: "musement.notification.multiple.prefix")} ${notif.post_count} ${message(code: "musement.notification.multiple.postfix")} ${notif.category.name}</g:link>
                </g:else>
            </li>
        </g:each>
    </ul>
</g:if>
