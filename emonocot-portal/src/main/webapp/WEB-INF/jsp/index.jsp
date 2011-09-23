<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
	<jsp:output omit-xml-declaration="true" />
	<jsp:output doctype-root-element="HTML"
		doctype-system="about:legacy-compat" />
	<html class="no-js" lang="en" dir="ltr">
<jsp:include page="/WEB-INF/jsp/head.jsp" />
<body>
 <div id="wrapper">
		<div id="container">
	

			<jsp:include page="/WEB-INF/jsp/header.jsp" />

			<div class="page-bubble">
				<div class="home-header">
					<h1>emonocot</h1>
					<h2 class="subheader"></h2>
					<a href="http://www.e-monocot.org" id="helpLink">What is
						emonocot?</a>
				</div>
				<!--  <form id="search.form" accept-charset="UTF-8" method="GET" action="search">
               			<input class="box" type="text" placeholder="search" name="query"/>
                		<input type="submit" value="go" name="submit"/>
              		</form>
              		-->
				<form action="search" id="search form" method="GET">
					<div style="margin: 0; padding: 0; display: inline"></div>
					<input class="oversize input-text" id="emonocot-search" name="query"
						size="40" type="text" value="" />
					<button type="submit" class="super large gray button">
						<span>SEARCH</span>
					</button>
					<small id="hint">Search for... </small>

				</form>
				<br/> 
				<!--  <span class="shadow"></span>  -->
			</div>
			<div class="steps">
			</div>
		</div>
	</div>  
	<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
	</html>
</jsp:root>