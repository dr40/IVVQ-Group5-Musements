<%@ page import="musement.Post" %>

<div>
    <g:form role="form" controller="post" action="sendPost">
        <g:hiddenField name="categoryId" value="${categoryId}"/>
        <div style="border: 1px solid #DFDFDF;-webkit-box-shadow: 0 8px 6px -6px #555555;-moz-box-shadow: 0 8px 6px -6px #555555;box-shadow: 0 8px 6px -6px #555555;">
            <div style="background-color:white;text-align:center;padding:8px;">
                <g:textArea name="content" class="form-control" placeholder="Write your post content ... You can use tags like twitter (ex: #musement #cool)" style="width:96%;" rows="5"></g:textArea>
            </div>
            <div style="background-color:#EAEAEA;color:#AEAEAE;text-align:right;padding:8px;font-size:10px;">
                <button type="submit" class="btn-primary">Post</button>
            </div>
        </div>
    </g:form>
</div>

