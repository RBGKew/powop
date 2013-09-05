/**
 *
 */
package org.emonocot.job.taxonmatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.match.Match;
import org.emonocot.api.match.MatchStatus;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.Taxon;
import org.emonocot.pager.Page;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.parser.NameParser;
import org.gbif.ecat.parser.UnparsableException;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jk00kg
 */
public class DefaultTaxonMatcher implements TaxonMatcher {

    private Logger logger = LoggerFactory.getLogger(DefaultTaxonMatcher.class);

    @Autowired
    private SearchableObjectService searchableObjectService;
    
    @Autowired 
    private NameParser nameParser;
    
    private Boolean assumeAcceptedMatches = Boolean.FALSE;
    
    public void setNameParser(NameParser nameParser) {
    	this.nameParser = nameParser;
    }
    
    public void setAssumeAcceptedMatches(Boolean assumeAcceptedMatches) {
    	if(assumeAcceptedMatches != null) {
    	    this.assumeAcceptedMatches = assumeAcceptedMatches;
    	}
    }

    /**
     * @param searchableObjectService
     *            the taxonService to set
     */
    public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
        this.searchableObjectService = searchableObjectService;
    }

    /*
     * Returns one or more match of the same status
     * @see
     * org.emonocot.api.match.TaxonMatcher#match(org.gbif.ecat.model.ParsedName
     * )
     */
    public List<Match<Taxon>> match(ParsedName<String> parsed) {
        StringBuilder stringBuilder = new StringBuilder();
        if (parsed.getSpecificEpithet() == null) {
            stringBuilder.append("searchable.label_sort:" + parsed.getGenusOrAbove());
            if(parsed.getAuthorship() != null) {
                stringBuilder.append(" AND taxon.scientific_name_authorship_s:"
                        + parsed.getAuthorship());
            }
        } else {
            stringBuilder.append("taxon.genus_ns:" + parsed.getGenusOrAbove());
            if (parsed.getSpecificEpithet() != null) {
                stringBuilder.append(" AND taxon.specific_epithet_s:"
                        + parsed.getSpecificEpithet());
            }
            if (parsed.getInfraGeneric() != null) {
                stringBuilder.append(" AND taxon.subgenus_s:"
                        + parsed.getInfraGeneric());
            }
            if (parsed.getInfraSpecificEpithet() != null) {
                stringBuilder.append(" AND taxon.infraspecific_epithet_s:"
                        + parsed.getInfraSpecificEpithet());
            } else {
            	stringBuilder.append(" AND -taxon.infraspecific_epithet_s:[* TO *]");
            }
            if (parsed.getRank() != null) {
                if (parsed.getRank().equals(Rank.SPECIES)) {
                    stringBuilder.append(" AND taxon.taxon_rank_s:SPECIES");
                } else {

                }

            }
            if(parsed.getAuthorship() != null) {
                stringBuilder.append(" AND taxon.scientific_name_authorship_s:"
                        + parsed.getAuthorship());
            }
        }

        String searchTerm = stringBuilder.toString();
        logger.debug("Attempting to match " + searchTerm);
        List<Match<Taxon>> matches = new ArrayList<Match<Taxon>>();
        Map<String,String> selectedFacets = new HashMap<String,String>();
        selectedFacets.put("base.class_s", "org.emonocot.model.Taxon");
        Page<SearchableObject> page;
		try {
			page = searchableObjectService.search(searchTerm, null, null, null, null, null, selectedFacets, null, null);
		} catch (SolrServerException sse) {
			throw new RuntimeException("SolrServerException", sse);
		}
        
        switch (page.getRecords().size()) {
        case 0:
        	if(parsed.getBracketAuthorship() != null){
        		logger.info("removing bracket authorship " + parsed.getAuthorship());
        		parsed.setBracketAuthorship(null);
        		logger.info("'null' bracket authorship is " + parsed.getAuthorship());
        		matches = match(parsed);
        	} else if (parsed.getAuthorship() != null) {
        		logger.info("removing authorship " + parsed.getAuthorship());
        		parsed.setAuthorship(null);
        		logger.info("'null' authorship is " + parsed.getAuthorship());
        		matches = match(parsed);
        	}
            break;
        case 1:
            Match<Taxon> single = new Match<Taxon>();
            single.setInternal((Taxon)page.getRecords().get(0));
            String internalName = (new NameParser().parseToCanonical(single.getInternal().getScientificName()));
            
            if (parsed.canonicalName().equals(internalName)) {
                single.setStatus(MatchStatus.EXACT);
            } else {
                single.setStatus(MatchStatus.PARTIAL);
            }
            matches.add(single);
            break;
        default:
        	logger.debug(page.getSize() + " records found");
            Set<Match<Taxon>> exactMatches = new HashSet<Match<Taxon>>();
            for (SearchableObject eTaxon : page.getRecords()) {
                logger.debug(((Taxon)eTaxon).getScientificName() + " " + eTaxon.getIdentifier());
                Match<Taxon> m = new Match<Taxon>();
                m.setInternal((Taxon)eTaxon);
                matches.add(m);
                String name = (new NameParser().parseToCanonical(((Taxon)eTaxon).getScientificName()));
                logger.debug("Name is " + name);
                if (parsed.canonicalName().equals(name)) {
                    m.setStatus(MatchStatus.EXACT);
                    exactMatches.add(m);
                } else {
                    m.setStatus(MatchStatus.PARTIAL);
                }
            }
            logger.debug(exactMatches.size() + " exact matches");
            switch (exactMatches.size()) {
            case 0:
                break;
            case 1:
                matches.retainAll(exactMatches);
                break;
            default:
            	if(assumeAcceptedMatches && parsed.getAuthorship() == null) {
            		Set<Match<Taxon>> acceptedMatches = new HashSet<Match<Taxon>>();
            		for(Match<Taxon> match : exactMatches) {
            			logger.debug("Taxonomic status " + match.getInternal().getTaxonomicStatus());
            			if(match.getInternal().getTaxonomicStatus() != null && match.getInternal().getTaxonomicStatus().equals(TaxonomicStatus.Accepted)) {
            				acceptedMatches.add(match);
            			}
            		}
            		if(acceptedMatches.size() == 1) {
            			matches.retainAll(acceptedMatches);
            		} else {
            			logger.debug(acceptedMatches.size() + " accepted taxa exactly match");
            		}
            	} else {
                    logger.debug(exactMatches.size() + " exact matches:");
            	}
                break;
            }
        }
        return matches;
    }

	@Override
	public List<Match<Taxon>> match(String name) throws UnparsableException {
		ParsedName<String> parsed = nameParser.parse(name);
		return match(parsed);
	}

}
