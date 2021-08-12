package org.powo.portal.view.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.powo.site.Site;
import com.github.jknack.handlebars.Handlebars;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AnalyticsHelper {

	@Value("${google.analytics.code:#{null}}")
	String code;

	@Value("${environment.type:dev}")
	String type;

	@Autowired
	@Qualifier("currentSite")
	Site site;

	String typeProd = "prod";
	String typeUAT = "uat";

	public CharSequence gaNoScript() {
		if(type.equals(typeProd)) {
			return new Handlebars.SafeString(
				"<noscript><iframe src='//www.googletagmanager.com/ns.html?id=" + code + "' " +
				"height='0' width='0' style='display:none;visibility:hidden'></iframe></noscript>");
		} else if(type.equals(typeUAT)) {
			return new Handlebars.SafeString(
				"<noscript><iframe src='//www.googletagmanager.com/ns.html?id=" + code + "' " +
				"height='0' width='0' style='display:none;visibility:hidden'></iframe></noscript>");
		} else	{
			return "";
		}
	}

	public CharSequence gaTracking() {
		if(Strings.isNullOrEmpty(type)) {
			return "";
		} else if(type.equals(typeProd)) {
			return new Handlebars.SafeString(
				"<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':" +
				"new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0]," +
				"j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=" +
				"'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);" +
				"})(window,document,'script','dataLayer','" + code + "');</script>");
		} else if(type.equals(typeUAT)) {
			return new Handlebars.SafeString(
				"<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':" +
				"new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0]," +
				"j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=" +
				"'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);" +
				"})(window,document,'script','dataLayer','" + code + "');</script>");
		} else	{
			return "";
		}
	}
}