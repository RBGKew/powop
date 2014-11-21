/**
 * 
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
