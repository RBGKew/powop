package org.powo.portal.view.helpers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.github.jknack.handlebars.Options;
import com.google.common.base.Optional;

@Component
public class AssetHelper {

	private static final Logger log = LoggerFactory.getLogger(AssetHelper.class);
	private static final Resource resource = new ClassPathResource("rev-manifest.json");
	private static Optional<JsonObject> manifest;

	private boolean developmentMode;
	private Clock clock;

	static {
		try {
			manifest = Optional.of(Json.parse(new InputStreamReader(resource.getInputStream())).asObject());
			log.debug(manifest.get().toString());
		} catch (IOException e) {
			log.error("Error initializing AssetHelper: could not find {}", resource.getFilename());
		}
	}

	public AssetHelper(@Value("${development.mode:false}") boolean developmentMode, Clock clock) {
		this.developmentMode = developmentMode;
		this.clock = clock;
	}

	public CharSequence asset(String name, Options options) {
		if (developmentMode) {
			return name + "?t=" + clock.millis();
		}
		if(manifest.isPresent()) {
			return manifest.get().getString(name, name);
		}

		log.warn("Couldn't find asset reved asset path for {}", name);
		return name;
	}
}
