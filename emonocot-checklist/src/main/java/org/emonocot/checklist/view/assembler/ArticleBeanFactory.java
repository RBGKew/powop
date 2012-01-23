package org.emonocot.checklist.view.assembler;

import org.dozer.BeanFactory;
import org.emonocot.checklist.model.Article;
import org.hibernate.Hibernate;
import org.tdwg.voc.PublicationCitation;
import org.tdwg.voc.PublicationTypeTerm;

/**
 *
 * @author ben
 *
 */
public class ArticleBeanFactory implements BeanFactory {

   /**
    *
    * @param source Set the source bean
    * @param sourceClass Set the class of the source bean
    * @param targetBeanId Set the target bean id
    * @return a new object
    */
    public final Object createBean(final Object source, final Class sourceClass,
            final String targetBeanId) {
        if (source == null) {
            return null;
        }

        Article article = (Article) source;
        PublicationCitation publicationCitation = new PublicationCitation();
        if (article.getPageFrom() != null) {
            if (article.getPageTo() != null) {
                publicationCitation.setPages(article.getPageFrom()
                        + " - " + article.getPageTo());
            } else {
                publicationCitation.setPages(article.getPageFrom()
                        + " -");
            }
        } else {
            if (article.getPageTo() != null) {
                publicationCitation
                        .setPages("- " + article.getPageTo());
            }
        }
        if (Hibernate.isInitialized(article.getPublication())
                && article.getPublication() != null) {
            switch (article.getPublication().getType()) {
            case BOOK:
                publicationCitation.setPublicationType(
                        PublicationTypeTerm.BOOK_SECTION);
                break;
            case JOURNAL:
                publicationCitation.setPublicationType(
                        PublicationTypeTerm.JOURNAL_ARTICLE);
                break;
            case ELECTRONIC:
                publicationCitation.setPublicationType(
                        PublicationTypeTerm.WEB_PAGE);
                break;
            case SERIAL_FLORA:
                publicationCitation.setPublicationType(
                        PublicationTypeTerm.BOOK);
                break;
            case PERSONAL_COMMUNICATION:
                publicationCitation.setPublicationType(
                        PublicationTypeTerm.COMMUNICATION);
                break;
            default:
                publicationCitation.setPublicationType(
                        PublicationTypeTerm.GENERIC);
            }
        } else {
            publicationCitation.setPublicationType(PublicationTypeTerm.GENERIC);
        }

        return publicationCitation;
    }

}
