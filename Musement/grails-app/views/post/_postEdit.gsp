<%@ page import="musement.Post" %>

<g:form role="form" controller="post" action="editPost">
    <g:hiddenField id="edit-postId" name="postId" value="${postId}"/>
    <g:hiddenField name="categoryId" value="${categoryId}"/>
    <div class="modal fade" id="editPostDialog" tabindex="-1" role="dialog" aria-labelledby="editPostLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="editPostLabel">Edit post</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <g:textArea class="form-control" style="width:96%;" rows="5" id="edit-post-content" name="newContent" placeholder="Document content"></g:textArea>
                    </div>
                </div>
                <div class="modal-footer">
                    <div style="text-align:right;">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-lg btn-success">Save change(s)</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</g:form>

<script>
    function showPostEdit(postId) {
        document.getElementById("edit-postId").value = postId;
        document.getElementById("edit-post-content").value = document.getElementById("post-content-" + postId).innerText;
        $("#editPostDialog").modal("show");
    }
</script>