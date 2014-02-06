<%@ page import="musement.Post" %>

<div style="padding:8px;background-color:#DFDFDF">
    <div style="border: 1px solid #CFCFCF;">
        <div style="background-color:white;padding:8px;">
            <%=post.content.encodeAsHTML().replaceAll("\n", "<br>")%>
        </div>
        <div style="background-color:#EAEAEA;color:#AEAEAE;text-align:right;padding:8px;font-size:10px;">
            <g:set var="postDate"><g:formatDate date="${post.postDate}"/></g:set>
            <g:message code="musement.post._post.Footer" args="[post.sender.username, postDate]"/>
        </div>
    </div>
</div>

