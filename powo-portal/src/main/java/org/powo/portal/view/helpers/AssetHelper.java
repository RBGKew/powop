package org.powo.portal.view.helpers;

import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.github.jknack.handlebars.Options;
import com.google.common.base.Optional;

public class AssetHelper {

	private static final Logger log = LoggerFactory.getLogger(AssetHelper.class);
	private static final Resource resource = new ClassPathResource("rev-manifest.json");
	private static Optional<JsonObject> manifest;

	@Value("${development.mode:false}")
	private boolean developmentMode;

	static {
		try {
			manifest = Optional.of(Json.parse(new InputStreamReader(resource.getInputStream())).asObject());
			log.debug(manifest.get().toString());
		} catch (IOException e) {
			log.error("Error initializing AssetHelper: could not find {}", resource.getFilename());
		}
	}

	public CharSequence asset(String name, Options options) {
		if(manifest.isPresent() && !developmentMode) {
			return manifest.get().getString(name, name);
		}

		log.warn("Couldn't find asset reved asset path for {}", name);
		return name;
	}
}
