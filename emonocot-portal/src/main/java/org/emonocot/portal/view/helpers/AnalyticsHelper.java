package org.emonocot.portal.view.helpers;

import org.springframework.beans.factory.annotation.Value;

import com.github.jknack.handlebars.Handlebars;
import com.google.common.base.Strings;

public class AnalyticsHelper {

	@Value("${google.analytics.code:#{null}}")
	String code;

	public CharSequence gaTracking() {
		if(Strings.isNullOrEmpty(code)) {
			return "";
		} else {
			return new Handlebars.SafeString(
					"<script>dataLayer = [{'siteSection': 'PlantsoftheWorldOnline', 'commercial': 'No',}];</script>" +
					"<noscript><iframe src='//www.googletagmanager.com/ns.html?id=" + code + "'" +
					"height='0' width='0' style='display:none;visibility:hidden'></iframe></noscript>" +
					"<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':" +
					"new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0]," +
					"j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=" +
					"'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);" +
					"})(window,document,'script','dataLayer','" + code + "');</script>");
		}
	}
}
