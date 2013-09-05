package org.emonocot.job.key;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.emonocot.api.IdentificationKeyService;
import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.tdwg.ubif.Agent;
import org.tdwg.ubif.Dataset;

/**
 *
 * @author ben
 *
 */
public class DatasetProcessor extends AuthorityAware implements
        ItemProcessor<Dataset, IdentificationKey> {

   private Logger logger = LoggerFactory.getLogger(DatasetProcessor.class);

    private String authorityUri;

    private IdentificationKeyService identificationKeyService;

    private Resource matrixFile;

    private String rootTaxonIdentifier;

    private TaxonService taxonService;

    public void setRootTaxonIdentifier(String newRootTaxonIdentifier) {
        this.rootTaxonIdentifier = newRootTaxonIdentifier;
    }

    public void setTaxonService(TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    public void setAuthorityUri(String newAuthorityUri) {
        this.authorityUri = newAuthorityUri;
    }

    public void setMatrixFile(Resource newMatrixFile) {
        this.matrixFile = newMatrixFile;
    }
    
    @Autowired
    public void setIdentificationKeyService(
            IdentificationKeyService newIdentificationKeyService) {
        this.identificationKeyService = newIdentificationKeyService;
    }

    /**
     *
     * @param resource
     *            Set the resource
     * @return the string
     * @throws IOException
     *             if there is a problem reading the file
     */
    private String readFileAsString(Resource resource)
        throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resource.getInputStream()));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString().trim();
    }

    /**
     * @param item
     *            Set the dataset
     * @return the identification key
     * @throws Exception
     *             if there is a problem
     */
    public IdentificationKey process(Dataset item)
        throws Exception {
        IdentificationKey persistedIdentificationKey = identificationKeyService.find(authorityUri,"object-page");
        String matrix = readFileAsString(matrixFile);
        if (persistedIdentificationKey == null) {
            IdentificationKey identificationKey = new IdentificationKey();
            identificationKey.setIdentifier(authorityUri);
            identificationKey.setAuthority(getSource());

            if (rootTaxonIdentifier != null
                    && !rootTaxonIdentifier.isEmpty()) {
            	if(rootTaxonIdentifier.indexOf(",") == -1) {
                    Taxon root = taxonService.find(rootTaxonIdentifier);
                    logger.debug("rootTaxonIdentifier is "  + rootTaxonIdentifier);
                    logger.debug("rootTaxon "  + root);
                    identificationKey.getTaxa().add(root);
            	} else {
            		for(String identifier : rootTaxonIdentifier.split(",")) {
            			Taxon root = taxonService.find(identifier);
                        logger.debug("rootTaxonIdentifier is "  + identifier);
                        logger.debug("rootTaxon "  + root);
                        identificationKey.getTaxa().add(root);
            		}
            	}
            }

            if (item.getRevisionData() != null) {
                identificationKey.setCreated(item.getRevisionData().getDateCreated());
                identificationKey.setCreator(constructCreators(item));
                identificationKey.setModified(item.getRevisionData().getDateModified());
            }
            identificationKey.setTitle(item.getRepresentation().getLabel());
            identificationKey.setDescription(item.getRepresentation().getDetail());
            identificationKey.setMatrix(matrix);
            return identificationKey;
        } else {
            if (item.getRevisionData() != null) {
                persistedIdentificationKey.setModified(item.getRevisionData().getDateModified());                
                persistedIdentificationKey.setCreated(item.getRevisionData().getDateCreated());                
                persistedIdentificationKey.setCreator(constructCreators(item));
            }
            if (rootTaxonIdentifier != null
                    && rootTaxonIdentifier.trim().length() > 0) {
                Taxon root = taxonService.find(rootTaxonIdentifier);
                persistedIdentificationKey.getTaxa().clear();
                persistedIdentificationKey.getTaxa().add(root);
                logger.debug("rootTaxonIdentifier is "  + rootTaxonIdentifier);
                logger.debug("rootTaxon "  + root);
            }
            persistedIdentificationKey.setTitle(item.getRepresentation().getLabel());
            persistedIdentificationKey.setDescription(item.getRepresentation().getDetail());
            persistedIdentificationKey.setMatrix(matrix);
            
            return persistedIdentificationKey;
        }
    }

    /**
     *
     * @param item Set the item
     * @return the list of creators
     */
    private String constructCreators(Dataset item) {
        StringBuffer creator = new StringBuffer();
        boolean appendComma = false;
        for (Agent a : item.getRevisionData().getCreators()) {
            if (appendComma) {
                creator.append(", ");
            }
            for (Agent agent : item.getAgents()) {
                if (agent.getId().equals(a.getRef())) {
                    creator.append(agent.getRepresentation().getLabel());
                    appendComma = true;
                    break;
                }
            }

        }
        return creator.toString();
    }
}
