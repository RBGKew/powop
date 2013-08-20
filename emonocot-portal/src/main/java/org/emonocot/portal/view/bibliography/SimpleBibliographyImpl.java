package org.emonocot.portal.view.bibliography;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.emonocot.model.Distribution;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.compare.ReferenceComparator;

/**
 *
 * @author ben
 *
 */
public class SimpleBibliographyImpl implements Bibliography {
    /**
     *
     */
    List<Reference> references = new ArrayList<Reference>();
    
    SortedSet<ReferenceWrapper> refs = new TreeSet<ReferenceWrapper>(new ReferenceWrapperComparator());

   /**
    *
    * @param taxon Set the taxon
    */
    public final void setReferences(final Taxon taxon) {
        
        for(Reference reference : taxon.getReferences()) {
            refs.add(new ReferenceWrapper(reference));
        }
        for (Description d : taxon.getDescriptions()) {
        	for(Reference reference : d.getReferences()) {
                refs.add(new ReferenceWrapper(reference));
        	}
        }
        for (Distribution d : taxon.getDistribution()) {
        	for(Reference reference : d.getReferences()) {
                refs.add(new ReferenceWrapper(reference));
        	}
        }
        for(ReferenceWrapper ref : refs) {
            references.add(ref.reference);
        }
    }

   /**
    *
    * @param reference Set the reference
    * @return A string key which can be displayed in the taxon page as a
    *         citation reference to the reference
    */
    public final String getKey(final Reference reference) {
    	ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
    	ReferenceWrapper wrapper = null;
    	for(ReferenceWrapper rw : refs) {
    		if(rw.equals(referenceWrapper)) {
    			wrapper = rw;
    			break;
    		}
    	}
        return new Integer(references.indexOf(wrapper.reference) + 1).toString();
    }

   /**
    *
    * @return a sorted list of references
    */
    public final List<Reference> getReferences() {
        return references;
    }

    /**
     *
     * @author ben
     *
     */
	class ReferenceWrapperComparator implements Comparator<ReferenceWrapper> {

		private Comparator<Reference> referenceComparator = new ReferenceComparator();

        /**
         * @param o1
         *            Set the first reference
         * @param o2
         *            Set the second reference
         * @return 1 if o1 comes after o2, -1 if o1 comes before o2 and 0
         *         otherwise
         */
        public final int compare(final ReferenceWrapper o1, final ReferenceWrapper o2) {
        	return referenceComparator.compare(o1.reference, o2.reference);
        }
    }

	@Override
	public SortedSet<String> getKeys(Collection<Distribution> distributions) {
		SortedSet<String> keys = new TreeSet<String>();
		for(Distribution d: distributions) {
			for(Reference r : d.getReferences()) {
				keys.add(getKey(r));
			}
		}
		return keys;
	}

	@Override
	public Reference getReference(String key) {
		Integer i = Integer.parseInt(key) - 1;
		return references.get(i);
	}
	
	class ReferenceWrapper {
		private Reference reference;
		
		public ReferenceWrapper(Reference reference) {
			this.reference = reference;
		}
		
		public int hashCode() {
	        return new HashCodeBuilder(17, 31)
	            .append(reference.getDate())
	            .append(reference.getCreator())
	            .append(reference.getTitle())
	            .append(reference.getBibliographicCitation())
	            .toHashCode();
	    }

	    public boolean equals(Object obj) {
	        if (obj == null)
	            return false;
	        if (obj == this)
	            return true;
	        if (!(obj instanceof ReferenceWrapper))
	            return false;

	        ReferenceWrapper rhs = (ReferenceWrapper) obj;
	        return new EqualsBuilder()
	            .append(reference.getDate(), rhs.reference.getDate())
	            .append(reference.getCreator(), rhs.reference.getCreator())
	            .append(reference.getTitle(), rhs.reference.getTitle())
	            .append(reference.getBibliographicCitation(), rhs.reference.getBibliographicCitation())
	            .isEquals();
	    }
	}
}
