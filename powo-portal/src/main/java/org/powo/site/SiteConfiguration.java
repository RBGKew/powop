package org.powo.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteConfiguration {

	@Autowired
	ApplicationContext ctx;

	@Bean
	public Site currentSite(@Value("${site.variant}") String siteVariant) {
		return (Site) ctx.getBean(siteVariant);
	}

}
