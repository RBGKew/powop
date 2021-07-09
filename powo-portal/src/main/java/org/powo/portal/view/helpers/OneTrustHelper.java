package org.powo.portal.view.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.powo.site.Site;
import com.github.jknack.handlebars.Handlebars;
import com.google.common.base.Strings;
import org.springframework.web.bind.annotation.ModelAttribute;

public class OneTrustHelper {

	@Value("${environment.type:#{null}}")
	String type;
  Site site;

  @ModelAttribute("oneTrustID")
  protected String oneTrustID() {
    return site.oneTrustID();
  }

	public CharSequence oneTrustScript() {
		if(type == "prod") {
			return "";
		} else if(type == "uat") {
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
