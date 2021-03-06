<%@ page import="musement.Post" %>

<g:form role="form" controller="post" action="deletePost">
    <g:hiddenField id="delete-postId" name="postId" value="${postId}"/>
    <g:hiddenField name="categoryId" value="${categoryId}"/>
    <div class="modal fade" id="removePostConfirm" tabindex="-1" role="dialog" aria-labelledby="deletePostLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="deletePostLabel">Delete post</h4>
                </div>
                <div class="modal-body">
                    Do you really want to delete the post?
                </div>
                <div class="modal-footer">
                    <div style="text-align:right;">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-lg btn-danger">Delete</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</g:form>

<script>
    /**
     * Show delete post Alert
     * @param postId Post id to delete
     */
    function showPostDeleteAlert(postId) {
        document.getElementById("delete-postId").value = postId;
        $("#removePostConfirm").modal("show");
    }
</script>