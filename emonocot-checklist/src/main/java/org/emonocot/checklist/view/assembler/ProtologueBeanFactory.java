package org.emonocot.checklist.view.assembler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dozer.BeanFactory;
import org.emonocot.checklist.model.Article;
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
    Pattern pattern = Pattern.compile("(.*?):(.*?)");

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
            if (taxon.getVolumeAndPage() != null) {
                Matcher matcher = pattern.matcher(taxon.getVolumeAndPage());
                if (matcher.matches()) {
                    // then the volume and page falls into the pattern
                  if (matcher.group(1).length() > 0) {
                      publicationCitation.setVolume(matcher.group(1));
                  }
                  if (matcher.group(2).length() > 0) {
                      publicationCitation.setPages(matcher.group(2));
                  }
                  publicationCitation.setTpubTitle(
                          taxon.getProtologue().getTitle());
                } else {
                    publicationCitation.setTpubTitle(
                            taxon.getProtologue().getTitle()
                            + taxon.getVolumeAndPage());
                }
            }
        }

        return publicationCitation;
    }

}
