<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:tags="urn:jsptagdir:/WEB-INF/tags"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions" version="2.0">
	<jsp:directive.attribute name="taxon"
		type="org.emonocot.model.taxon.Taxon" required="true" />

	<c:url value="http://edit.br.fgov.be/edit_wp5/v1/areas.php"
		var="alternativeMapUrl">
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
	<noscript>
		<img id="alternative-map" src="${alternativeMapUrl}" />
	</noscript>
	<div id="map" class="noPrint">&#160;</div>
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
	<c:url var="openlayersImageLocation" value="/images/OpenLayers/" />
	<script type="text/javascript">
		var map;

		function foo(data) {
			if (!data) {
			} else {
				OpenLayers.ImgPath = "${openlayersImageLocation}";
				<spring:message var="useDynamicBaseLayer" code="web.map.dynamic.base.layer"/>
				<c:choose>
				<c:when test="${useDynamicBaseLayer eq 'false'}">
				var options = {
					numZoomLevels : 3
				};
				var base_layer = new OpenLayers.Layer.Image(
						'City Lights',
						'http://earthtrends.wri.org/images/maps/4_m_citylights_lg.gif',
						new OpenLayers.Bounds(-180, -88.759, 180, 88.759),
						new OpenLayers.Size(580, 288), options);

				map = new OpenLayers.Map('map',
						{
							controls : [ new OpenLayers.Control.Navigation({
								zoomWheelEnabled : false
							}), new OpenLayers.Control.PanZoomBar() ],
							maxExtent : new OpenLayers.Bounds(-180, -90, 180,
									90),
							maxResolution : 0.72,
							restrictedExtent : new OpenLayers.Bounds(-180, -90,
									180, 90),
							projection : new OpenLayers.Projection("EPSG:4326")
						});
				map.addLayers([ base_layer ]);

				</c:when>
				<c:otherwise>
				var base_layer = new OpenLayers.Layer.WMS("OpenLayers WMS",
						"http://labs.metacarta.com/wms/vmap0", {
							layers : 'basic'
						}, {
							maxExtent : new OpenLayers.Bounds(-180, -90, 180,
									90),
							isBaseLayer : true,
							displayInLayerSwitcher : false
						});
				map = new OpenLayers.Map('map',
						{
							controls : [ new OpenLayers.Control.Navigation({
								zoomWheelEnabled : false
							}), new OpenLayers.Control.PanZoomBar() ],
							maxExtent : new OpenLayers.Bounds(-180, -90, 180,
									90),
							maxResolution : 0.72,
							restrictedExtent : new OpenLayers.Bounds(-180, -90,
									180, 90),
							projection : new OpenLayers.Projection("EPSG:4326")
						});
				map.addLayers([ base_layer ]);

				for (i in data.layers) {
					var layerName = "topp:tdwg_level_"
							+ data.layers[i].tdwg.substr(4, 1);

					var layer = new OpenLayers.Layer.WMS.Untiled("layer "
							+ (i + 1), data.geoserver, {
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
				</c:otherwise>
				</c:choose>

				map.addControl(new OpenLayers.Control.Navigation({
					zoomWheelEnabled : false
				}));
				var bbox = data.bbox.split(",");
				map
						.zoomToExtent(new OpenLayers.Bounds(parseInt(bbox[0]),
								parseInt(bbox[1]), parseInt(bbox[2]),
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

</jsp:root>
