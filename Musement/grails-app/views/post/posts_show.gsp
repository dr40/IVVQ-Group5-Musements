<%--
  Created by IntelliJ IDEA.
  User: Dorian
  Date: 04/02/14
  Time: 21:47
--%>

<%@ page contentType="text/html;charset=UTF-8" %>

<script>

    /** List of posts showed in this page(s), used for optimizations purpose **/
    var postIds = new Array();

    /**
     * Change the search tags field
     * @param tags Tags to set into the search tags field
     */
    function post_setSearchTags(tags) {
        document.getElementById("searchTags").value = tags;
        post_searchTags();
    }
    /**
     * Show/Hide post follow tags
     * @param tags List of tags separated by any characters which are not letter or number
     */
    function post_searchTags() {
        /* Seek tags to search */
        var tags = document.getElementById("searchTags").value;
        var tagsToSearch = tags.split(/[\s,]+/);
        var maxTagsToSearch = tagsToSearch.length
        for(var i = 0; i < maxTagsToSearch; i++) {
            if (tagsToSearch[i][0] != '#') {
                tagsToSearch[i] = "#" + tagsToSearch[i].toLowerCase();
            } else {
                tagsToSearch[i] = tagsToSearch[i].toLowerCase();
            }
        }
        /* Show only post which have one of a tag */
        for(var i = 0, max = postIds.length; i < max; i++) {
            var showPost = false;
            var postObj = document.getElementById("post-" + postIds[i]);
            if (maxTagsToSearch == 0) {
                showPost = true;
            } else {
                for (var j = 0; j < maxTagsToSearch; j++) {
                    if (postObj.innerHTML.toLowerCase().indexOf(tagsToSearch[j]) != -1) {
                        showPost = true;
                        break;
                    }
                }
            }
            if (showPost) {
                $("#post-" + postIds[i]).show("slow");
            } else {
                $("#post-" + postIds[i]).hide("fast");
            }
        }
    }

    /**
     * Change content of a post by transforming URL into a href HTML markup
     * @param postId Post id desired
     */
    function post_prepare(postId) {
        var obj = document.getElementById("post-content-" + postId);
        if (obj) {
            postIds.push(postId);
            var content = obj.innerHTML;
            /* Replace URL */
            content = content.replace(
                    /((https?\:\/\/)|(www\.))(\S+)(\w{2,4})(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/gi,
                    function(url){
                        var full_url = url;
                        if (!full_url.match('^https?:\/\/')) {
                            full_url = 'http://' + full_url;
                        }
                        return '<a href="' + full_url + '" target="_blank">' + url + '</a>';
                    }
            );
            /* Replace #TAGS */
            content = content.replace(
                    /#\S+/gi,
                    function(tag) {
                        return '<a href="javascript:post_setSearchTags(\'' + tag.substr(1) + '\');"><b>' + tag + '</b></a>';
                    }
            );
            /* Finalize */
            obj.innerHTML = content;
        }
    }

</script>

<g:render template="sendPostForm" model="[categoryId:categoryId]" />

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="#">Tags</a>
        <div class="navbar-search" style="width:83%;">
            <input id="searchTags" class="search-query" style="width:100%;font-size:16px;" onKeyUp="post_searchTags()" type="text" placeholder="Search tags, example: good, #like test" />
        </div>
    </div>
</div>

<g:each in="${posts}" var="post">
    <g:include controller="post" action="renderAPost" params="[categoryId:categoryId, postId: post.id]"/>
</g:each>
<div style="margin-bottom: 80px;"></div>

<g:render template="postDeleteAlert" model="[categoryId:categoryId]" />
<g:render template="postEdit" model="[categoryId:categoryId]" />
