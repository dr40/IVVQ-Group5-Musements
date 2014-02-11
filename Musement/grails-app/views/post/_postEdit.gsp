<%@ page import="musement.Post" %>

<div class="modal fade" id="editPostDialog" tabindex="-1" role="dialog" aria-labelledby="editPostLabel" aria-hidden="true">
    <div class="modal-dialog">
        <g:form role="form" controller="post" action="editPost">
            <g:hiddenField id="edit-postId" name="postId" value="${postId}"/>
            <g:hiddenField name="categoryId" value="${categoryId}"/>
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="editPostLabel">Edit post</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <g:textArea class="form-control"
                                    style="width:96%;display:none"
                                    rows="5"
                                    id="edit-post-content"
                                    name="newContent"
                                    placeholder="Document content"
                                    maxlength="256"
                                    onkeydown="javascript:editPostUpdateCharCountLeft()"
                                    onkeyup="javascript:editPostUpdateCharCountLeft()"
                                    onchange="javascript:editPostUpdateCharCountLeft()"></g:textArea>
                    </div>
                </div>
                <div class="modal-footer">
                    <div style="float:left">
                        <span style="color:#A0A0A0">character left</span>
                        <span style="color:#A0A0A0;" id="edit-post-char-count-left">256</span>
                    </div>
                    <div style="float:right">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-lg btn-success">Save change(s)</button>
                    </div>
                    <div style="clear:both"></div>
                </div>
            </div>
        </g:form>
    </div>
</div>

<script>
    /**
     * Show post edit dialog
     * @param postId Id of the post to edit
     */
    function showPostEdit(postId) {
        document.getElementById("edit-postId").value = postId;
        document.getElementById("edit-post-content").value = document.getElementById("post-content-" + postId).innerText;
        $("#edit-post-content").show();
        $("#editPostDialog").modal("show");
        editPostUpdateCharCountLeft();
    }
    /**
     * Refresh the character count left for the post
     */
    function editPostUpdateCharCountLeft() {
        document.getElementById("edit-post-char-count-left").innerHTML = 256 - document.getElementById("edit-post-content").value.length;
    }
</script>