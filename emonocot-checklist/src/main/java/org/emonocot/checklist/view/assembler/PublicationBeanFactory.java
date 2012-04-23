package org.emonocot.checklist.view.assembler;

import org.dozer.BeanFactory;
import org.emonocot.checklist.model.Publication;
import org.emonocot.checklist.model.PublicationType;
import org.tdwg.voc.PublicationCitation;
import org.tdwg.voc.PublicationTypeTerm;

/**
 *
 * @author ben
 *
 */
public class PublicationBeanFactory implements BeanFactory {

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

        Publication publication = (Publication) source;
        PublicationCitation publicationCitation = new PublicationCitation();

        PublicationType publicationType = null;
        try{
            publicationType = publication.getType(); 
        } catch (NullPointerException npe) {}//if hibernate is proxying a null
        if(publicationType != null) {
            switch (publicationType) {
            case BOOK:
                publicationCitation
                        .setPublicationType(PublicationTypeTerm.BOOK);
                break;
            case JOURNAL:
                publicationCitation
                        .setPublicationType(PublicationTypeTerm.JOURNAL);
                break;
            case ELECTRONIC:
                publicationCitation
                        .setPublicationType(PublicationTypeTerm.WEB_PAGE);
                break;
            case SERIAL_FLORA:
                publicationCitation
                        .setPublicationType(PublicationTypeTerm.BOOK_SERIES);
                break;
            case PERSONAL_COMMUNICATION:
                publicationCitation
                        .setPublicationType(PublicationTypeTerm.COMMUNICATION);
                break;
            case SPECIMEN:
                publicationCitation
                        .setPublicationType(PublicationTypeTerm.DETERMINATION);
                break;
            default:
                publicationCitation.setPublicationType(PublicationTypeTerm.GENERIC);
            }
        } else {
            publicationCitation.setPublicationType(PublicationTypeTerm.GENERIC);
        }

        return publicationCitation;
    }

}
