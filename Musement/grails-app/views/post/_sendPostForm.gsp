<%@ page import="musement.Post" %>

<div>
    <g:form role="form" controller="post" action="sendPost">
        <g:hiddenField name="categoryId" value="${categoryId}"/>
        <div style="border: 1px solid #DFDFDF;">
            <div style="background-color:white;padding:8px;">
                <g:textArea name="content" class="form-control" placeholder="Post content"></g:textArea>
            </div>
            <div style="background-color:#EAEAEA;color:#AEAEAE;text-align:right;padding:8px;font-size:10px;">
                <button type="submit" class="btn-primary">Post</button>
            </div>
        </div>
    </g:form>
</div>

