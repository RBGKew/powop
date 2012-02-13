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
						<li id="${facetName}">
							<h3>
								<spring:message code="${facetName}" />
							</h3>
							
							<ul class="facet">
							    <c:set var="facetSelected" value="${em:isFacetSelected(pager,facetName)}"/>
								<c:choose>
									<c:when test="${facetSelected}">
										<li>
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
												<spring:message code="${facetName}.clearFacet" />
											</jsp:element>
										</li>
										
										<li>
										  <c:set var="selectedFacetName" value="${pager.selectedFacets[facetName]}"/>
											<spring:message code="${selectedFacetName}" />
										</li>
									
									</c:when>
									<c:otherwise>
										<!--<c:forEach var="facet" begin="0" end="9" step="1" items="${pager.facets[facetName]}">-->
										<c:forEach var="facet" items="${pager.facets[facetName]}">
											<li>
												<c:choose>
													<c:when test="${facet.count == 0}">
														<spring:message code="${facet.value}" />
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
															<spring:message code="${facet.value}" />
														</jsp:element>
														<c:if test="${!em:isMultiValued(facetName)}">
														<p style="display:inline"> [${facet.count}]</p>
														</c:if>
													</c:otherwise>
												</c:choose>
											</li>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</ul>
						</li>
	
	</jsp:root>