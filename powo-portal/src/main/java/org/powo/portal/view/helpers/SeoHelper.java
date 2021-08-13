package org.powo.portal.view.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.powo.site.Site;
import com.github.jknack.handlebars.Handlebars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SeoHelper {

	@Value("${environment.type:dev}")
	private String environment;

	@Autowired
	@Qualifier("currentSite")
	Site site;

	private String typeProd = "prod";
	private String typeUAT = "uat";

	public CharSequence seoNoIndexTag() {
		  if(environment.equals(typeProd)) {
          return "";
      } else if(environment.equals(typeUAT)) {
        return new Handlebars.SafeString(
          "<meta name='robots' content='noindex'>");
      } else	{
        return "";
      }
    }
  }
