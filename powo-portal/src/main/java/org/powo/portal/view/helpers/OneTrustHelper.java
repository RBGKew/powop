package org.powo.portal.view.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.powo.site.Site;
import com.github.jknack.handlebars.Handlebars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.google.common.base.Strings;

public class OneTrustHelper {
  
  @Value("${environment.type:#{null}}")
	String type;

  @Autowired
  @Qualifier("currentSite")
  Site site;

  String typeProd = "prod";
  String typeUAT = "uat";

  protected String oneTrustID() {
    return site.oneTrustID();
  }

	public CharSequence oneTrustScript() {
    if(Strings.isNullOrEmpty(type)) {
			return "";
		}
		else if(type.equals(typeProd)) {
			return "";
		} else if(type.equals(typeUAT)) {
			return new Handlebars.SafeString(
					"<script src='https://cdn.cookielaw.org/scripttemplates/otSDKStub.js'  type='text/javascript' charset='UTF-8' data-domain-script='" + oneTrustID() + "-test' ></script>" +
					"<script type='text/javascript'>" +
          "function OptanonWrapper() { }" +
          "</script>");
		}
    else {
      return "";
    }
	}
}
