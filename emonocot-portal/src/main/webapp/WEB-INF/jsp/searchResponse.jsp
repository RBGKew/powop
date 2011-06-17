<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  version="2.0">
  <jsp:directive.page contentType="text/html;charset=UTF-8" />
  <jsp:useBean id="result" scope="request" type="org.emonocot.model.pager.Page&lt;org.emonocot.model.taxon.Taxon&gt;" />
  <html>
    <head>                              
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
        <meta http-equiv="Content-Language" content="en" />
        <link rel="stylesheet" type="text/CSS" href="css/style.css"/>
    </head> 
    <body>
      <h2>eMonocot</h2>
      <div id="search">
        <form id="search.form" accept-charset="UTF-8" method="GET" action="search">
          <jsp:element name="input">
            <jsp:attribute name="value">${result.params['query']}</jsp:attribute>
            <jsp:attribute name="type">text</jsp:attribute>
            <jsp:attribute name="name">query</jsp:attribute>
          </jsp:element>
          <input type="hidden" name="limit" value="${result.pageSize}"/>
          <input type="hidden" name="start" value="0"/>
          <c:forEach var="selectedFacet" items="${result.selectedFacetNames}">
           <input type="hidden" name="start" value="${result.currentIndex}"/>
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
        <div id="pages">Showing ${result.firstRecord} to ${result.lastRecord} of <jsp:expression>result.size()</jsp:expression></div>
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
                       <c:param name="start" value="${result.currentIndex}"/>
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
          <li>results per page</li>
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
          <li>${facetName}            
            <ul class="facet">              
              <jsp:scriptlet>
                String facetName = (String) pageContext.getAttribute("facetName");
                pageContext.setAttribute("facetSelected",new Boolean(result.isFacetSelected(facetName)));
              </jsp:scriptlet>
              <c:if test="${facetSelected}">
                <li>
                  <jsp:element name="a">
                    <jsp:attribute name="href">
                      <c:url value="search">
                        <c:param name="query" value="${result.params['query']}"/>
                        <c:param name="limit" value="${result.pageSize}"/>
                        <c:param name="start" value="${result.currentIndex}"/>
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
                    ${facetName}
                  </jsp:element>
                </li>
              </c:if>
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
                            <c:param name="start" value="${result.currentIndex}"/>
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
            </ul>
          </li>
        </c:forEach>
      </ul>
      <ul id="results">      
        <c:forEach var="taxon" items="${result.records}">
          <li>${taxon.name}</li>
        </c:forEach>
      </ul>
    </body>
  </html>
</jsp:root>