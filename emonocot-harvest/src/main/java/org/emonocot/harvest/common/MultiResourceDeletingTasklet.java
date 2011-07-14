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
 */
public class MultiResourceDeletingTasklet implements Tasklet {

    /** Commons-logging Logger. */
    private final Log logger = LogFactory
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

    public final RepeatStatus execute(final StepContribution contribution,
            final ChunkContext chunkContext) throws Exception {
        for (Resource lResource : resources) {
            FileSystemResource lFileSystemResource
                  = (FileSystemResource) lResource;
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
