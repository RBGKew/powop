package org.emonocot.checklist.view.assembler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.dozer.BeanFactory;
import org.emonocot.checklist.model.Taxon;
import org.hibernate.Hibernate;
import org.tdwg.voc.PublicationCitation;
import org.tdwg.voc.PublicationTypeTerm;

/**
 *
 * @author ben
 *
 */
public class ProtologueBeanFactory implements BeanFactory {

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

        Taxon taxon = (Taxon) source;
        PublicationCitation publicationCitation = new PublicationCitation();
        publicationCitation.setPublicationType(PublicationTypeTerm.GENERIC);
        publicationCitation.setAuthorship(taxon.getProtologueAuthor());
        publicationCitation.setDatePublished(taxon.getPublicationDate());

        if (Hibernate.isInitialized(taxon.getProtologue())
                && taxon.getProtologue() != null) {

            publicationCitation.setTpubTitle(taxon.getProtologue().getTitle());

            try {
                publicationCitation
                  .setIdentifier(
                          new URI(taxon.getProtologue().getIdentifier()));
            } catch (URISyntaxException e) {
               new RuntimeException(e);
            }
        }

        return publicationCitation;
    }
}
