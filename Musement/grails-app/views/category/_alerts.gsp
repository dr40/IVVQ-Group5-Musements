<g:if test='${flash.info}'>
    <div class='alert alert-info alert-dismissable' style="text-align: left">
        ${flash.info}
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
    </div>
</g:if>
<g:if test='${flash.message}'>
    <div class='alert alert-error alert-dismissable' style="text-align: left">
        ${flash.message}
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
    </div>
</g:if>
<g:if test="${category?.hasErrors()}">
    <div class='alert alert-warning alert-dismissable' style="text-align: left">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <g:eachError bean="${category}">
            <li><g:message error="${it}"/></li>
        </g:eachError>
    </div>
</g:if>