<%@ page import="musement.Post" %>

<div>
    <g:form role="form" controller="post" action="sendPost">
        <g:hiddenField name="categoryId" value="${categoryId}"/>
        <div style="border: 1px solid #DFDFDF;-webkit-box-shadow: 0 8px 6px -6px #555555;-moz-box-shadow: 0 8px 6px -6px #555555;box-shadow: 0 8px 6px -6px #555555;">
            <div style="background-color:white;text-align:center;padding:8px;">
                <g:textArea name="content"
                            class="form-control"
                            placeholder="Write your post content ... You can use tags like twitter (ex: #musement #cool)"
                            style="width:96%;"
                            rows="5"
                            maxlength="256"
                            id="send-post-content"
                            onkeydown="javascript:sendPostUpdateCharCountLeft()"
                            onkeyup="javascript:sendPostUpdateCharCountLeft()"
                            onchange="javascript:sendPostUpdateCharCountLeft()"></g:textArea>
            </div>
            <div style="background-color:#EAEAEA;color:#AEAEAE;text-align:right;padding:8px;font-size:10px;">
                <div style="float:left">
                    <span style="color:#A0A0A0">character left</span>
                    <span style="color:#A0A0A0;" id="send-post-char-count-left">256</span>
                </div>
                <div style="float:right">
                    <button type="submit" class="btn-primary">Post</button>
                </div>
                <div style="clear:both"></div>
            </div>
        </div>
    </g:form>
</div>

<script>
    /**
     * Refresh the character count left for the post
     */
    function sendPostUpdateCharCountLeft() {
        document.getElementById("send-post-char-count-left").innerHTML = 256 - document.getElementById("send-post-content").value.length;
    }
</script>
