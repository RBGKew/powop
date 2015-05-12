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
package org.emonocot.harvest.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 *
 * @author Adrian Gonzalez
 * 
 * @author jk00kg
 * Adding support for URL Resources
 */
public class MultiResourceDeletingTasklet implements Tasklet {

    /** Commons-logging Logger. */
    private static final Log logger = LogFactory
            .getLog(MultiResourceDeletingTasklet.class);

    /**
     * Répertoires ou fichiers à supprimer.
     */
    private Resource[] resources;

    /**
     *
     * @param aResources The resources to delete
     */
    public final void setResources(final Resource[] aResources) {
        resources = aResources;
    }

    /**
     * <p>
     * Vérifie que l'attribut resources est bien valorisé et que ses éléments
     * correspondent à des FileSystemResource.
     * </p>
     *
     */
    public final void afterPropertiesSet() {
        Assert.notNull(resources, "resources must be set");
        for (Resource lResource : resources) {
            Assert.isInstanceOf(
                    FileSystemResource.class,
                    lResource,
                    "The attribute 'resources' does not contain"
                    + " resources of the type FileSystemResource.");
        }
    }

    /**
     * @param contribution Set the step contribution
     * @param chunkContext Set the chunk context
     * @return the repeat status
     * @throws Exception if there is a problem deleting the resources
     */
    public final RepeatStatus execute(final StepContribution contribution,
            final ChunkContext chunkContext) throws Exception {
        for (Resource lResource : resources) {
            FileSystemResource lFileSystemResource
                  = new FileSystemResource(lResource.getFile().getAbsolutePath());
            if (!lFileSystemResource.exists()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Resource "
                            + lFileSystemResource.getDescription()
                            + " does not exist. The resource is ignored");
                }
            } else {
                File lFile = lFileSystemResource.getFile();
                if (lFile.isDirectory()) {
                    // supprime le répertoire et son contenu
                    FileUtils.deleteDirectory(lFile);
                } else {
                    if (!lFile.delete()) {
                        throw new IOException("The file " + lFile
                                + " cannot be deleted.");
                    }
                }
            }
        }
        return RepeatStatus.FINISHED;
    }
}
