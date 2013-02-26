<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:security="http://www.springframework.org/security/tags"
    xmlns:spring="http://www.springframework.org/tags"
	xmlns:tags="urn:jsptagdir:/WEB-INF/tags" version="2.0">
	<!-- Defaults to "feedbackModal" -->
	<jsp:directive.attribute name="modalId" type="java.lang.String" required="false" />
	<jsp:directive.attribute name="selector" type="java.lang.String" required="true" />
	<jsp:directive.attribute name="dataName" type="java.lang.String" required="false" />
    <c:if test="${empty modalId}" >
        <c:set var="modalId" value="feedbackModal"/>
    </c:if>
    <c:set var="clickScript" value="$('${selector}').attr('selected', true); return true;" />
<security:authorize access="hasRole('PERMISSION_ADMINISTRATE')">
    <a class="pull-right" rel="tooltip" data-placement="right" title="Comment on ${dataName}." onclick="${clickScript}"  data-toggle="modal" href="#${modalId}">
		<i class="halflings-icon comments"><!--  --></i>
	</a>
</security:authorize>
</jsp:root>
