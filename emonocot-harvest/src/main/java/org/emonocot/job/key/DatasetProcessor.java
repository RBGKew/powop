package org.emonocot.job.key;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import org.emonocot.api.IdentificationKeyService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.model.key.IdentificationKey;
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

    /**
     *
     */
    private String authorityUri;

    /**
     *
     */
    private IdentificationKeyService identificationKeyService;

    /**
     *
     */
    private Resource matrixFile;

    /**
     * @param newAuthorityUri
     *            Set the source of this dataset
     */
    public final void setAuthorityUri(final String newAuthorityUri) {
        this.authorityUri = newAuthorityUri;
    }

    /**
     * @param newMatrixFile
     *            the matrixFile to set
     */
    public final void setMatrixFile(final Resource newMatrixFile) {
        this.matrixFile = newMatrixFile;
    }

    /**
     * @param newIdentificationKeyService
     *            Set the identification key service
     */
    @Autowired
    public final void setIdentificationKeyService(
            final IdentificationKeyService newIdentificationKeyService) {
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
    private String readFileAsString(final Resource resource)
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
    public final IdentificationKey process(final Dataset item)
        throws Exception {
        IdentificationKey persistedIdentificationKey = identificationKeyService
                .findBySource(authorityUri);
        String matrix = readFileAsString(matrixFile);
        if (persistedIdentificationKey == null) {
            IdentificationKey identificationKey = new IdentificationKey();
            identificationKey.setSource(authorityUri);
            identificationKey.setAuthority(getSource());
            identificationKey.setIdentifier(UUID.randomUUID().toString());

            if (item.getRevisionData() != null) {
                identificationKey.setCreated(item.getRevisionData()
                        .getDateCreated());
                identificationKey.setCreator(constructCreators(item));
            }
            identificationKey.setTitle(item.getRepresentation().getLabel());
            identificationKey.setTitle(item.getRepresentation().getDetail());
            identificationKey.setMatrix(matrix);
            return identificationKey;
        } else {
            if (item.getRevisionData() != null) {
                if (persistedIdentificationKey.getCreated().isBefore(
                        item.getRevisionData().getDateCreated())) {
                    persistedIdentificationKey.setModified(item
                            .getRevisionData().getDateCreated());
                } else {
                    persistedIdentificationKey.setCreated(item
                            .getRevisionData().getDateCreated());
                }
                persistedIdentificationKey.setCreator(constructCreators(item));
            }
            persistedIdentificationKey.setTitle(item.getRepresentation()
                    .getLabel());
            persistedIdentificationKey.setTitle(item.getRepresentation()
                    .getDetail());
            persistedIdentificationKey.setMatrix(matrix);
            return persistedIdentificationKey;
        }
    }

    /**
     *
     * @param item Set the item
     * @return the list of creators
     */
    private String constructCreators(final Dataset item) {
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
