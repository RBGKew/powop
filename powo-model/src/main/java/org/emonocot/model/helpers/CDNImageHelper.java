package org.emonocot.model.helpers;

import org.emonocot.model.Image;
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

	public CDNImageHelper(@Value("${cdn.key}") String key, @Value("${cdn.prefix}") String prefix) {
		this.CDNKey = key;
		this.CDNPrefix = prefix;
	}

	public String getThumbnailUrl(Image img) {
		if(hasCDNImage(img)) {
			return getCDNUrl(img, 400);
		} else {
			return String.format("%s_thumbnail.jpg", img.getAccessUri());
		}
	}

	public String getFullsizeUrl(Image img) {
		if(hasCDNImage(img)) {
			return getCDNUrl(img, 1600);
		} else {
			return String.format("%s_fullsize.jpg", img.getAccessUri());
		}
	}

	public boolean hasCDNImage(Image img) {
		return img.getIdentifier().startsWith("urn:kew.org:dam:");
	}

	public String getCDNUrl(Image img, int size) {
		int id = Integer.parseInt(img.getIdentifier().substring(
				img.getIdentifier().lastIndexOf(':') + 1,
				img.getIdentifier().length()));

		return String.format("%s/%s.jpg",
				CDNPrefix,
				DigestUtils.md5DigestAsHex((id + "-" + size + "-" + CDNKey).getBytes()));
	}
}
