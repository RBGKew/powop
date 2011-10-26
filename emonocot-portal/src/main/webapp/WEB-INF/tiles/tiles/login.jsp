<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<div class="row">
		<div class="twelvecol">
			<div class="content-wrapper">

				<div class="row">
					<div class="tencol">
						<c:if test="${param.error}">
							<p>${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
						</c:if>
						<c:url value="/process_login" var="actionUrl" />
						<form:form commandName="loginForm" action="${actionUrl}">
							<table>
								<tr>
									<td><spring:message code="username" />
									</td>
									<td><form:input path="j_username" />
									</td>
									<td><form:errors path="j_username" />
									</td>
								</tr>
								<tr>
									<td><spring:message code="password" />
									</td>
									<td><form:password path="j_password" />
									</td>
									<td><form:errors path="j_password" />
									</td>
								</tr>
								<tr>
									<td colspan="3"><jsp:element name="input">
											<jsp:attribute name="type">submit</jsp:attribute>
											<jsp:attribute name="value">
												<spring:message code="submit" />
											</jsp:attribute>
										</jsp:element></td>
								</tr>
							</table>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</jsp:root>