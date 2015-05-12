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
package org.emonocot.portal.view.bibliography;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.emonocot.model.Distribution;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;

/**
 *
 * @author ben
 *
 */
public interface Bibliography {

    /**
     *
     * @param taxon Set the taxon
     */
    void setReferences(final Taxon taxon);

    /**
     *
     * @param refernce Set the reference
     * @return A string key which can be displayed in the taxon page as a
     *         citation reference to the reference
     */
    String getKey(Reference refernce);
    
    /**
     *
     * @param key the key to the reference
     * @return the reference
     */
    Reference getReference(String key);
    
    /**
     *
     * @param distributions Set the distributions
     * @return a sorted set of keys
     */
    SortedSet<String> getKeys(Collection<Distribution> distributions);

    /**
     *
     * @return a sorted list of references
     */
    List<Reference> getReferences();

}
