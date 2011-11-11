<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:tags="urn:jsptagdir:/WEB-INF/tags"
	xmlns:spring="http://www.springframework.org/tags"
	version="2.0">
	<jsp:useBean id="result" scope="request"
		type="org.emonocot.model.pager.Page&lt;org.emonocot.model.taxon.Taxon&gt;" />
		
<style type="text/css">
a.thumb {
		display:block;
		/*float:left;*/
		width:100px;
		height:100px;
		line-height:100px;
		overflow:hidden;
		position:relative;
		z-index:1;		
	}
	a.thumb  img{
		/*float:left;*/
		position:absolute;
		top:-20px;
		left:-50px;	
	}</style>		
		
  <div class="content">
	<div class="page-header">
		  <h2 id="page-title"><spring:message code="search.emonocot"/></h2>
	</div>
	<div class="row">
	  <form id="search.form" accept-charset="UTF-8" method="GET" action="search">
	    <jsp:element name="input">
	      <jsp:attribute name="class">xxlarge</jsp:attribute>
		  <jsp:attribute name="placeholder">search</jsp:attribute>
		  <jsp:attribute name="value">${result.params['query']}</jsp:attribute>
		  <jsp:attribute name="type">text</jsp:attribute>
		  <jsp:attribute name="name">query</jsp:attribute>
		</jsp:element>
		<input type="hidden" name="limit" value="${result.pageSize}" />
		<input type="hidden" name="start" value="0" />
		<c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
		 <jsp:element name="input">
		   <jsp:attribute name="type">hidden</jsp:attribute>
		   <jsp:attribute name="name">facet</jsp:attribute>
		   <jsp:attribute name="value">
			 <jsp:scriptlet>
			   String selectedFacet = (String) pageContext.getAttribute("selectedFacet");
               out.print(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
             </jsp:scriptlet>
		   </jsp:attribute>
		  </jsp:element>
		</c:forEach>
		<jsp:element name="input">
		  <jsp:attribute name="type">hidden</jsp:attribute>
		  <jsp:attribute name="name">sort</jsp:attribute>
		  <jsp:attribute name="value">${result.sort}</jsp:attribute>
		</jsp:element>
		<input type="submit" value="go" name="submit" class="btn primary" />
	  </form>
	</div>
	<div class="row">		
	  <div class="pagination">
	    <tags:pagination pager="${result}" url="search"/>
	  </div>
	</div>
	<div class="row">
	  <div id="pages" class="span8">				
		<tags:results pager="${result}"/>
      </div>
	</div>
	<div class="row">
		<div class="span4">
			<ul id="facets">
				<c:forEach var="facetName" items="${result.facetNames}">
					<li id="${facetName}">
						<h2>
							<spring:message code="${facetName}" />
						</h2>
						<ul class="facet">
							<jsp:scriptlet>
							  String facetName = (String) pageContext.getAttribute("facetName");
                              pageContext.setAttribute("facetSelected", new Boolean(result.isFacetSelected(facetName)));</jsp:scriptlet>
							<c:choose>
								<c:when test="${facetSelected}">
									<li><jsp:element name="a">
											<jsp:attribute name="href">
												<c:url value="search">
													<c:param name="query" value="${result.params['query']}" />
													<c:param name="limit" value="${result.pageSize}" />
													<c:param name="start" value="0" />
													<c:param name="sort">${result.sort}</c:param>
													<c:forEach var="selectedFacet"
														items="${result.selectedFacetNames}">
														<c:if test="${selectedFacet != facetName}">
															<c:param name="facet">
																<jsp:scriptlet>
																  String selectedFacet = (String) pageContext.getAttribute("selectedFacet");
                                                                  out.println(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
                                                                </jsp:scriptlet>
															</c:param>
														</c:if>
													</c:forEach>
												</c:url>
											</jsp:attribute>
											<spring:message code="${facetName}.clearFacet" />
										</jsp:element></li>
									<li>
										<jsp:scriptlet>
										  try {
                                              pageContext.setAttribute("selectedFacetName", result.getSelectedFacets().get(facetName));
                                          } catch (Exception e) {
                                              pageContext.setAttribute("selectedFacetName", "Unable to display name of selected option");
                                          }
                                        </jsp:scriptlet>
                                        <spring:message code="${selectedFacetName}" />
									</li>
								</c:when>
								<c:otherwise>
									<c:forEach var="facet" items="${result.facets[facetName]}">
										<li><c:choose>
												<c:when test="${facet.count == 0}">
													<spring:message code="${facet.value}" />
												</c:when>
												<c:otherwise>
													<jsp:element name="a">
														<jsp:attribute name="href">
															<c:url value="search">
																<c:param name="query" value="${result.params['query']}" />
																<c:param name="limit" value="${result.pageSize}" />
																<c:param name="start" value="0" />
																<c:param name="sort">${result.sort}</c:param>
																<c:param name="facet"
																	value="${facetName}.${facet.value}" />
																<c:forEach var="selectedFacet"
																	items="${result.selectedFacetNames}">
																	<c:if test="${selectedFacet != facetName}">
																		<c:param name="facet">
																			<jsp:scriptlet>
																			     String selectedFacet = (String) pageContext.getAttribute("selectedFacet");
                                                                                 out.println(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
                                                                            </jsp:scriptlet>
																		</c:param>
																	</c:if>
																</c:forEach>
															</c:url>
														</jsp:attribute>
														<spring:message code="${facet.value}" />
													</jsp:element>
												</c:otherwise>
											</c:choose>
										</li>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</ul>
					</li>
				</c:forEach>
				<li id="sort">
					<h2>
						<spring:message code="sort" />
					</h2>
					<ul class="facet">
						<c:forEach var="sortItem" items="${em:sortItems()}">
							<li>
								<jsp:scriptlet>
								  org.emonocot.api.Sorting sortItem = (org.emonocot.api.Sorting) pageContext.getAttribute("sortItem");
                                  pageContext.setAttribute("equalsSortItem", new Boolean(sortItem.equals(result.getSort())));</jsp:scriptlet>
								<c:choose>
									<c:when test="${equalsSortItem}">
										<spring:message code="${sortItem}" />
									</c:when>
									<c:otherwise>
										<jsp:element name="a">
											<jsp:attribute name="href">
												<c:url value="search">
													<c:param name="query" value="${result.params['query']}" />
													<c:param name="limit" value="${result.pageSize}" />
													<c:param name="start" value="0" />
													<c:forEach var="selectedFacet"
														items="${result.selectedFacetNames}">
														<c:param name="facet">
															<jsp:scriptlet>
															  String selectedFacet = (String) pageContext.getAttribute("selectedFacet");
                                                              out.println(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));</jsp:scriptlet>
														</c:param>
													</c:forEach>
													<c:param name="sort">${sortItem}</c:param>
												</c:url>
											</jsp:attribute>
											<spring:message code="${sortItem}" />
										</jsp:element>
									</c:otherwise>
								</c:choose>
							</li>
						</c:forEach>
					</ul>
				</li>
			</ul>
		</div>
		<div class="span12">
			<div id="results"  style="border-left: 1px solid #eee;">
				<c:forEach var="item" items="${result.records}">
					<div class="well">
							<c:choose>
								<c:when test="${item.className == 'Taxon'}">
								<div class="row" style="margin-left:0px;">
									<a href="taxon/${item.identifier}">${item.name}</a>
									<!-- <p class="thumb"><a><img src="${images.image.url}" title="${images.image.caption}"/></a></p> -->
									</div>
								</c:when>
								<c:when test="${item.className == 'Image'}">
									<!-- <a href="image/${em:encodePathSegment(item.identifier)}">${item.caption}</a> -->
									<div class="row" style="margin-left:0px;">
										<a href="image/${item.identifier}">${item.caption}</a>
										<a class="thumb" style="float:right"><img src="${item.url}" title="${item.caption}"/></a>
									</div>
								</c:when>
								<c:otherwise>
									Unknown class ${item.className}
								</c:otherwise>
								
							</c:choose>
						</div>	
					
					</c:forEach>
				
				
				</div>
			</div>
	</div>
  </div>
</jsp:root>