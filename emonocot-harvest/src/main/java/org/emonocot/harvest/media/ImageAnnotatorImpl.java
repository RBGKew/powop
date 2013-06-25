/**
 * 
 */
package org.emonocot.harvest.media;

import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.Annotation;
import org.emonocot.model.Image;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.RecordType;
import org.joda.time.DateTime;

/**
 * @author jk00kg
 *
 */
public class ImageAnnotatorImpl extends AbstractRecordAnnotator implements ImageAnnotator {

    /**
     * 
     */
    private Long jobId;

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /* (non-Javadoc)
	 * @see org.emonocot.harvest.media.ImageAnnotator#annotate(org.emonocot.model.Image, org.emonocot.model.constants.AnnotationCode, java.lang.String)
	 */
    @Override
	public void annotate(Image image, AnnotationCode code, String message) {
        Annotation a = new Annotation();
        a.setAnnotatedObj(image);
        a.setAuthority(super.getSource());
        if(jobId != null) {
            a.setJobId(jobId);
        }
        a.setCode(code);
        a.setDateTime(new DateTime());
        a.setRecordType(RecordType.Image);
        a.setText(message);
        super.annotate(a);
    }
}
