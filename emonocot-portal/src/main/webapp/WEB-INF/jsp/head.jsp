<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<head>
<!-- Include the latest version of jQuery -->

<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Language" content="en" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link type="images/x-icon" href="images/favicon.ico" rel="shortcut icon" />
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js">/* */</script>

 <script type="text/javascript">
	$(document).ready(function() {
		$("#showcase").awShowcase({
			content_width : 700,
			content_height : 470,
			fit_to_parent : false,
			auto : false,
			interval : 3000,
			continuous : false,
			loading : true,
			tooltip_width : 200,
			tooltip_icon_width : 32,
			tooltip_icon_height : 32,
			tooltip_offsetx : 18,
			tooltip_offsety : 0,
			arrows : true,
			buttons : true,
			btn_numbers : true,
			keybord_keys : true,
			mousetrace : false, /* Trace x and y coordinates for the mouse */
			pauseonover : true,
			stoponclick : true,
			transition : 'hslide', /* hslide/vslide/fade */
			transition_delay : 300,
			transition_speed : 500,
			show_caption : 'onhover', /* onload/onhover/show */
			thumbnails : true,
			thumbnails_position : 'outside-last', /* outside-last/outside-first/inside-last/inside-first */
			thumbnails_direction : 'horizontal', /* vertical/horizontal */
			thumbnails_slidex : 0, /* 0 = auto / 1 = slide one thumbnail / 2 = slide two thumbnails / etc. */
			dynamic_height : false, /* For dynamic height to work in webkit you need to set the width and height of images in the source. Usually works to only set the dimension of the first slide in the showcase. */
			speed_change : false, /* Set to true to prevent users from swithing more then one slide at once. */
			viewline : false
		/* If set to true content_width, thumbnails, transition and dynamic_height will be disabled. As for dynamic height you need to set the width and height of images in the source. */
		});
	});
</script>
<jsp:element name="script">
    <jsp:attribute name="type">text/javascript</jsp:attribute>
    	
      <jsp:attribute name="src">
        <c:url value="/js/header.js" />
      </jsp:attribute>   
      /* */
    </jsp:element>


<jsp:element name="link">
	  <jsp:attribute name="rel">stylesheet</jsp:attribute>
	  <jsp:attribute name="type">text/css</jsp:attribute>
	  <jsp:attribute name="media">screen</jsp:attribute>
	  <jsp:attribute name="href">
	    <c:url value="/css/base.css" />
	  </jsp:attribute>
	</jsp:element>



<!--[if lte IE 9]><link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" /><![endif]-->
<jsp:element name="link">
	  <jsp:attribute name="rel">stylesheet</jsp:attribute>
	  <jsp:attribute name="type">text/css</jsp:attribute>
	  <jsp:attribute name="media">screen</jsp:attribute>
	  <jsp:attribute name="href">
	    <c:url value="/css/1140.css" />
	  </jsp:attribute>
	</jsp:element>

<jsp:element name="link">
	  <jsp:attribute name="rel">stylesheet</jsp:attribute>
	  <jsp:attribute name="type">text/css</jsp:attribute>
	  <jsp:attribute name="media">screen</jsp:attribute>
	  <jsp:attribute name="href">
	    <c:url value="/css/style.css" />
	  </jsp:attribute>
	</jsp:element>
<!-- 
	<jsp:element name="link">
	  <jsp:attribute name="rel">stylesheet</jsp:attribute>
	  <jsp:attribute name="type">text/css</jsp:attribute>
	  <jsp:attribute name="media">screen</jsp:attribute>
	  <jsp:attribute name="href">
	    <c:url value="/css/thumb.css"/>
	  </jsp:attribute>
	</jsp:element>
	 -->
    <jsp:element name="link">
	  <jsp:attribute name="rel">stylesheet</jsp:attribute>
	  <jsp:attribute name="type">text/css</jsp:attribute>
	  <jsp:attribute name="media">screen</jsp:attribute>
	  <jsp:attribute name="href">
	    <c:url value="/css/style-aw.css" />
	  </jsp:attribute>
	</jsp:element>



	</head>
</jsp:root>