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
package org.emonocot.model.convert;

import org.emonocot.model.Multimedia;
import org.emonocot.model.PhylogeneticTree;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jk00kg
 *
 */
public class MultimediaToPhylogeneticTreeConverter implements Converter<Multimedia, PhylogeneticTree> {


    /* (non-Javadoc)
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public PhylogeneticTree convert(Multimedia source) {
        //TODO throw exception if it is not a tree
        if(source instanceof PhylogeneticTree) {
            return (PhylogeneticTree) source;
        } else {
            PhylogeneticTree phylogeneticTree = new PhylogeneticTree();
            phylogeneticTree.setAccessRights(source.getAccessRights());
            phylogeneticTree.setAudience(source.getAudience());
            phylogeneticTree.setAuthority(source.getAuthority());
            phylogeneticTree.setContributor(source.getContributor());
            phylogeneticTree.setCreated(source.getCreated());
            phylogeneticTree.setCreator(source.getCreator());
            phylogeneticTree.setDescription(source.getDescription());
            phylogeneticTree.setFormat(source.getFormat());
            phylogeneticTree.setIdentifier(source.getIdentifier());
            phylogeneticTree.setLicense(source.getLicense());
            phylogeneticTree.setModified(source.getModified());
            phylogeneticTree.setPublisher(source.getPublisher());
            phylogeneticTree.setReferences(source.getReferences());
            phylogeneticTree.setRights(source.getRights());
            phylogeneticTree.setRightsHolder(source.getRightsHolder());
            phylogeneticTree.setSource(source.getSource());
            phylogeneticTree.setTaxa(source.getTaxa());
            phylogeneticTree.setTitle(source.getTitle());
            return phylogeneticTree;
        }
    }

}
