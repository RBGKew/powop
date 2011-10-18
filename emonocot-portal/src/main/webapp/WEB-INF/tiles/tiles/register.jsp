<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<div class="content-wrapper">

		<div class="row">
			<div class="tencol">
				<form:form commandName="registrationForm">
					<table>
						<tr>
							<td><spring:message code="username"/></td>
							<td><form:input path="username"/></td>
							<td><form:errors path="username"/></td>
						</tr>
						<tr>
			               <td><spring:message code="repeatUsername"/></td>
							<td><form:input path="repeatUsername"/></td>
							<td><form:errors path="repeatUsername"/></td>
						</tr>
						<tr>
			               <td><spring:message code="password"/></td>
							<td><form:password path="password"/></td>
							<td><form:errors path="password"/></td>
						</tr>
						<tr>
			               <td><spring:message code="repeatPassword"/></td>
							<td><form:password path="repeatPassword"/></td>
							<td><form:errors path="repeatPassword"/></td>
						</tr>
						<tr>
							<td colspan="3">
							  <jsp:element name="input">
							    <jsp:attribute name="type">submit</jsp:attribute>
							    <jsp:attribute name="value"><spring:message code="submit"/></jsp:attribute>							    
							  </jsp:element>
							</td>
						</tr>
					</table>
				</form:form>
			</div>
		</div>
	</div>
</jsp:root>