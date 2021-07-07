package org.powo.portal.view.helpers;

import org.springframework.beans.factory.annotation.Value;

import com.github.jknack.handlebars.Handlebars;
import com.google.common.base.Strings;

public class OneTrustHelper {

	@Value("${onetrust.code:#{null}}")
  String code;

	public CharSequence gaNoScript() {
		if(Strings.isNullOrEmpty(code)) {
			return new Handlebars.SafeString(
				 "<script src='https://cdn.cookielaw.org/scripttemplates/otSDKStub.js'  type='text/javascript' charset='UTF-8' data-domain-script='" + code + "' ></script>" +
				 "<script type='text/javascript'>" +
				 "function OptanonWrapper() { }" +
					"</script>");
		} else {
			return "";
		}
	}
}




