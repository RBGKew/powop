<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="content">
			<div class="page-header">
				<h2 id="page-title">${source.identifier}</h2>
    		</div>
    		<div class="row">
				<br/>
				<a href="${source.uri}">
					<spring:message code="availableAt" />
				</a>
		    </div>
		    <div class="row">
				<h5><spring:message code="recent.jobs"/></h5>
				<table class="zebra-striped">
				  <thead>				    
				    <tr>
				      <th>
				        <spring:message code="jobExecution.jobId"/>
				      </th>
				      <th>
				        <spring:message code="jobExecution.startTime"/>
				      </th>
				      <th>
				        <spring:message code="jobExecution.duration"/>
				      </th>
				      <th>
				        <spring:message code="jobExecution.status"/>
				      </th>
				      <th>
				        <spring:message code="jobExecution.exitStatus.exitCode"/>
				      </th>
				      <th>
				        <spring:message code="jobExecution.exitStatus.exitDescription"/>
				      </th>
				    </tr>
				  </thead>
					<tbody>
						<c:forEach var="jobExecution" items="${jobExecutions}">
							<tr>
								<td>
								  <jsp:element name="a">
										<jsp:attribute name="href">
											<c:url
												value="/admin/source/${source.identifier}/jobs/${jobExecution.jobId}" />
										</jsp:attribute>
				                        ${jobExecution.jobId}
				                   </jsp:element>
				                </td>
								<td>${em:formatDate(jobExecution.startTime)}</td>
								<td>${em:formatPeriod(jobExecution.startTime,jobExecution.endTime)}</td>
								<td>${jobExecution.status}</td>
								<td>${jobExecution.exitStatus.exitCode}</td>
								<td>${jobExecution.exitStatus.exitDescription}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
	</div>
</jsp:root>