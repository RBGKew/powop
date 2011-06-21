<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  version="2.0">
  <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
  <jsp:output omit-xml-declaration="true" />
  <jsp:output doctype-root-element="HTML" doctype-system="about:legacy-compat"/>
  <jsp:useBean id="result" scope="request" type="org.emonocot.model.pager.Page&lt;org.emonocot.model.taxon.Taxon&gt;" />
  <html>
    <head>                              
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
        <meta http-equiv="Content-Language" content="en" />
        <link rel="stylesheet" type="text/CSS" href="css/style.css"/>
    </head> 
    <body>
      <h2><spring:message code="eMonocot" /></h2>
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
        <div id="pages"><spring:message code="pager.message" arguments="${result.firstRecord}, ${result.lastRecord}"/> <jsp:expression>result.size()</jsp:expression></div>
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
          <li><spring:message code="results.per.page" /></li>
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
      <ul id="facets">
        <c:forEach var="facetName" items="${result.facetNames}">
          <li><spring:message code="${facetName}" />            
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
                   org.hibernate.search.query.facet.Facet facet = result.getFacets().get(facetName).get(result.getSelectedFacets().get(facetName));
                   pageContext.setAttribute("selectedFacetName",facet.getValue());   
                  </jsp:scriptlet>
                  <spring:message code="${selectedFacetName}" />
                </li>
              </c:when>
              <c:otherwise>
                <c:forEach var="facet" items="${result.facets[facetName]}" varStatus="loopStatus">
                  <li>
                    <c:choose>
                      <c:when test="${facet.count == 0}"><spring:message code="${facet.value}" /></c:when>
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
      </ul>
      <ul id="results">      
        <c:forEach var="taxon" items="${result.records}">
          <li><a href="taxon/${taxon.identifier}">${taxon.name}</a></li>
        </c:forEach>
      </ul>
    </body>
  </html>
</jsp:root>