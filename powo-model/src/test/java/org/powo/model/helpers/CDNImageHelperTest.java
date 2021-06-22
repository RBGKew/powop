package org.powo.model.helpers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powo.model.Image;

public class CDNImageHelperTest {
  
  private String testKey = "test_cdn_key";
  private String testPrefix = "//testcdn.com/powo";
  private String[] secureDomains = {"cloudfront.net", "googleapis.com"};

  @Test
  /**
   * Test that the URL protocol is not changed if a URL is invalid.
   */
  public void testGetInvalidUrls() {
    var helper = new CDNImageHelper(testKey, testPrefix, secureDomains);

    var image = new Image();
    image.setIdentifier("urn:not-a-cdn.com:1");
    image.setAccessUri("http://example.com/this is an invalid url");

    var thumbnailUrl = helper.getThumbnailUrl(image);
    var fullsizeUrl = helper.getFullsizeUrl(image);
    assertEquals("http://example.com/this is an invalid url_thumbnail.jpg", thumbnailUrl);
    assertEquals("http://example.com/this is an invalid url_fullsize.jpg", fullsizeUrl);
  }

  @Test
  /**
   * Test that the URL protocol is not changed if a URL uses a domain that isn't in the whitelisted
   * CDNImageHelper.secureDomains array.
   */
  public void testGetInsecureUrls() {
    var helper = new CDNImageHelper(testKey, testPrefix, secureDomains);

    var image = new Image();
    image.setIdentifier("urn:not-a-cdn.com:1");
    image.setAccessUri("http://not-a-cdn.com/plant");

    var thumbnailUrl = helper.getThumbnailUrl(image);
    var fullsizeUrl = helper.getFullsizeUrl(image);
    assertEquals("http://not-a-cdn.com/plant_thumbnail.jpg", thumbnailUrl);
    assertEquals("http://not-a-cdn.com/plant_fullsize.jpg", fullsizeUrl);
  }

  @Test
  /**
   * Test that the URL protocol is removed (to leave "//") if a URL uses a domain that
   * IS in the whitelisted secureDomains array.
   */
  public void testGetGoogleCloudUrl() {
    var helper = new CDNImageHelper(testKey, testPrefix, secureDomains);

    var image = new Image();
    image.setIdentifier("urn:kew.org:fwta:media:9");
    image.setAccessUri("http://storage.googleapis.com/powop-assets/fwta/v1p1/033");

    var thumbnailUrl = helper.getThumbnailUrl(image);
    var fullsizeUrl = helper.getFullsizeUrl(image);
    assertEquals("//storage.googleapis.com/powop-assets/fwta/v1p1/033_thumbnail.jpg", thumbnailUrl);
    assertEquals("//storage.googleapis.com/powop-assets/fwta/v1p1/033_fullsize.jpg", fullsizeUrl);
  }

  @Test
  /**
   * Test that CDN urls are given a blank protocol ("//"), to allow the browser to
   * choose which protocol to use when accessing the image.
   */
  public void testGetCDNUrl() {
    var helper = new CDNImageHelper(testKey, testPrefix, secureDomains);

    var image = new Image();
    image.setIdentifier("urn:kew.org:dam:595377");
    image.setAccessUri("p/medicore/asset/thumb/595377");

    var thumbnailUrl = helper.getThumbnailUrl(image);
    var fullsizeUrl = helper.getFullsizeUrl(image);
    // MD5 hash of "595377-400-test_cdn_key"
    assertEquals("//testcdn.com/powo/c7f13a5a80759296d7e615a27c2691c7.jpg", thumbnailUrl);
    // MD5 hash of "595377-1600-test_cdn_key"
    assertEquals("//testcdn.com/powo/5a94756062158da7a62a24e2666ecd17.jpg", fullsizeUrl);
  }

}
