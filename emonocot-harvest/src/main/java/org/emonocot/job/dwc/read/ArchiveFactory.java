package org.emonocot.job.dwc.read;

import org.emonocot.api.job.TermFactory;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveField.DataType;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.UnsupportedArchiveException;
import org.gbif.file.CSVReader;
import org.gbif.file.CSVReaderFactory;
import org.gbif.file.DownloadUtil;
import org.gbif.metadata.handler.BasicMetadataSaxHandler;
import org.gbif.utils.file.BomSafeInputStreamWrapper;
import org.gbif.utils.file.CompressionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ArchiveFactory {

  /**
   * SAX handler to parse a meta.xml descriptor for dwc archives. It populates a given archive instance and ignores
   * namespaces. The parser needs to be namespace aware!
   */
  static class MetaHandler extends BasicMetadataSaxHandler {

    private static final String NS_DWCA = "http://rs.tdwg.org/dwc/text/";
    private Archive archive;
    private ArchiveFile af;

    protected MetaHandler(Archive archive) {
      this.archive = archive;
    }

    private static Character getFirstChar(String x) throws UnsupportedArchiveException {
      if (x == null || x.length() == 0) {
        return null;
      }
      if (x.length() == 1) {
        return x.charAt(0);
      }
      if (x.equalsIgnoreCase("\\t")) {
        return '\t';
      }
      if (x.equalsIgnoreCase("\\n")) {
        return '\n';
      }
      if (x.equalsIgnoreCase("\\r")) {
        return '\r';
      }
      if (x.length() > 1) {
        throw new UnsupportedArchiveException(
          "Only darwin core archives with a single quotation character are supported, but found >>>" + x + "<<<");
      }
      return ' ';
    }

    private static void makeLocationPathsAbsolute(ArchiveFile af, File root) {
      // I know this is verbose and stupid, but its easy coded now without the hassle of deep copying lists, etc...
      List<String> newLocs = new ArrayList<String>();
      for (String loc : af.getLocations()) {
        newLocs.add(new File(root, af.getLocation()).getAbsolutePath());
      }
      af.getLocations().clear();
      for (String loc : newLocs) {
        af.getLocations().add(loc);
      }
    }

    private static String unescapeBackslash(String x) {
      if (x == null || x.length() == 0) {
        return null;
      }
      return x.replaceAll("\\\\t", String.valueOf('\t')).replaceAll("\\\\n", String.valueOf('\n'))
        .replaceAll("\\\\r", String.valueOf('\r')).replaceAll("\\\\f", String.valueOf('\f'));
    }

    private ArchiveFile buildArchiveFile(Attributes attr) throws UnsupportedArchiveException {
      ArchiveFile dwcFile = new ArchiveFile();

      // extract the File attributes
      if (getAttr(attr, "encoding") != null) {
        dwcFile.setEncoding(getAttr(attr, "encoding"));
      }
      if (getAttr(attr, "fieldsTerminatedBy") != null) {
        dwcFile.setFieldsTerminatedBy(unescapeBackslash(getAttr(attr, "fieldsTerminatedBy")));
      }
      if (getAttr(attr, "fieldsEnclosedBy") != null) {
        dwcFile.setFieldsEnclosedBy(getFirstChar(getAttr(attr, "fieldsEnclosedBy")));
      }
      if (getAttr(attr, "linesTerminatedBy") != null) {
        dwcFile.setLinesTerminatedBy(unescapeBackslash(getAttr(attr, "linesTerminatedBy")));
      }
      if (getAttr(attr, "rowType") != null) {
        dwcFile.setRowType(getAttr(attr, "rowType"));
      }
      String ignoreHeaderLines = getAttr(attr, "ignoreHeaderLines");
      try {
        dwcFile.setIgnoreHeaderLines(Integer.parseInt(ignoreHeaderLines));
      } catch (NumberFormatException ignored) { // swallow null or bad value
      }
      return dwcFile;
    }

    /**
     * Build an ArchiveField object based on xml attributes.
     */
    private ArchiveField buildField(Attributes attributes) {
      // build field
      ConceptTerm term = TermFactory.findTerm(getAttr(attributes, "term"));
      String defaultValue = getAttr(attributes, "default");
      DataType type = DataType.findByXmlSchemaType(getAttr(attributes, "type"));
      if (type == null) {
        type = DataType.string;
      }
      String indexAsString = getAttr(attributes, "index");
      Integer index = null;
      if (indexAsString != null) {
        // let bad errors be thrown up
        try {
          index = Integer.parseInt(indexAsString);
        } catch (NumberFormatException e) {
          throw new UnsupportedArchiveException(e);
        }
      }
      return new ArchiveField(index, term, defaultValue, type);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      // calling the super method to stringify the character buffer
      super.endElement(uri, localName, qName);

      if (localName.equalsIgnoreCase("archive")) {
        // archive
      } else if (localName.equalsIgnoreCase("core")) {
        // update location to absolute path incl archive path
//      makeLocationPathsAbsolute(af, archive.getLocation());
        archive.setCore(af);
      } else if (localName.equalsIgnoreCase("extension")) {
        // update location to absolute path incl archive path
//      makeLocationPathsAbsolute(af, archive.getLocation());
        archive.addExtension(af);
      } else if (localName.equalsIgnoreCase("location")) {
        // a file location
        af.addLocation(content);
      }

    }

    private String getAttr(Attributes attributes, String key) {
      String val = null;
      if (attributes != null) {
        // try without NS
        val = attributes.getValue("", key);
        if (val == null) {
          // try with dwca NS if nothing found
          val = attributes.getValue(NS_DWCA, key);
        }
      }
      return val;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      super.startElement(uri, localName, qName, attributes);
      if (localName.equalsIgnoreCase("archive") || localName.equalsIgnoreCase("stararchive")) {
        // metadata location
        archive.setMetadataLocation(getAttr(attributes, "metadata"));
      } else if (localName.equalsIgnoreCase("core") || localName.equalsIgnoreCase("extension")) {
        // archive/extension
        af = new ArchiveFile();
        if (localName.equalsIgnoreCase("core") || localName.equalsIgnoreCase("extension")) {
          // archive/core or archive/extension
          af = buildArchiveFile(attributes);
        }
      } else if (localName.equalsIgnoreCase("coreid") || localName.equalsIgnoreCase("id")) {
        ArchiveField field = buildField(attributes);
        if (af != null) {
          af.setId(field);
        } else {
          log.warn(localName + " field found outside of an archive file");
        }
      } else if (localName.equalsIgnoreCase("field")) {
        ArchiveField field = buildField(attributes);
        if (af != null) {
          af.addField(field);
        } else {
          log.warn("field found outside of an archive file");
        }
      }
    }

  }

  private static final TermFactory TERM_FACTORY = new TermFactory();

  private static final Logger LOG = LoggerFactory.getLogger(ArchiveFactory.class);

  private static final SAXParserFactory SAX_FACTORY = SAXParserFactory.newInstance();

  static {
    SAX_FACTORY.setNamespaceAware(true);
    SAX_FACTORY.setValidating(false);
  }

  /**
   * Opens an archive from a URL, downloading and decompressing it.
   *
   * @param archiveUrl the location of a compressed archive or single data file
   * @param workingDir writable directory to download to and decompress archive
   */
  public static Archive openArchive(URL archiveUrl, File workingDir) throws IOException, UnsupportedArchiveException {
    File downloadTo = new File(workingDir, "dwca-download");
    File dwca = new File(workingDir, "dwca");
    DownloadUtil.download(archiveUrl, downloadTo);
    return openArchive(downloadTo, dwca);
  }

  /**
   * Opens an archive from a local file and decompresses or copies it into the given archive directory.
   * Make sure the archive directory does not contain files already!
   *
   * @param archiveFile the location of a compressed archive or single data file
   * @param archiveDir  empty, writable directory used to keep decompress archive in
   */
  public static Archive openArchive(File archiveFile, File archiveDir) throws IOException, UnsupportedArchiveException {
    // try to decompress archive
    try {
      List<File> files = CompressionUtil.decompressFile(archiveDir, archiveFile);
      // continue to read archive from the tmp dir
      return openArchive(archiveDir);

    } catch (CompressionUtil.UnsupportedCompressionType e) {
      // If its a text file only we will get this exception - but also for corrupt compressions
      // try to open as text file only
      return openArchive(archiveFile);
    }
  }

  /**
   * @param unzippedFolderLocation the location of an expanded archive directory or just a single dwc text file
   */
  public static Archive openArchive(File unzippedFolderLocation) throws IOException, UnsupportedArchiveException {
    Archive archive = new Archive();
    archive.setLocation(unzippedFolderLocation);

    File mf = null;
    // see if we can find a meta.xml descriptor file
    if (unzippedFolderLocation.isFile()) {
      String suffix = unzippedFolderLocation.getName().substring(unzippedFolderLocation.getName().lastIndexOf("."));
      if (suffix.equalsIgnoreCase(".xml")) {
        // could be a metafile on its own pointing to remote data files...
        mf = unzippedFolderLocation;
      }
    } else {
      mf = new File(unzippedFolderLocation, "meta.xml");
    }
    // read metadata
    if (mf != null && mf.exists()) {
      // read metafile
      readMetaDescriptor(archive, new FileInputStream(mf), true);
      if (archive.getMetadataLocation() == null) {
        // search for known metadata filenames
        File emlFile = new File(mf.getParentFile(), "eml.xml");
        if (emlFile.exists()) {
          archive.setMetadataLocation("eml.xml");
        }
      }
    } else {
      // try to detect data files ourselves as best as we can...
      // currently support a single data file or a folder which contains a single data file
      if (unzippedFolderLocation.isFile()) {
        ArchiveFile coreFile = readFileHeaders(unzippedFolderLocation);
        archive.setCore(coreFile);
      } else {
        // folder. see if we got only 1 file in there...
        List<File> dataFiles = new ArrayList<File>();
        FilenameFilter ff = new SuffixFileFilter(".csv", IOCase.INSENSITIVE);
        dataFiles.addAll(Arrays.asList(unzippedFolderLocation.listFiles(ff)));
        ff = new SuffixFileFilter(".txt", IOCase.INSENSITIVE);
        dataFiles.addAll(Arrays.asList(unzippedFolderLocation.listFiles(ff)));

        if (dataFiles.size() == 1) {
          // set pointer to data file
          File dataFile = new File(unzippedFolderLocation, dataFiles.get(0).getName());
          archive.setLocation(unzippedFolderLocation);
          if (archive.getMetadataLocation() == null && unzippedFolderLocation.isDirectory()) {
            // search for known metadata filenames
            File emlFile = new File(unzippedFolderLocation, "eml.xml");
            if (emlFile.exists()) {
              archive.setMetadataLocation("eml.xml");
            }
          }
          ArchiveFile coreFile = readFileHeaders(dataFile);
          coreFile.getLocations().clear();
          coreFile.addLocation(dataFile.getName());
          archive.setCore(coreFile);
        } else {
          throw new UnsupportedArchiveException(
            "The archive given is a folder with more or less than 1 data files having a txt or csv suffix");
        }
      }
    }
    // final validation
    validateArchive(archive);
    // report basic stats
    LOG.debug("Archive contains " + archive.getExtensions().size() + " described extension files");
    LOG.debug("Archive contains " + archive.getCore().getFields().size() + " core properties");
    return archive;
  }

  /**
   * Use internal term factory to find/build a new ConceptTerm based on its qualified name.
   *
   * @param termName the qualified term name
   *
   * @return the ConceptTerm either as one of the existing enums or an UnknownTerm singleton
   */
  public static ConceptTerm findTerm(String termName) {
    return TERM_FACTORY.findTerm(termName);
  }

  private static ArchiveFile readFileHeaders(File dataFile) throws UnsupportedArchiveException, IOException {
    ArchiveFile dwcFile = new ArchiveFile();
    dwcFile.addLocation(null);
    dwcFile.setIgnoreHeaderLines(1);

    CSVReader reader = CSVReaderFactory.build(dataFile);

    // copy found delimiters & encoding
    dwcFile.setEncoding(reader.encoding);
    dwcFile.setFieldsTerminatedBy(reader.delimiter);
    dwcFile.setFieldsEnclosedBy(reader.quoteChar);

    // detect dwc terms as good as we can based on header row
    String[] headers = reader.header;
    int index = 0;
    for (String head : headers) {
      // there are never any quotes in term names - remove them just in case the csvreader didnt recognize them
      if (head != null && head.length() > 1) {
        ConceptTerm dt = TERM_FACTORY.findTerm(head);
        if (dt != null) {
          ArchiveField field = new ArchiveField(index, dt, null, DataType.string);
          if (dwcFile.getId() == null &&
              (dt.equals(DwcTerm.occurrenceID) || dt.equals(DwcTerm.taxonID) || dt.equals(DcTerm.identifier))) {
            dwcFile.setId(field);
          } else {
            dwcFile.addField(field);
          }
        }
      }
      index++;
    }

    return dwcFile;
  }

  private static void readMetaDescriptor(Archive archive, InputStream metaDescriptor, boolean normaliseTerms)
    throws UnsupportedArchiveException {

    try {
      SAXParser p = SAX_FACTORY.newSAXParser();
      MetaHandler mh = new MetaHandler(archive);
      LOG.debug("Reading archive metadata file");
//    p.parse(metaDescriptor, mh);
      p.parse(new BomSafeInputStreamWrapper(metaDescriptor), mh);
    } catch (Exception e1) {
      LOG.warn("Exception caught", e1);
      throw new UnsupportedArchiveException(e1);
    }
  }

  private static void validateArchive(Archive archive) throws UnsupportedArchiveException {
    validateCoreFile(archive.getCore(), !archive.getExtensions().isEmpty());
    for (ArchiveFile af : archive.getExtensions()) {
      validateExtensionFile(af);
    }
  }

  private static void validateCoreFile(ArchiveFile f, boolean hasExtensions) throws UnsupportedArchiveException {
    if (hasExtensions) {
      if (f.getId() == null) {
        LOG.warn(
          "DwC-A core data file " + f.getTitle() + " is lacking an id column. No extensions allowed in this case");
      }
    }
    validateFile(f);
  }

  private static void validateExtensionFile(ArchiveFile f) throws UnsupportedArchiveException {
    if (f.getId() == null) {
      throw new UnsupportedArchiveException(
        "DwC-A data file " + f.getTitle() + " requires an id or foreign key to the core id");
    }
    validateFile(f);
  }

  private static void validateFile(ArchiveFile f) throws UnsupportedArchiveException {
    if (f == null) {
      throw new UnsupportedArchiveException("DwC-A data file is NULL");
    }
    if (f.getLocationFile() == null) {
      throw new UnsupportedArchiveException("DwC-A data file " + f.getTitle() + " requires a location");
    }
    if (f.getEncoding() == null) {
      throw new UnsupportedArchiveException("DwC-A data file " + f.getTitle() + " requires a character encoding");
    }

  }

}
