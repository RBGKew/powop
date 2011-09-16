<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  version="2.0">
  <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
  <jsp:output omit-xml-declaration="true" />
  <jsp:output doctype-root-element="HTML" doctype-system="about:legacy-compat"/>
  <jsp:useBean id="result" scope="request" type="org.emonocot.model.pager.Page&lt;org.emonocot.model.taxon.Taxon&gt;" />
  <html class="no-js" lang="en" dir="ltr">
    <jsp:include page="/WEB-INF/jsp/head.jsp"/>
    <body>
      <div class="container">
        <jsp:include page="/WEB-INF/jsp/header.jsp"/>
        <div class="row">
        <div class="twelvecol">
          <div id="search">
           <form id="search.form" accept-charset="UTF-8" method="GET" action="search">
            <jsp:element name="input">
              <jsp:attribute name="placeholder">search</jsp:attribute>
              <jsp:attribute name="value">${result.params['query']}</jsp:attribute>
              <jsp:attribute name="type">text</jsp:attribute>
              <jsp:attribute name="name">query</jsp:attribute>
            </jsp:element>
            <input type="hidden" name="limit" value="${result.pageSize}"/>
            <input type="hidden" name="start" value="0"/>
            <c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
             <jsp:element name="input">
               <jsp:attribute name="type">hidden</jsp:attribute>
               <jsp:attribute name="name">facet</jsp:attribute>
               <jsp:attribute name="value">
                 <jsp:scriptlet>
                   String selectedFacet = (String)pageContext.getAttribute("selectedFacet");
                   out.print(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
                 </jsp:scriptlet>
               </jsp:attribute>
             </jsp:element>
            </c:forEach>
            <input type="submit" value="go" name="submit"/>
          </form>
          <div id="pages"><spring:message code="pager.message" arguments="${result.firstRecord}, ${result.lastRecord}"/><jsp:expression>result.size()</jsp:expression></div>
          <jsp:scriptlet>
            Integer[] pageSizes =  new Integer[] {10,20,50,100};
            request.setAttribute("pageSizes",pageSizes);
          </jsp:scriptlet>
          <ul id="pageSize">
            <c:forEach var="pageSize" items="${pageSizes}">
              <c:choose>
                <c:when test="${pageSize == result.pageSize}">
                  <li>${pageSize}</li>
                </c:when>
                <c:otherwise>
                  <li>
                   <jsp:element name="a">
                     <jsp:attribute name="href">
                       <c:url value="search">
                         <c:param name="query" value="${result.params['query']}"/>
                         <c:param name="limit" value="${pageSize}"/>
                         <c:param name="start" value="0"/>
                         <c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
                           <c:param name="facet">
                             <jsp:scriptlet>
                               String selectedFacet = (String)pageContext.getAttribute("selectedFacet");
                               out.println(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
                             </jsp:scriptlet>
                           </c:param>
                         </c:forEach>
                       </c:url>
                     </jsp:attribute>
                     ${pageSize}
                   </jsp:element>
                  </li>
                </c:otherwise>
              </c:choose>
            </c:forEach>
            <spring:message code="results.per.page" />
          </ul>
          <ul id="pageNumber">
            <c:if test="${result.prevIndex != null}">
              <li>
                <jsp:element name="a">
                  <jsp:attribute name="href">
                    <c:url value="search">
                      <c:param name="query" value="${result.params['query']}"/>
                      <c:param name="limit" value="${result.pageSize}"/>
                      <c:param name="start" value="${result.prevIndex}"/>
                      <c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
                        <c:param name="facet">
                          <jsp:scriptlet>
                            String selectedFacet = (String)pageContext.getAttribute("selectedFacet");
                            out.println(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
                          </jsp:scriptlet>
                        </c:param>
                      </c:forEach>
                    </c:url>
                  </jsp:attribute>
                «</jsp:element>
              </li>
            </c:if>
            <c:forEach var="index" items="${result.indices}">
              <li>
                <c:choose>
                  <c:when test="${result.currentIndex == index}">
                    <jsp:scriptlet>
                      Integer index = (Integer) pageContext.getAttribute("index");
                      out.println(result.getPageNumber(index));
                    </jsp:scriptlet>
                  </c:when>
                  <c:otherwise>
                    <jsp:element name="a">
                      <jsp:attribute name="href">
                        <c:url value="search">
                          <c:param name="query" value="${result.params['query']}"/>
                          <c:param name="limit" value="${result.pageSize}"/>
                          <c:param name="start" value="${index}"/>
                          <c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
                            <c:param name="facet">
                              <jsp:scriptlet>
                                String selectedFacet = (String)pageContext.getAttribute("selectedFacet");
                                out.println(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
                              </jsp:scriptlet>
                            </c:param>
                          </c:forEach>
                        </c:url>
                      </jsp:attribute>
                      <jsp:scriptlet>
                        Integer index = (Integer) pageContext.getAttribute("index");
                        out.println(result.getPageNumber(index));
                      </jsp:scriptlet>
                    </jsp:element>
                  </c:otherwise>
                </c:choose>
              </li>
            </c:forEach>
            <c:if test="${result.nextIndex != null}">
              <li>
                <jsp:element name="a">
                  <jsp:attribute name="href">
                    <c:url value="search">
                      <c:param name="query" value="${result.params['query']}"/>
                      <c:param name="limit" value="${result.pageSize}"/>
                      <c:param name="start" value="${result.nextIndex}"/>
                      <c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
                        <c:param name="facet">
                          <jsp:scriptlet>
                            String selectedFacet = (String)pageContext.getAttribute("selectedFacet");
                            out.println(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
                          </jsp:scriptlet>
                        </c:param>
                      </c:forEach>
                    </c:url>
                  </jsp:attribute>
                  »</jsp:element>
              </li>
            </c:if>
          </ul>
        </div>
        </div>
        </div>
        <div class="row">
        <div class="threecol">
        <ul id="facets">
          <c:forEach var="facetName" items="${result.facetNames}">
            <li><h2><spring:message code="${facetName}" /></h2>            
              <ul class="facet">              
                <jsp:scriptlet>
                  String facetName = (String) pageContext.getAttribute("facetName");
                  pageContext.setAttribute("facetSelected",new Boolean(result.isFacetSelected(facetName)));
                </jsp:scriptlet>
                <c:choose>
                <c:when test="${facetSelected}">
                  <li>
                    <jsp:element name="a">
                      <jsp:attribute name="href">
                        <c:url value="search">
                          <c:param name="query" value="${result.params['query']}"/>
                          <c:param name="limit" value="${result.pageSize}"/>
                          <c:param name="start" value="0"/>
                          <c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
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
                    </jsp:element>
                  </li>
                  <li>
                    <jsp:scriptlet>
                      try{
                          org.hibernate.search.query.facet.Facet facet = result.getFacets().get(facetName).get(result.getSelectedFacets().get(facetName));
                          pageContext.setAttribute("selectedFacetName",facet.getValue());
                      } catch(Exception e){
                          pageContext.setAttribute("selectedFacetName", "Unable to display name of selected option");
                      }
                    </jsp:scriptlet>
                    ${selectedFacetName}
                  </li>
                </c:when>
                <c:otherwise>
                  <c:forEach var="facet" items="${result.facets[facetName]}" varStatus="loopStatus">
                    <li>
                      <c:choose>
                        <c:when test="${facet.count == 0}">${facet.value}</c:when>
                        <c:otherwise>
                          <jsp:element name="a">
                            <jsp:attribute name="href">
                              <c:url value="search">
                                <c:param name="query" value="${result.params['query']}"/>
                                <c:param name="limit" value="${result.pageSize}"/>
                                <c:param name="start" value="0"/>
                                <c:param name="facet" value="${facetName}.${loopStatus.index}"/>
                                <c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
                                  <c:if test="${selectedFacet != facetName}">  
                                    <c:param name="facet">
                                      <jsp:scriptlet>
                                        String selectedFacet = (String)pageContext.getAttribute("selectedFacet");
                                        out.println(selectedFacet + "." + result.getSelectedFacets().get(selectedFacet));
                                      </jsp:scriptlet>
                                    </c:param>
                                  </c:if>
                                </c:forEach>
                            </c:url>
                          </jsp:attribute>
                          ${facet.value}
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
      </ul>
      </div>
      <div class="ninecol last">
      <ul id="results">      
        <c:forEach var="item" items="${result.records}">
          <li>
          <c:choose>
            <c:when test="${item.className == 'Taxon'}">
              <a href="taxon/${item.identifier}">${item.name}</a>
            </c:when>
            <c:when test="${item.className == 'Image'}">
              <a href="image/${item.identifier}">${item.caption}</a>
            </c:when>
            <c:otherwise>
              Unknown class ${item.className}
            </c:otherwise>
          </c:choose>
          </li>
        </c:forEach>
      </ul>
      </div>
      </div>
    </div>
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
    </body>
  </html>
</jsp:root>