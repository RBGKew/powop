<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:tags="urn:jsptagdir:/WEB-INF/tags"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions" version="2.0">
	<jsp:directive.attribute name="taxon"
		type="org.emonocot.model.taxon.Taxon" required="true" />
	
	<div id="map" class="noPrint">&#160;</div>
	
	<c:url var="openlayersImageLocation" value="/images/OpenLayers/" />
	<spring:message code="web.map.server.url" var="wmsUrl"/>
	<script type="text/javascript">
	
    $(document).ready(function() {
	  var map;
	  var originalExtent = new OpenLayers.Bounds(${em:boundingBox(taxon)});
	  OpenLayers.Map.prototype.zoomToMaxExtent = function() {
      	this.zoomToExtent(originalExtent);
      };
	  OpenLayers.ImgPath = "${openlayersImageLocation}";												
						
	  map = new OpenLayers.Map(
	    'map',
		{controls : [new OpenLayers.Control.Navigation({zoomWheelEnabled : false}),
					 new OpenLayers.Control.PanZoomBar({zoomWorldIcon: true})],
		 units: "m",
		 numZoomLevels: 8,
		 zoomOffset:0,
		 projection : new OpenLayers.Projection("EPSG:3857"),
		 maxResolution: 156543.0339,
		 maxExtent: new OpenLayers.Bounds(-20037500,
										  -20037500,
										   20037500,
										   20037500)
	  });
	  var base_layer = new OpenLayers.Layer.TMS("MapBox Layer",
		 [ "http://build.e-monocot.org/tiles/" ],
		 { 'layername': 'eMonocot', 'type': 'png',isBaseLayer : true, }
	  );

	  map.addLayers([ base_layer ]);
	  <c:if test="${em:hasLevel1Features(taxon)}">
		var level1 = new OpenLayers.Layer.WMS(
		"level1","${wmsUrl}",
		{
			layers : "tdwg:level1",
			transparent : "true",
			format : "image/png",
			tiled: 'true'
		},
																			
		{
			isBaseLayer : false,
			displayInLayerSwitcher : false
		});
																		
		level1.params.STYLES = "eMonocot";
		level1.params.FEATUREID = "${em:getLevel1Features(taxon)}";
		map.addLayers([ level1 ]);
	 </c:if>
	 <c:if test="${em:hasLevel2Features(taxon)}">
		var level2 = new OpenLayers.Layer.WMS(
		"level1","${wmsUrl}",
		{
			layers : "tdwg:level2",
			transparent : "true",
			format : "image/png",
			tiled: 'true'
		},
																			
		{
			isBaseLayer : false,
			displayInLayerSwitcher : false
		});
																		
		level2.params.STYLES = "eMonocot";
		level2.params.FEATUREID = "${em:getLevel2Features(taxon)}";
		map.addLayers([ level2 ]);
	 </c:if>
	 <c:if test="${em:hasLevel3Features(taxon)}">
		var level3 = new OpenLayers.Layer.WMS(
		"level3","${wmsUrl}",
		{
			layers : "tdwg:level3",
			transparent : "true",
			format : "image/png",
			tiled: 'true'
		},
																			
		{
			isBaseLayer : false,
			displayInLayerSwitcher : false
		});
																		
		level3.params.STYLES = "eMonocot";
		level3.params.FEATUREID = "${em:getLevel3Features(taxon)}";
		map.addLayers([ level3 ]);
	 </c:if>											
												
	  map.addControl(new OpenLayers.Control.Navigation({zoomWheelEnabled:false}));
	  map.zoomToExtent(originalExtent);	
	});
		
	</script>
</jsp:root>
