package org.emonocot.model.constants;

/**
 *
 * @author ben
 *
 */
public enum ResourceType {
    DwC_Archive("DarwinCoreArchiveHarvesting"),   
    IDENTIFICATION_KEY("IdentificationKeyHarvesting"),
    IUCN("IUCNImport"),
    GBIF("GBIFImport"),
    DOWNLOAD("Download"),
    PHYLOGENETIC_TREE("PhylogeneticTreeHarvesting");
    
    private String jobName;
    
    private ResourceType(String jobName) {
    	this.jobName = jobName;
    }
    
    public String getJobName() {
    	return jobName;
    }
}
