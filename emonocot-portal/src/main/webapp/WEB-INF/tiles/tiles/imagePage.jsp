<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	
	
	<div class="content-wrapper">
		<header>
			<h2 id="page-title">${image.caption}</h2>
		</header>
		<br/>
		<h5>
			<spring:message code="imageOf"/>
			<a href="/portal/taxon/${image.taxon.identifier}">${image.taxon.name}</a>
		</h5>
		
		<br/>
		
		<img id="main-img" src="${image.url}" title="${image.caption}" alt="${status.index}" />
		
		<!-- 
		<div class="showcase-content">
			<img src="${image.url}" title="${image.caption}" alt="${status.index}" />
		</div>
		
				<div id="showcase" class="showcase">
					<c:forEach var="image" items="${taxon.images}">
						<div class="showcase-slide">
							<div class="showcase-thumbnail">
								<a href="/portal/image/${image.identifier}"><img src="${image.url}" alt="${status.index}" /></a>
															<img src="${image.url}" alt="${status.index}" width="140px" /> 
								<div class="showcase-thumbnail-caption">${image.caption}</div>
							</div>
						</div>
					</c:forEach>
				</div>
					
				
			<c:forEach var="image" items="${taxon.images}">
			<div class="showcase-thumbnail">
				
					<div class="showcase-thumbnail-container">
					
						<a href="/portal/image/${image.identifier}"><img src="${image.url}" alt="${status.index}" /></a>
											<img src="${image.url}" alt="${status.index}" width="140px" /> 
						<div class="showcase-thumbnail-caption">${image.caption}</div>
					
				</div>
				</div>
			</c:forEach>
			  -->	

<!-- 
					<div id="img"></div>
            			<ul id="gallery" class="jcarousel-skin-tango">
            				<c:forEach var="image" items="${taxon.images}" varStatus="status">
               					 <li><a href="${image.url}" title="${image.caption}"><img src="${image.url}" alt="${status.index}" width="50" height="40" /></a></li>
			                </c:forEach>    
            			</ul>
           
        -->     
		<!-- galleriffic -->
		<!-- 
		<div id="container">
			 <div class="content">
				<div class="slideshow-container">
					<div id="controls" class="controls"></div>
					<div id="loading" class="loader"></div>
					<div id="slideshow" class="slideshow"></div>
				</div>
				<div id="caption" class="caption-container">
					<div class="photo-index"></div>
				</div>
			</div>
		
		<div class="navigation-container">
			<div id="thumbs" class="navigation">
				<a class="pageLink prev" style="visibility: hidden;" href="#" title="Previous Page"></a>
				
				<ul class="thumbs noscript">
				
				<c:forEach var="image" items="${taxon.images}" varStatus="status">
					<li>
						<a class="thumb" name="leaf" href="/portal/image/${image.identifier}" title="${image.caption}">
							<img src="${image.url}" alt="${status.index}" width="50" height="50" />
						</a>
						
						<div class="caption">
							<div class="image-title">${image.caption}</div>								
							<div class="image-desc">Description</div>
							 <div class="download">
								<a href="http://farm4.static.flickr.com/3261/2538183196_8baf9a8015_b.jpg">Download Original</a>
							</div> 
						</div>
					</li>
				</c:forEach>
				
				</ul>
				<a class="pageLink next" style="visibility: hidden;" href="#" title="Next Page"></a>
			</div>
        </div> -->
                 <!-- End Gallery Html Containers -->
		<!-- <div style="clear: both;"></div>
	</div> -->
	<!-- End of galleriffic -->
	
	<!--  ad-gallery -->
<div id="container">
	<div id="gallery" class="ad-gallery">
      <div class="ad-image-wrapper" style="display:none">&#160;</div>
      <div class="ad-controls">&#160;</div>
      <div class="ad-nav">
        <div class="ad-thumbs">
          <ul class="ad-thumb-list" >
          <c:forEach var="image" items="${taxon.images}" varStatus="status">
            <li>
              <a href="${image.url}">
                <img src="${image.url}" class="${status.index}" onclick="javascript:location.href='/portal/image/${image.identifier}';"/>
              </a>
            </li>
            </c:forEach>
           </ul>
          </div> 
		</div>
	</div>
</div>
	
	

	</div>
</jsp:root>