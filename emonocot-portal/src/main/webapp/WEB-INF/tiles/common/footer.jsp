<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  version="2.0">
  <jsp:element name="script">
    <jsp:attribute name="type">text/javascript</jsp:attribute>
    <jsp:attribute name="src">
      <c:url value="/js/footer.js"/>
    </jsp:attribute>
    /* */
  </jsp:element>
  
  <!-- <script type="text/javascript">
		$(document).ready(function() {
			$(".thumb").thumbs();
		});
  </script>
  <script type="text/javascript">
  $(function() {
    var galleries = $('.ad-gallery').adGallery();
    $('#switch-effect').change(
      function() {
        galleries[0].settings.effect = $(this).val();
        return false;
      }
    );
    $('#toggle-slideshow').click(
      function() {
        galleries[0].slideshow.toggle();
        return false;
      }
    );
    $('#toggle-description').click(
      function() {
        if(!galleries[0].settings.description_wrapper) {
          galleries[0].settings.description_wrapper = $('#descriptions');
        } else {
          galleries[0].settings.description_wrapper = false;
        }
        return false;
      }
    );
  });
  </script>
   -->
	      
</jsp:root>