package org.powo.site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@Configuration
public class SiteConfiguration {

	private static Logger log = LoggerFactory.getLogger(SiteConfiguration.class);

	@Autowired
	ApplicationContext ctx;

	@Bean
	public Site currentSite(@Value("${site.variant}") String siteVariant) {
		log.info("Running site as {} variant", siteVariant);
		Site current = (Site) ctx.getBean(siteVariant);

		return current;
	}

	@Bean LocaleResolver localeResolver(@Value("${site.variant}") String siteVariant) {
		Site current = (Site) ctx.getBean(siteVariant);
		log.info("Using locale: {}", current.defaultLocale());

		return new FixedLocaleResolver(current.defaultLocale());
	}
}
