<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	

	
	<div class="content">
		<div class="page-header">
			<!-- <header>
				<h2 id="page-title">
					<span class="taxonName">${taxon.name}</span>${taxon.authorship}
				</h2>
			</header> -->
			<h2 id="page-title">
				<span class="taxonName">${taxon.name}</span>${taxon.authorship}
			</h2>
				
			<c:if test="${taxon.protologue != null}">
				<c:set var="protologue" value="${taxon.protologue}" />
				<div id="protologue" class="twelvecol">${protologue.title}
					${protologue.datePublished} ${protologue.volume}
					${protologue.pages}
				</div>
			</c:if>
		</div>
		
		<div class="row">
			<div class="span12" style="border-right: 1px solid #eee;">	
						<c:if test="${not empty taxon.images}">
							<!-- <div id="container"> -->
								<div id="gallery" class="ad-gallery">
									<div class="ad-image-wrapper">&#160;</div>
									<div class="ad-controls">&#160;</div>
									<div class="ad-nav">
										<div class="ad-thumbs">
											<ul class="ad-thumb-list media-grid">
												<c:forEach var="image" items="${taxon.images}"
													varStatus="status">
													<li><a href="${image.url}">
													    <c:url var="url" value="/image/${image.identifier}"/> 
													    <img
															src="${image.url}" class="${status.index} thumbnail"
															title="${image.caption}" ad-href="${url}" /></a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
								</div>

							<!-- </div> -->
						</c:if>





						<BR />
					
				<br />
				<div id="textContent">
					<c:forEach var="feature" items="${em:features()}">
						<c:set var="content" value="${em:content(taxon,feature)}" />
						<c:if test="${content != null}">
							<div>
							  <h5>${feature}</h5>
							  <p>${content.content}</p>
							</div>
						</c:if>
					</c:forEach>
				</div>
				<br />
				<c:if test="${not empty em:regions(taxon)}">
					<h5>
						<spring:message code="distribution" />
					</h5>
					<div id="map" style="height: 470px; width: 700px">
						<jsp:element name="img">
						    <jsp:attribute name="id">alternative-map</jsp:attribute>
							<jsp:attribute name="src">
								<c:url value="http://edit.br.fgov.be/edit_wp5/v1/areas.php">
									<c:param name="l" value="earth" />
											<!-- Layer -->
									<c:param name="ms" value="470" />
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
												maxExtent : new OpenLayers.Bounds(
														-180, -90, 180, 90),
												isBaseLayer : true,
												displayInLayerSwitcher : false
											});
									map = new OpenLayers.Map(
											'map',
											{
												maxExtent : new OpenLayers.Bounds(
														-180, -90, 180, 90),
												maxResolution : 0.72,
												restrictedExtent : new OpenLayers.Bounds(
														-180, -90, 180, 90),
												projection : new OpenLayers.Projection(
														"EPSG:4326")
											});
									map.addLayers([ base_layer ]);

									for (i in data.layers) {
										var layerName = "topp:tdwg_level_"
												+ data.layers[i].tdwg.substr(4,
														1);

										var layer = new OpenLayers.Layer.WMS.Untiled(
												"layer " + (i + 1),
												data.geoserver,
												{
													layers : layerName,
													transparent : "true",
													format : "image/png"
												},
												{
													maxExtent : new OpenLayers.Bounds(
															-180, -90, 180, 90),
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
				</c:if>
				<br />
				<c:if test="${taxon.parent != null}">
					<h5>
						<spring:message code="parent" />
					</h5>
					<jsp:element name="a">
						<jsp:attribute name="href">
							<c:url value="/taxon/${taxon.parent.identifier}" />
                  		</jsp:attribute>
                  		${taxon.parent.name} ${taxon.parent.authorship}
                	</jsp:element>
                </c:if>
				<br />
				<c:if test="${not empty taxon.children}">
					<h5>
						<spring:message code="children" />
					</h5>
					<ul>
						<c:forEach var="child" items="${taxon.children}">
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
				</c:if>
				<br />
				<c:if test="${taxon.accepted != null}">
					<h5>
						<spring:message code="accepted" />
					</h5>
					<jsp:element name="a">
                    	<jsp:attribute name="href">
                      		<c:url value="/taxon/${taxon.accepted.identifier}" />
                    	</jsp:attribute>
                    	${taxon.accepted.name} ${taxon.accepted.authorship}
               		</jsp:element>
               		</c:if>
				<br />
				<c:if test="${not empty taxon.synonyms}">
					<h5>
						<spring:message code="synonyms" />
					</h5>
					<ul>
						<c:forEach var="synonym" items="${taxon.synonyms}">
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
				</c:if>
			</div>
			<div class="span4">Prova</div>
		</div>
	</div>
</jsp:root>
