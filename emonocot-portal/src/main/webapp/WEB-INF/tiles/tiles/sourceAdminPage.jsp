<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="content">
			<div class="page-header">
				<img src="${source.logoUrl}"/>
    		</div>
    		<div>
    		
		    <div>
		      <jsp:element name="a">
		        <jsp:attribute name="href">
		          <c:url value="/admin/source/${source.identifier}">
		            <c:param name="form">true</c:param>
		          </c:url>
		        </jsp:attribute>
		        <spring:message code="edit.group"/>
		      </jsp:element>
		    </div>
		 
			<div class="row">
			<h3 class="span12">${source.title}</h3>
			<a href="${source.uri}" class="pull-right">
				<spring:message code="availableAt" />
			</a>
			</div>
			<h5>
				<spring:message code="creator" />
			</h5>
			<p>${source.creator}</p>
			<h5>
				<spring:message code="email" />
			</h5>
			<a href="mailto:${source.creatorEmail}">${source.creatorEmail}</a>
			
			<h5>
				<spring:message code="creationDate" />
			</h5>
			<p>${source.created}</p>
			
			<h5>
				<spring:message code="description" />
			</h5>
			<p>${source.description}</p>

			<h5>
				<spring:message code="publisherName" />
			</h5>
			<p>${source.publisherName}</p>
			<h5>
				<spring:message code="email" />
			</h5>
			<a href="mailto:${source.publisherEmail}">${source.publisherEmail}</a>
			<h5>
				<spring:message code="keywords" />
			</h5>
			<c:if test="${not empty source.subject}">
		          <c:forEach var="subject" items="${em:split(source.subject,';')}" varStatus="status">
		            <c:choose>
		              <c:when test="${status.last}">
		                <span class="label">${subject}</span>
		              </c:when>
		              <c:otherwise>
		                <span class="label">${subject}</span>&#160;
		              </c:otherwise>
		            </c:choose>		          
		          </c:forEach>
		        </c:if>
			
			<h5>
				<spring:message code="reference" />
			</h5>
			<p>${source.source}</p>
		</div>
		<br/>
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
					<tbody id="jobs">
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