package org.powo.model.helpers;

import java.net.URI;
import java.util.Arrays;

import org.powo.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * Helper for generating web links from image records. To use the Digifolia CDN,
 * set the prefix to the relevant s3 bucket and key to the Digifolia shared key.
 * Only images with identifers prefixed with urn:kew.org:dam will have CDN links
 * generated. Otherwise, the _thumbnail.jpg/_fullsize.jpg naming scheme is used.
 * 
 * @param key Shared key for generating asset hashes
 * @param prefix CDN s3 bucket prefix
 *
 */
@Component
public class CDNImageHelper {

	private String CDNKey;
	private String CDNPrefix;

	private String[] secureDomains;

	private Logger logger = LoggerFactory.getLogger(CDNImageHelper.class);

	public CDNImageHelper(@Value("${cdn.key}") String key, @Value("${cdn.prefix}") String prefix, @Value("${images.securedomains}") String[] secureDomains) {
		this.CDNKey = key;
		this.CDNPrefix = prefix;
		this.secureDomains = secureDomains;
	}

	public String getThumbnailUrl(Image img) {
		if (hasCDNImage(img)) {
			return getCDNUrl(img, 400);
		} else {
			return getSecureUrl(img, "thumbnail");
		}
	}

	public String getFullsizeUrl(Image img) {
		if (hasCDNImage(img)) {
			return getCDNUrl(img, 1600);
		} else {
			return getSecureUrl(img, "fullsize");
		}
	}

	public boolean hasCDNImage(Image img) {
		return img.getIdentifier().startsWith("urn:kew.org:dam:");
	}

	public String getCDNUrl(Image img, int size) {
		var id = img.getIdentifier().substring(img.getIdentifier().lastIndexOf(':') + 1, img.getIdentifier().length());

		return String.format("%s/%s.jpg",
				CDNPrefix,
				DigestUtils.md5DigestAsHex((id + "-" + size + "-" + CDNKey).getBytes()));
	}

	/**
	 * Convert an HTTP URL to a "//" URL, if the domain matches a known list of
	 * CDNs. This will cause the browser to use the protocol that matches the site
	 * URL. Therefore, it will use HTTPS if the site was loaded over HTTPS.
	 */
	private String getSecureUrl(Image img, String size) {
		var url = String.format("%s_%s.jpg", img.getAccessUri(), size);
		try {
			var uri = URI.create(url);
			boolean hasSecureDomain = Arrays.stream(secureDomains).anyMatch(d -> uri.getHost().contains(d));
			if (!hasSecureDomain) {
				return uri.toString();
			}
			return "//" + uri.getAuthority() + uri.getPath();
		} catch (IllegalArgumentException e) {
			logger.error("Error getting secure URL from " + url + ": " + e.getMessage());
		}
		return url;
	}
}
