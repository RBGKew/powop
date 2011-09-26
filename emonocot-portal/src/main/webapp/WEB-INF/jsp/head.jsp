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

<jsp:element name="script">
        
    <jsp:attribute name="type">text/javascript</jsp:attribute>
    	
      <jsp:attribute name="src">
        <c:url
			value="http://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.js" />
      </jsp:attribute>   
      /* */
    </jsp:element>

<jsp:element name="script">
    
    <jsp:attribute name="type">text/javascript</jsp:attribute>
    	
      <jsp:attribute name="src">
        <c:url value="/js/jquery.aw-showcase.js" />
      </jsp:attribute>
      /* */   
    </jsp:element>

<jsp:element name="script">
    <jsp:attribute name="type">text/javascript</jsp:attribute>
    	
      <jsp:attribute name="src">
        <c:url value="/js/functions.js" />
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