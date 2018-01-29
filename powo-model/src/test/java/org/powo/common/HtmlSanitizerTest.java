/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.powo.common;

import static org.junit.Assert.assertEquals;
import static org.powo.common.HtmlSanitizer.sanitize;
import static org.powo.common.HtmlSanitizer.strip;

import org.junit.Test;

public class HtmlSanitizerTest {

	@Test
	public void testInvalidMarkup() {
		assertEquals("<p>Local Aguaruna name: magkamak, katípas. <br> <br> Croat, Swart &amp; Yates 2005: 94</p>",
				sanitize("<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" /> <meta content=\"Word.Document\" name=\"ProgId\" /> <meta content=\"Microsoft Word 11\" name=\"Generator\" /> <meta content=\"Microsoft Word 11\" name=\"Originator\" /> <link href=\"file:///C:\\Users\\simon\\AppData\\Local\\Temp\\msohtml1\\01\\clip_filelist.xml\" rel=\"File-List\" /><!--[if gte mso 9]><xml> <w:WordDocument> <w:View>Normal</w:View> <w:Zoom>0</w:Zoom> <w:PunctuationKerning /> <w:ValidateAgainstSchemas /> <w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid> <w:IgnoreMixedContent>false</w:IgnoreMixedContent> <w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText> <w:Compatibility> <w:BreakWrappedTables /> <w:SnapToGridInCell /> <w:WrapTextWithPunct /> <w:UseAsianBreakRules /> <w:DontGrowAutofit /> </w:Compatibility> <w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel> </w:WordDocument> </xml><![endif]--><!--[if gte mso 9]><xml> <w:LatentStyles DefLockedState=\"false\" LatentStyleCount=\"156\"> </w:LatentStyles> </xml><![endif]--><style type=\"text/css\"> <!--  /* Font Definitions */  @font-face  {font-family:TimesNewRoman;  panose-1:0 0 0 0 0 0 0 0 0 0;  mso-font-charset:0;  mso-generic-font-family:roman;  mso-font-format:other;  mso-font-pitch:auto;  mso-font-signature:3 0 0 0 1 0;} @font-face  {font-family:\"TimesNewRoman\\,Bold\";  panose-1:0 0 0 0 0 0 0 0 0 0;  mso-font-charset:0;  mso-generic-font-family:roman;  mso-font-format:other;  mso-font-pitch:auto;  mso-font-signature:3 0 0 0 1 0;}  /* Style Definitions */  p.MsoNormal, li.MsoNormal, div.MsoNormal  {mso-style-parent:\"\";  margin:0cm;  margin-bottom:.0001pt;  mso-pagination:widow-orphan;  font-size:12.0pt;  font-family:\"Times New Roman\";  mso-fareast-font-family:\"Times New Roman\";} @page Section1  {size:612.0pt 792.0pt;  margin:72.0pt 90.0pt 72.0pt 90.0pt;  mso-header-margin:36.0pt;  mso-footer-margin:36.0pt;  mso-paper-source:0;} div.Section1  {page:Section1;} --> </style><!--[if gte mso 10]> <style> /* Style Definitions */ table.MsoNormalTable {mso-style-name:\"Table Normal\"; mso-tstyle-rowband-size:0; mso-tstyle-colband-size:0; mso-style-noshow:yes; mso-style-parent:\"\"; mso-padding-alt:0cm 5.4pt 0cm 5.4pt; mso-para-margin:0cm; mso-para-margin-bottom:.0001pt; mso-pagination:widow-orphan; font-size:10.0pt; font-family:\"Times New Roman\"; mso-ansi-language:#0400; mso-fareast-language:#0400; mso-bidi-language:#0400;} </style> <![endif]--> <p style=\"\" class=\"MsoNormal\"><span style=\"font-family: &quot;TimesNewRoman,Bold&quot;;\">Local Aguaruna name</span><span style=\"font-family: TimesNewRoman;\">: magkamak, kat&iacute;pas. <br /> <br /> Croat, Swart &amp; Yates 2005: 94<o:p></o:p></span>"));
	}

	@Test
	public void testScratchpadReferenceMarkup() {
		assertEquals("<a href=\"http://blarg.com\" rel=\"nofollow\">Linder, H.P.</a>, 2013. \n<a rel=\"nofollow\">Delta database to Restionaceae</a>",
				sanitize("<div style=\"  text-indent: -25px; padding-left: 25px;\"><span class=\"biblio-authors\"><a href=\"http://blarg.com\">Linder, H.P.</a></span>, 2013. <a href=\"/node/1\"><span class=\"biblio-title\">Delta database to Restionaceae"));
	}

	@Test
	public void testNeotropikeyGlossaryLinksArePreserved() {
		String input = "<strong>Leaves</strong><a href=\"http://www.kew.org/science/tropamerica/neotropikey/families/glossary.htm#Alternate\" title=\"[Leaf arrangement] -- Of leaves or other lateral organs, borne singly at different heights on the axis, when used to describe leaf insertion, often vague and including the more precise spiral and distichous.\"> alternate </a>";
		String result = "<strong>Leaves</strong>\n<a href=\"http://www.kew.org/science/tropamerica/neotropikey/families/glossary.htm#Alternate\" title=\"[Leaf arrangement] -- Of leaves or other lateral organs, borne singly at different heights on the axis, when used to describe leaf insertion, often vague and including the more precise spiral and distichous.\" rel=\"nofollow\"> alternate </a>";
		assertEquals(result, sanitize(input));
	}

	@Test
	public void testControlCharacter() {
		assertEquals("William J. Baker", sanitize("William J. Baker" + ((char) 0)));
		assertEquals("<p>William J. Baker</p>", sanitize("<p>William J. Baker" + ((char) 0) + "</p>"));
		assertEquals("William J. Baker says hi!", sanitize("William J. Baker" + ((char) 0) + " says hi!"));
	}

	@Test
	public void testStripRemovesAllHtml() {
		String input = "<strong>Leaves</strong><a href=\"http://www.kew.org/science/tropamerica/neotropikey/families/glossary.htm#Alternate\" title=\"[Leaf arrangement] -- Of leaves or other lateral organs, borne singly at different heights on the axis, when used to describe leaf insertion, often vague and including the more precise spiral and distichous.\"> alternate </a>";
		assertEquals("Leaves alternate", strip(input));
	}
}
