<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<style type="text/css">
      body {
        background-color: #ffffff;
      }
    </style>
	<header class="jumbotron masthead">
	  <div class="inner">
	    <div class="container">
    	<h1><spring:message code="hero.title"/></h1>
        <p class="lead"><spring:message code="hero.message"/></p>
        <script type="text/javascript">				
		  $(document).ready(function() {
			  $("input#query").autocomplete({
		            source: "autocomplete",
		            minLength: 2
		        });
		  });
		</script>
     	<form action="search" method="GET">
          <div class="clearfix">
     		<div class="input">					
			  <input id="query" name="query" class="xxlarge" type="text" value="" />
			  <button type="submit" class="btn primary">
					<span>SEARCH</span>
			  </button>					
			</div>
			<span class="help-block"><spring:message code="search.for"/></span>
		  </div>
		</form> 
		</div>
      </div>       	
	</header>
</jsp:root>