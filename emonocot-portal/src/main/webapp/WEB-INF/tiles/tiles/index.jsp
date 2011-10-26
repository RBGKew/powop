<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<div class="row">
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

</jsp:root>