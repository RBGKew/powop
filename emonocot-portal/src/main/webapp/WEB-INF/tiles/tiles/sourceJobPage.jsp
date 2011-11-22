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
		    <h5><spring:message code="job.number" arguments="${job.jobId}"/></h5>
			<table class="zebra-striped">
				<thead>
					<tr>
						<th><spring:message code="jobExecution.startTime" />
						</th>
						<th><spring:message code="jobExecution.duration" />
						</th>
						<th><spring:message code="jobExecution.status" />
						</th>
						<th><spring:message code="jobExecution.exitStatus.exitCode" />
						</th>
						<th><spring:message
								code="jobExecution.exitStatus.exitDescription" />
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${em:formatDate(job.startTime)}</td>
						<td>${em:formatPeriod(job.startTime,job.endTime)}</td>
						<td>${job.status}</td>
						<td>${job.exitStatus.exitCode}</td>
						<td>${job.exitStatus.exitDescription}</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="row">
		    <h5><spring:message code="content.harvested"/></h5>
			<table class="zebra-striped">
				<thead>
					<th><spring:message code="record.type" /></th>
					<th><spring:message code="records.harvested" /></th>
				</thead>
				<tbody id="results">
					<c:forEach var="result" items="${results}">
						<tr>
							<td><c:choose>
									<c:when test="${not empty result.value}">
										<jsp:element name="a">
											<jsp:attribute name="href">
												<c:url
													value="/admin/source/${source.identifier}/jobs/${job.jobId}">
													<c:param name="recordType">${result.label}</c:param>
												</c:url>
											</jsp:attribute>
											<spring:message code="${result.label}" />
										</jsp:element>
									</c:when>
									<c:otherwise>
										<spring:message code="${result.label}" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>${result.value}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</jsp:root>