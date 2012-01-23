<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:tags="urn:jsptagdir:/WEB-INF/tags"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	
	<c:set var="bibliography" value="${em:bibliography(taxon)}"/>
	<div class="content">
		<div class="page-header">
			<h2 id="page-title">
				<span class="taxonName">${taxon.name}</span> ${taxon.authorship}
			</h2>				
			<c:if test="${taxon.protologue != null}">
				<small id="protologue">${taxon.protologue.title} ${taxon.protologue.volume}${taxon.protologueMicroReference} ${taxon.protologue.datePublished}</small>
			</c:if>
		</div>
		
		<div class="row">
			<div class="span12">
		  		<c:choose>
				  	<c:when test="${taxon.accepted != null}">
						<section id="accepted">
							<spring:message code="isSynonym" />
							<jsp:element name="a">
		                    	<jsp:attribute name="href">
		                      		<c:url value="/taxon/${taxon.accepted.identifier}" />
		                    	</jsp:attribute>
		                    	${taxon.accepted.name} ${taxon.accepted.authorship}
		               		</jsp:element>
		               	</section>
					</c:when>
					<c:otherwise>
						<spring:message code="isAccepted" />
					</c:otherwise>
				</c:choose>
				
			
				<c:if test="${not empty taxon.images}">
					<section id="gallery" class="ad-gallery">
				    	<div class="ad-image-wrapper">&#160;</div>
						<div class="ad-controls">&#160;</div>
						<div class="ad-nav">
						  <div class="ad-thumbs">
						    <ul class="ad-thumb-list media-grid">
							  <c:forEach var="image" items="${taxon.images}" varStatus="status">
							    <li>
							      <a href="${image.url}">
							        <c:url var="thumbnail" value="/images/thumbnails/${image.identifier}.jpg"/>
								    <c:url var="url" value="/image/${image.identifier}"/> 
							        <img	src="${thumbnail}" class="${status.index} thumbnail" title="${image.caption}" ad-href="${url}" />
							      </a>
							    </li>
							  </c:forEach>
						    </ul>
						  </div>
				    	</div>
			  		</section>
				</c:if>
				<section id="textContent">
					<c:forEach var="feature" items="${em:features()}">
						<c:set var="content" value="${em:content(taxon,feature)}" />
						<c:if test="${content != null}">
							<div>
						    	<h5><spring:message code="${feature}"/></h5>
						    	<p>${content.content}</p>
						    	<c:if test="${not empty content.references }">
						    	  <span class="citations">
						    	    <c:forEach var="reference" items="${content.references}">
						    	      <li><a href="#${reference.identifier}">${em:citekey(bibliography, reference)}</a></li>
						    	    </c:forEach>
						    	  </span>
						    	</c:if>
						  	</div>
						</c:if>
					</c:forEach>
				</section>			
			<c:if test="${not empty em:regions(taxon)}">
				<section id="distribution">
					<h5><spring:message code="distribution" /></h5>
			  		<div id="map">
						<jsp:element name="img">
				  			<jsp:attribute name="id">alternative-map</jsp:attribute>
				  			<jsp:attribute name="src">
				  				<c:url value="http://edit.br.fgov.be/edit_wp5/v1/areas.php">
				  					<c:param name="l" value="earth" />
				  					<!-- Layer -->
				  					<c:param name="ms" value="800" />
				  					<!-- Map Size -->
				  					<c:param name="bbox" value="-180,-90,180,90" />
				  					<!-- Bounding Box -->
				  					<c:param name="ad" value="${em:map(taxon)}" />
				  					<!-- Areas -->
				  					<c:param name="as" value="present:FF0000,,0.25" />
				  					<!-- Area Styling -->
				  				</c:url>
				  			</jsp:attribute>
				  		</jsp:element>
				  	</div>
				  	<c:set var="mapUrl">
						<c:url value="http://edit.br.fgov.be/edit_wp5/v1/areas.php">
				  			<c:param name="callback" value="foo" />
				  			<!-- callback -->
				  			<c:param name="ms" value="1" />
				  			<!-- Map Size -->
				  			<c:param name="l" value="earth" />
				  			<!-- Layer -->
				  			<c:param name="img" value="false" />
				  			<!-- Bounding Box -->
				  			<c:param name="ad" value="${em:map(taxon)}" />
							<!-- Areas -->
							<c:param name="as" value="present:FF0000,,0.25" />
							<!-- Area Styling -->
						</c:url>
					</c:set>
					
					<script type="text/javascript">
						var map;
						function foo(data) {
					  		if (!data) {
	                  	} else {
							$('#alternative-map').hide();
							var base_layer = new OpenLayers.Layer.WMS(
								"OpenLayers WMS",
								"http://labs.metacarta.com/wms/vmap0",
								{
								layers : 'basic'
								},
								{
								maxExtent : new OpenLayers.Bounds(-180, -90, 180, 90),
								isBaseLayer : true,
								displayInLayerSwitcher : false
								});
							map = new OpenLayers.Map('map',
							{
								maxExtent : new OpenLayers.Bounds(-180, -90, 180, 90),
								maxResolution : 0.72,
								restrictedExtent : new OpenLayers.Bounds(-180, -90, 180, 90),
								projection : new OpenLayers.Projection("EPSG:4326")
							});
							
							map.addLayers([ base_layer ]);
							
							for (i in data.layers) {
								var layerName = "topp:tdwg_level_" + data.layers[i].tdwg.substr(4, 1);
								
								var layer = new OpenLayers.Layer.WMS.Untiled(
									"layer " + (i + 1),data.geoserver,
									{
										layers : layerName,
										transparent : "true",
										format : "image/png"
									},
									
									{
										maxExtent : new OpenLayers.Bounds(-180, -90, 180, 90),
										isBaseLayer : false,
										displayInLayerSwitcher : false
									});
								
								layer.params.SLD = data.layers[i].sld;
								map.addLayers([ layer ]);
								}
	
								var bbox = data.bbox.split(",");
								map.zoomToExtent(new OpenLayers.Bounds(
										parseInt(bbox[0]),
										parseInt(bbox[1]),
										parseInt(bbox[2]),
										parseInt(bbox[3])));
								}
					  		}
						
						$(document).ready(function() {
							$.ajax({
								url : "${mapUrl}",
								dataType : "script",
								type : "GET",
								cache : true,
								callback : foo,
								data : null
								});
							});
					</script>
					
					<ul id="distribution-textual">
						<c:forEach var="region" items="${em:regions(taxon)}">
							<li>
								<spring:message code="${region}" />
							</li>
						</c:forEach>
					</ul>
				</section>
			</c:if>
			
			<c:if test="${not empty taxon.children}">
				<section id="children">
					<a name="children">
						<h5>
							<spring:message code="children" />
						</h5>
					</a>
					<ul>
						<c:forEach var="child" items="${em:sort(taxon.children)}">
							<li>
								<jsp:element name="a">
									<jsp:attribute name="href">
										<c:url value="/taxon/${child.identifier}" />
									</jsp:attribute>
									${child.name} ${child.authorship}
                  				</jsp:element>
                  			</li>
						</c:forEach>
					</ul>
				</section>
			</c:if>
			
			<c:if test="${not empty taxon.synonyms}">
				<section id="synonyms">
					<h5>
						<spring:message code="synonyms" />
					</h5>
					<ul>
						<c:forEach var="synonym" items="${em:sort(taxon.synonyms)}">
							<li>
								<jsp:element name="a">
                      				<jsp:attribute name="href">
                        				<c:url value="/taxon/${synonym.identifier}" />
                      				</jsp:attribute>
                      				${synonym.name} ${synonym.authorship}
                    			</jsp:element>
                   			</li>
						</c:forEach>
					</ul>
				</section>
			</c:if>
			<c:if test="${not empty bibliography.references}">
		        <section id="bibliography">
					<h5>
						<spring:message code="bibliography" />
					</h5>
					<ul>
						<c:forEach var="reference" items="${bibliography.references}">
							<li>
								<a id="${reference.identifier}">${em:citekey(bibliography,reference)}</a>
								<c:choose>
								  <c:when test="${not empty reference.citation}">
								    ${reference.citation}
								  </c:when>
								  <c:otherwise>
								    <!-- Construct reference ourselves -->
								    ${reference.author} <c:if test="${not empty reference.datePublished}">(${reference.datePublished})</c:if> ${reference.title} ${reference.publishedIn} ${reference.volume} <c:if test="${not empty reference.pages}">: ${reference.pages}</c:if> <c:if test="${not empty reference.publisher}">${reference.publisher}.</c:if>
								  </c:otherwise>
								</c:choose>                   				
                      		    
                   			</li>
						</c:forEach>
					</ul>
		       </section>
		   </c:if>
			
		</div>
		<div class="span4 info-right">
		    <section id="sources">
			  <h5><spring:message code="sources" /></h5>
			  <ul>
				<c:forEach var="source" items="${taxon.sources}">
					<li>
						<jsp:element name="a">
							<jsp:attribute name="href">
                        		<c:url value="/source/${source.identifier}" />
                      		</jsp:attribute>
                      	${source.title}
                    	</jsp:element>
                   	</li>
				</c:forEach>
			  </ul>
			</section>
				
			<ul id="taxonHierarchy" class="no-bullet">
				<c:if test="${not empty taxon.ancestors}">
					<tags:tree taxon = "${taxon}" ancestors="${taxon.ancestors}" />
				</c:if>
			</ul>
		</div>
	</div>
</div>
</jsp:root>
