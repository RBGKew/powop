<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:tiles="http://tiles.apache.org/tags-tiles" version="2.0">
	<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
	<jsp:output omit-xml-declaration="true" />
	<jsp:output doctype-root-element="HTML"
		doctype-system="about:legacy-compat" />
	  <html class="no-js" lang="en" dir="ltr">
        <tiles:insertAttribute name="head" />
        <body>
	      <div id="wrapper">
		  <!--   <div id="container">--> 
			  <tiles:insertAttribute name="header" />
			  <tiles:insertAttribute name="body" />
          <!-- </div>  --> 
	      </div>
	      <br />
	      <tiles:insertAttribute name="footer" />

	      
        </body>
      </html>
</jsp:root>