package org.powo.portal.view.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.powo.site.Site;
import com.github.jknack.handlebars.Handlebars;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class NoIndexHelper {

	@Value("${environment.type:#{null}}")
	String type;

	@Autowired
	@Qualifier("currentSite")
	Site site;

	String typeProd = "prod";
	String typeUAT = "uat";

	public CharSequence noIndexScript() {
		if(Strings.isNullOrEmpty(type)) {
        return "";
      } else if(type.equals(typeProd)) {
          return "";
      } else if(type.equals(typeUAT)) {
        return new Handlebars.SafeString(
          "<meta name='robots' content='noindex'>");
      } else	{
        return "";
      }
    }
  }
