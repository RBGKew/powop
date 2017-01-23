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
			return new Handlebars.SafeString("<script>" +
					"(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){" +
					"(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o)," +
					"m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)" +
					"})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');" +
					"ga('create', '"+ code +"', 'auto');" +
					"ga('send', 'pageview');" +
					"</script>");
		}
	}
}
