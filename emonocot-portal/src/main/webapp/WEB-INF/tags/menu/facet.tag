<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/portal/functions"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.attribute name="facetName"
		type="java.lang.String" required="true" />
	<jsp:directive.attribute name="pager" type="org.emonocot.model.pager.Page"
		required="true" />
	<jsp:directive.attribute name="title" type="java.lang.String" required="false" />
	
	<jsp:directive.attribute name="showIcons" type="java.lang.Boolean" required="false" />
	
	<spring:message var="more" code="more"/>
	<spring:message var="less" code="less"/>
	<li id="${facetName}" class="nav-header">
		<c:choose>
			<c:when test="${not empty title}">
				<spring:message code="${title}" />
			</c:when>
			<c:otherwise>
				<spring:message code="${facetName}" />
			</c:otherwise>
		</c:choose>
	</li>		
	
	<c:set var="facetSelected" value="${em:isFacetSelected(pager,facetName)}"/>
	<c:choose>
		<c:when test="${facetSelected}">
			<li class="${facetName}">
					<jsp:element name="a">
						<jsp:attribute name="href">
							<c:url value="search">
								<c:forEach var="p" items="${pager.paramNames}">
									<c:param name="${p}" value="${pager.params[p]}"/>
								</c:forEach>
								<c:param name="limit" value="${pager.pageSize}" />
								<c:param name="start" value="0" />
								<c:param name="sort">${pager.sort}</c:param>
								<c:forEach var="selectedFacet" items="${pager.selectedFacetNames}">
									<c:if test="${selectedFacet != facetName}">
										<c:param name="facet" value="${selectedFacet}.${pager.selectedFacets[selectedFacet]}"/>
									</c:if>
								</c:forEach>
							</c:url>
						</jsp:attribute>
						<c:if test="${facetName eq 'CLASS'}">
							<c:set value="no-small-icon" var="cssClass"></c:set>
						</c:if>
						<div class="${cssClass}"><spring:message code="${facetName}.clearFacet" /></div>
					</jsp:element>
			</li>
						
			<li class="${facetName}">
				<a href="#">
					<c:set var="selectedFacetName" value="${pager.selectedFacets[facetName]}"/>
					<c:if test="${showIcons}">
						<spring:url var="imageUrl" value="/images/${selectedFacetName}.jpg"/>
						<img src="${imageUrl}"/>
					</c:if>
					<span class="facetValue"><spring:message code="${selectedFacetName}" /></span>
				</a>
			</li>
		</c:when>
		<c:otherwise>
			<c:set var="values" value="${pager.facets[facetName]}"/>
			<c:forEach var="facet" begin="0" end="9" step="1" items="${values}">
				<li class="${facetName}">
						<c:choose>
							<c:when test="${facet.count == 0}">
								<c:if test="${facetName eq 'CLASS'}">
									<c:set value="no-small-icon" var="cssClass"></c:set>
								</c:if>
								<div class="${cssClass}"><spring:message code="${facet.value}" /></div>
							</c:when>
							<c:otherwise>
								<jsp:element name="a">
									<jsp:attribute name="href">
										<c:url value="search">
											<c:forEach var="p" items="${pager.paramNames}">
												<c:param name="${p}" value="${pager.params[p]}"/>
											</c:forEach>
											<c:param name="limit" value="${pager.pageSize}" />
											<c:param name="start" value="0" />
											<c:param name="sort">${pager.sort}</c:param>
											<c:param name="facet" value="${facetName}.${facet.value}" />
											<c:forEach var="selectedFacet" items="${pager.selectedFacetNames}">
												<c:if test="${selectedFacet != facetName}">
													<c:param name="facet" value="${selectedFacet}.${pager.selectedFacets[selectedFacet]}"/>
												</c:if>
											</c:forEach>
										</c:url>
									</jsp:attribute>
									<c:if test="${showIcons}">
									  <spring:url var="imageUrl" value="/images/${facet.value}.jpg"/>
									  <img src="${imageUrl}"/>
								    </c:if>
									
									<span class="facetValue"><spring:message code="${facet.value}" /></span>
									<c:if test="${!em:isMultiValued(facetName)}">
									<span class="facetCount"> [${facet.count}]</span>
								</c:if>
								</jsp:element>
								
							</c:otherwise>
						</c:choose>
					
				</li>
			</c:forEach>
			<c:if test="${fn:length(values) gt 10}">
				<div id="${facetName}-collapse" class="collapse">
					<c:forEach var="facet" begin="10" items="${values}">
						<li class="${facetName}"> 
							
								<c:choose>
									<c:when test="${facet.count == 0}">
										<a href="#"><span class="facetValue"><spring:message code="${facet.value}" /></span></a>
									</c:when>
									<c:otherwise>
										<jsp:element name="a">
											<jsp:attribute name="href">
												<c:url value="search">
													<c:forEach var="p" items="${pager.paramNames}">
														<c:param name="${p}" value="${pager.params[p]}"/>
													</c:forEach>
													<c:param name="limit" value="${pager.pageSize}" />
													<c:param name="start" value="0" />
													<c:param name="sort">${pager.sort}</c:param>
													<c:param name="facet" value="${facetName}.${facet.value}" />
													<c:forEach var="selectedFacet" items="${pager.selectedFacetNames}">
														<c:if test="${selectedFacet != facetName}">
															<c:param name="facet" value="${selectedFacet}.${pager.selectedFacets[selectedFacet]}"/>
														</c:if>
													</c:forEach>
												</c:url>
											</jsp:attribute>
											<span class="facetValue"> <spring:message code="${facet.value}" /></span>
										<c:if test="${!em:isMultiValued(facetName)}">
											<span class="facetCount"> [${facet.count}]</span>
										</c:if>
										</jsp:element>
									</c:otherwise>
								</c:choose>
							
						</li>
					</c:forEach>
				</div>						
				<a id="${facetName}-collapse-link" data-toggle="collapse" data-target="#${facetName}-collapse" class="label">${fn:length(values)-10} ${more}</a>
				<script>
					$("#${facetName}-collapse").on('hidden', function () {
						$("#${facetName}-collapse-link").html("${fn:length(values)-10} ${more}");
					}).on('shown', function () {
						$("#${facetName}-collapse-link").html("${less}");
					});
				</script>
			</c:if>
		</c:otherwise>
	</c:choose>
		
		
</jsp:root>