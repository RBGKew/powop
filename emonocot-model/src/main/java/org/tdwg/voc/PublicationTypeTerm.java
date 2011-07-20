package org.tdwg.voc;

import java.net.URI;
import java.net.URISyntaxException;

import org.tdwg.DefinedTerm;

/**
 *
 * @author ben
 *
 */
public class PublicationTypeTerm extends DefinedTerm {
   /**
    *
    */
   private static PublicationTypeTerm[] terms;

   static {
       terms = new PublicationTypeTerm[] {
               PublicationTypeTerm.ARTWORK,
               PublicationTypeTerm.AUDIOVISUAL_MATERIAL,
               PublicationTypeTerm.BOOK,
               PublicationTypeTerm.BOOK_SERIES,
               PublicationTypeTerm.BOOK_SECTION,
               PublicationTypeTerm.CONFERENCE_PROCEEDINGS,
               PublicationTypeTerm.COMMENTRY,
               PublicationTypeTerm.COMPUTER_PROGRAM,
               PublicationTypeTerm.COMMUNICATION,
               PublicationTypeTerm.DETERMINATION,
               PublicationTypeTerm.EDITED_BOOK,
               PublicationTypeTerm.GENERIC,
               PublicationTypeTerm.JOURNAL_ARTICLE,
               PublicationTypeTerm.JOURNAL,
               PublicationTypeTerm.MAGAZINE_ARTICLE,
               PublicationTypeTerm.MAP,
               PublicationTypeTerm.PATENT,
               PublicationTypeTerm.REPORT,
               PublicationTypeTerm.SUB_REFERENCE,
               PublicationTypeTerm.THESIS,
               PublicationTypeTerm.WEB_PAGE
       };
   }

    /**
   *
   */
    public static final PublicationTypeTerm ARTWORK = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Artwork",
            "Artwork");
   /**
    *
    */
    public static final PublicationTypeTerm AUDIOVISUAL_MATERIAL = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#AudiovisualMaterial",
            "AudiovisualMaterial");

    /**
     *
     */
    public static final PublicationTypeTerm BOOK = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Book",
            "Book");

    /**
     *
     */
    public static final PublicationTypeTerm BOOK_SECTION = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#BookSection",
            "BookSection");

    /**
     *
     */
    public static final PublicationTypeTerm BOOK_SERIES = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#BookSeries",
            "BookSeries");

    /**
     *
     */
    public static final PublicationTypeTerm COMMENTRY = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Commentry",
            "Commentry");

    /**
     *
     */
    public static final PublicationTypeTerm COMMUNICATION = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Communication",
            "Communication");

    /**
     *
     */
    public static final PublicationTypeTerm COMPUTER_PROGRAM = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#ComputerProgram",
            "ComputerProgram");

    /**
     *
     */
    public static final PublicationTypeTerm CONFERENCE_PROCEEDINGS = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#ConferenceProceedings",
            "ConferenceProceedings");

    /**
     *
     */
    public static final PublicationTypeTerm DETERMINATION = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Determination",
            "Determination");

    /**
     *
     */
    public static final PublicationTypeTerm EDITED_BOOK = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#EditedBook",
            "EditedBook");

    /**
     *
     */
    public static final PublicationTypeTerm GENERIC = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Generic",
            "Generic");

    /**
     *
     */
    public static final PublicationTypeTerm JOURNAL = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Journal",
            "Journal");

    /**
     *
     */
    public static final PublicationTypeTerm JOURNAL_ARTICLE = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#JournalArticle",
            "JournalArticle");

    /**
     *
     */
    public static final PublicationTypeTerm MAGAZINE_ARTICLE = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#MagazineArticle",
            "MagazineArticle");

    /**
     *
     */
    public static final PublicationTypeTerm MAP = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Map",
            "Map");

    /**
     *
     */
    public static final PublicationTypeTerm NEWSPAPER_ARTICLE = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#NewspaperArticle",
            "NewspaperArticle");

    /**
     *
     */
    public static final PublicationTypeTerm PATENT = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Patent",
            "Patent");

    /**
     *
     */
    public static final PublicationTypeTerm REPORT = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Report",
            "Report");

    /**
     *
     */
    public static final PublicationTypeTerm SUB_REFERENCE = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#SubReference",
            "SubReference");

    /**
     *
     */
    public static final PublicationTypeTerm THESIS = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#Thesis",
            "Thesis");

    /**
     *
     */
    public static final PublicationTypeTerm WEB_PAGE = new PublicationTypeTerm(
            "http://rs.tdwg.org/ontology/voc/PublicationCitation#WebPage",
            "WebPage");

  /**
   *
   */
  public PublicationTypeTerm() {
  }

  /**
   *
   * @param identifier Set the identifier of the taxon rank term
   * @param title Set the title of the taxon rank term
   */
  public PublicationTypeTerm(final String identifier, final String title) {
      this.setTitle(title);
      try {
          this.setIdentifier(new URI(identifier));
      } catch (URISyntaxException e) {
          throw new IllegalArgumentException(identifier
                  + " is not a valid uri", e);
      }
  }

  /**
   * Resolve a taxon rank term from a uri.
   *
   * @param uri the uri to resolve
   * @return a taxon relationship term
   */
  public static final PublicationTypeTerm fromValue(final String uri) {
      for (PublicationTypeTerm term : terms) {
          if (term.getIdentifier().toString().equals(uri)) {
              return term;
          }
      }
      return null;
  }

  @Override
  public final boolean equals(final Object obj) {
      if (obj == null
              || !obj.getClass().equals(PublicationTypeTerm.class)) {
          return false;
      }

      PublicationTypeTerm other = (PublicationTypeTerm) obj;
      if (other.getIdentifier() == null || this.getIdentifier() == null) {
          return false;
      }
      return this.getIdentifier().equals(other.getIdentifier());
  }

  @Override
  public final int hashCode() {
      return this.getIdentifier().hashCode();
  }

}
