package org.powo.portal.view.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.powo.site.Site;
import com.github.jknack.handlebars.Handlebars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class OneTrustHelper {

	@Value("${environment.type:dev}")
	private String environment;

	@Autowired
	@Qualifier("currentSite")
	Site site;

	private String typeProd = "prod";
	private String typeUAT = "uat";

	protected String oneTrustID() {
		return site.oneTrustID();
	}

	public CharSequence oneTrustScript() {
		if(environment.equals(typeProd)) {
			return "";
		} else if(environment.equals(typeUAT)) {
			return new Handlebars.SafeString(
				"<script src='https://cdn.cookielaw.org/scripttemplates/otSDKStub.js'  type='text/javascript' charset='UTF-8' data-domain-script='" + oneTrustID() + "-test' ></script>" +
				"<script type='text/javascript'>" +
				"function OptanonWrapper() { }" +
				"</script>");
		} else {
			return "";
		}
	}
}
