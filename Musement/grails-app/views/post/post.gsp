<%--
  Created by IntelliJ IDEA.
  User: Dorian
  Date: 06/02/14
  Time: 17:38
--%>

<%@ page import="musement.Post" %>

<%
    def postContent = post.content.encodeAsHTML().replaceAll("\n", "<br>");

%>

<div style="padding:8px;" id="post-${post.id}">
<g:if test="${isNewPost}">
    <div style="border: 1px solid #CFCFCF;-webkit-box-shadow: 0px 0px 10px 2px #FFCC00;-moz-box-shadow: 0px 0px 10px 2px #FFCC00;box-shadow: 0px 0px 10px 2px #FFCC00;">
</g:if>
<g:else>
    <div style="border: 1px solid #CFCFCF;-webkit-box-shadow: 0 8px 6px -6px #C0C0C0;-moz-box-shadow: 0 8px 6px -6px #C0C0C0;box-shadow: 0 8px 6px -6px #C0C0C0;">
</g:else>
        <div style="background-color:white;padding:8px;" id="post-content-${post.id}">
            <g:message code="musement.post._post.content" args="[postContent]" encodeAs="None" />
        </div>
        <script>
        <%
              print "post_prepare(" + post.id + ");"
        %>
        </script>
        <div style="background-color:#EAEAEA;color:#AEAEAE;text-align:right;padding:8px;font-size:10px;">
            <div style="float:left;">
                <g:if test="${deletable}">
                    <button class='btn btn-danger' onClick="showPostDeleteAlert(${post.id})">Delete</button>
                    <button class='btn btn-info' onClick="showPostEdit(${post.id})">Edit</button>
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
