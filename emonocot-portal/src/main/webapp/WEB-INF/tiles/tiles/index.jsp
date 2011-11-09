<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<style type="text/css">
      body {
        background-color: #ffffff;
      }
    </style>
	<!-- <div class="row">
		<div class="twelvecol">
			<div class="content-wrapper">
				<br />
				<div class="row">
					<div class="tencol"> 
						<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
							Aenean commodo ligula eget dolor. Aenean massa. Cum sociis
							natoque penatibus et magnis dis parturient montes, nascetur
							ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu,
							pretium quis, sem. Nulla consequat massa quis enim.</p>

						<p>Donec pede justo, fringilla vel, aliquet nec, vulputate
							eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis
							vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer
							tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean
							vulputate eleifend tellus. Aenean leo ligula, porttitor eu,
							consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus
							in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut
							metus varius laoreet. Quisque rutrum.</p>
					</div>
				</div>

				<br />

				<form action="search" id="search form" method="GET">
					<div style="margin: 0; padding: 0; display: inline"></div>
					<input class="oversize input-text" id="emonocot-search"
						name="query" size="40" type="text" value="" />
					<button type="submit" class="super large gray button">
						<span>SEARCH</span>
					</button>
					<small id="hint">Search for... </small>
				</form>
				<br /> <span class="shadow"></span>
			</div>
		</div>
	</div>
	<div class="steps"></div>
	-->
	

	<!-- Main hero unit for a primary marketing message or call to action -->
		<div class="hero-unit">
    		<h2>A global online resource for monocot plants.  Discover a wealth of information about monocot families, genera and species.</h2>
        	<p>Monocots represent  approx 20% of all flowering plants (70,000 species). They dominate significant parts of the world ecosystems, and contain numerous groups of the highest conservation, ecological and economic importance. They include the staple grass food crops (wheat, barley, rice and maize) and other important food plants such as onions, palms, yams, bananas and gingers. Overall they provide 75% of human nutrition.</p>
        	<br/>
     		<!-- <form action="search" id="search form" method="GET"> -->
     		<form action="search" method="GET">
					<div style="margin: 0; padding: 0; display: inline">&#160;</div>
					<!-- <input class="oversize input-text" id="emonocot-search" name="query" size="40" type="text" value="" /> -->
					<input name="query" size="40" type="text" value="" />
					<button type="submit" class="btn">
						<span>SEARCH</span>
					</button>
					<br/>
					<small id="hint">Search for... </small>
				</form>
        	<br/>
		</div>
	
		<!-- Example row of columns -->
    	<div class="row">
        	<div class="span-one-third">
          		<h2>Identify</h2>
          		<p>Use tools to identify monocot plants including interactive Keys and image galleries.</p>
        	</div>
        	<div class="span-one-third">
          		<h2>Classify</h2>
           		<p>Browse a checklist and check the accepted name of a species.</p>
       		</div>
        	<div class="span-one-third">
          		<h2>Explore</h2>
          		<p>Browse the Family, genera and species pages. Investigate the content within eMonocot.</p>
        	</div>
      	</div>
		<br/>   

</jsp:root>