<%--
  Created by IntelliJ IDEA.
  User: Dorian
  Date: 06/02/14
  Time: 17:38
--%>

<%@ page import="musement.Post" %>

<div style="padding:8px;background-color:#DFDFDF">
    <div style="border: 1px solid #CFCFCF;">
        <div style="background-color:white;padding:8px;">
            <%
                def postContent = post.content.encodeAsHTML().replaceAll("\n", "<br>");
            %>
            <g:message code="musement.post._post.content" args="[postContent]" encodeAs="None" />
        </div>
        <div style="background-color:#EAEAEA;color:#AEAEAE;text-align:right;padding:8px;font-size:10px;">
            <div style="float:left;">
                <g:if test="${deletable}">
                    <button class='btn btn-danger' onClick="showPostDeleteAlert(${post.id})">Delete</button>
                </g:if>
            </div>
            <div style="float:right;">
                <g:set var="postDate"><g:formatDate date="${post.postDate}"/></g:set>
                <g:message code="musement.post._post.Footer" args="[post.sender.username, postDate]"/>
            </div>
            <div style="clear:both"></div>
        </div>
    </div>
</div>
