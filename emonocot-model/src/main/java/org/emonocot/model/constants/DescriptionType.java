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
package org.emonocot.model.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @see http://rs.gbif.org/vocabulary/gbif/descriptionType
 */
public enum DescriptionType {

	// standard gbif-extension types
	general("http://rs.gbif.org/vocabulary/gbif/descriptionType/general", "general"),
	diagnostic("http://rs.gbif.org/vocabulary/gbif/descriptionType/diagnostic", "diagnostic"),
	morphology("http://rs.gbif.org/vocabulary/gbif/descriptionType/morphology", "morphology"),
	habit("http://rs.gbif.org/vocabulary/gbif/descriptionType/habit", "generalMorphology:habit"),
	cytology("http://rs.gbif.org/vocabulary/gbif/descriptionType/cytology", "cytology"),
	physiology("http://rs.gbif.org/vocabulary/gbif/descriptionType/physiology", "physiology"),
	size("http://rs.gbif.org/vocabulary/gbif/descriptionType/size", "size"),
	weight("http://rs.gbif.org/vocabulary/gbif/descriptionType/weight", "weight"),
	lifespan("http://rs.gbif.org/vocabulary/gbif/descriptionType/lifespan", "lifespan"),
	lifetime("http://rs.gbif.org/vocabulary/gbif/descriptionType/lifetime", "lifetime"),
	biology("http://rs.gbif.org/vocabulary/gbif/descriptionType/biology", "biology"),
	ecology("http://rs.gbif.org/vocabulary/gbif/descriptionType/ecology", "ecology"),
	habitat("http://rs.gbif.org/vocabulary/gbif/descriptionType/habitat", "habitat"),
	distribution("http://rs.gbif.org/vocabulary/gbif/descriptionType/distribution", "distribution"),
	reproduction("http://rs.gbif.org/vocabulary/gbif/descriptionType/reproduction", "reproduction"),
	conservation("http://rs.gbif.org/vocabulary/gbif/descriptionType/conservation", "conservation"),
	use("http://rs.gbif.org/vocabulary/gbif/descriptionType/use", "use"),
	dispersal( "http://rs.gbif.org/vocabulary/gbif/descriptionType/dispersal", "dispersal"),
	cyclicity("http://rs.gbif.org/vocabulary/gbif/descriptionType/cyclicity", "cyclicity"),
	lifecycle("http://rs.gbif.org/vocabulary/gbif/descriptionType/lifecycle", "lifecycle"),
	migration("http://rs.gbif.org/vocabulary/gbif/descriptionType/migration", "migration"),
	growth( "http://rs.gbif.org/vocabulary/gbif/descriptionType/growth", "constructionalOrganisation:growth"),
	genetics("http://rs.gbif.org/vocabulary/gbif/descriptionType/genetics", "genetics"),
	chemistry("http://rs.gbif.org/vocabulary/gbif/descriptionType/chemistry", "chemistry"),
	diseases("http://rs.gbif.org/vocabulary/gbif/descriptionType/diseases", "diseases"),
	associations("http://rs.gbif.org/vocabulary/gbif/descriptionType/associations", "associations"),
	behaviour("http://rs.gbif.org/vocabulary/gbif/descriptionType/behaviour", "behaviour"),
	population("http://rs.gbif.org/vocabulary/gbif/descriptionType/population", "population"),
	management("http://rs.gbif.org/vocabulary/gbif/descriptionType/management", "management"),
	legislation("http://rs.gbif.org/vocabulary/gbif/descriptionType/legislation", "legislation"),
	threats("http://rs.gbif.org/vocabulary/gbif/descriptionType/threats", "threats"),
	typematerial("http://rs.gbif.org/vocabulary/gbif/descriptionType/typematerial", "typematerial"),
	typelocality("http://rs.gbif.org/vocabulary/gbif/descriptionType/typelocality", "typelocality"),
	phylogeny("http://rs.gbif.org/vocabulary/gbif/descriptionType/phylogeny", "phylogeny"),
	hybrids("http://rs.gbif.org/vocabulary/gbif/descriptionType/hybrids", "hybrids"),
	literature("http://rs.gbif.org/vocabulary/gbif/descriptionType/literature", "literature"),
	culture("http://rs.gbif.org/vocabulary/gbif/descriptionType/culture", "culture"),
	vernacular("http://rs.gbif.org/vocabulary/gbif/descriptionType/vernacular", "vernacular"),

	// non-standard description types
	concept("concept"),
	fern("fern"),
	figures("figures"),
	floralDiagram("floralDiagram"),
	generalMorphology("generalMorphology"),
	generalMorphologyWood("generalMorphology:Wood"),
	generalMorphologyAreoles("generalMorphology:areoles"),
	generalMorphologyBuds("generalMorphology:buds"),
	generalMorphologyColleters("generalMorphology:colleters"),
	generalMorphologyCystoliths("generalMorphology:cystoliths"),
	generalMorphologyExudate("generalMorphology:exudate"),
	generalMorphologyGlands("generalMorphology:glands"),
	generalMorphologyHabit("generalMorphology:habit"),
	generalMorphologyIndumentum("generalMorphology:indumentum"),
	generalMorphologyScales("generalMorphology:scales"),
	generalMorphologySclaes("generalMorphology:sclaes"),
	generalMorphologySpines("generalMorphology:spines"),
	generalMorphologyTendrils("generalMorphology:tendrils"),
	generalMorphologyThallus("generalMorphology:thallus"),
	generalMorphologyTraps("generalMorphology:traps"),
	glossary("glossary"),
	leafMorphology("leafMorphology"),
	leafMorphologyLamina("leafMorphology:lamina"),
	leafMorphologyLaminaMargins("leafMorphology:lamina:margins"),
	leafMorphologyLaminaVenation("leafMorphology:lamina:venation"),
	leafMorphologyLeafSheaths("leafMorphology:leaf-sheaths"),
	leafMorphologyLigule("leafMorphology:ligule"),
	leafMorphologyLigules("leafMorphology:ligules"),
	leafMorphologyOcrea("leafMorphology:ocrea"),
	leafMorphologyPetiole("leafMorphology:petiole"),
	leafMorphologyStipules("leafMorphology:stipules"),
	note("note"),
	reference("reference"),
	reproductiveMorphology("reproductiveMorphology"),
	reproductiveMorphologyCones("reproductiveMorphology:cones"),
	reproductiveMorphologyConesFemale("reproductiveMorphology:cones:female"),
	reproductiveMorphologyConesMale("reproductiveMorphology:cones:male"),
	reproductiveMorphologyFlowers("reproductiveMorphology:flowers"),
	reproductiveMorphologyFlowersAndroecium("reproductiveMorphology:flowers:androecium"),
	reproductiveMorphologyFlowersAndrogynophore("reproductiveMorphology:flowers:androgynophore"),
	reproductiveMorphologyFlowersBracts("reproductiveMorphology:flowers:bracts"),
	reproductiveMorphologyFlowersBuds("reproductiveMorphology:flowers:buds"),
	reproductiveMorphologyFlowersCalyx("reproductiveMorphology:flowers:calyx"),
	reproductiveMorphologyFlowersColumn("reproductiveMorphology:flowers:column"),
	reproductiveMorphologyFlowersCorolla("reproductiveMorphology:flowers:corolla"),
	reproductiveMorphologyFlowersCorona("reproductiveMorphology:flowers:corona"),
	reproductiveMorphologyFlowersDisc("reproductiveMorphology:flowers:disc"),
	reproductiveMorphologyFlowersEpicalyx("reproductiveMorphology:flowers:epicalyx"),
	reproductiveMorphologyFlowersFemale("reproductiveMorphology:flowers:female"),
	reproductiveMorphologyFlowersFemaleCalyx("reproductiveMorphology:flowers:female:calyx"),
	reproductiveMorphologyFlowersFemaleCorolla("reproductiveMorphology:flowers:female:corolla"),
	reproductiveMorphologyFlowersFemaleGynoecium("reproductiveMorphology:flowers:female:gynoecium"),
	reproductiveMorphologyFlowersFemalePerianth("reproductiveMorphology:flowers:female:perianth"),
	reproductiveMorphologyFlowersFemaleReceptacle("reproductiveMorphology:flowers:female:receptacle"),
	reproductiveMorphologyFlowersFemaleSterileParts("reproductiveMorphology:flowers:female:sterileParts"),
	reproductiveMorphologyFlowersGynoecium("reproductiveMorphology:flowers:gynoecium"),
	reproductiveMorphologyFlowersGynophore("reproductiveMorphology:flowers:gynophore"),
	reproductiveMorphologyFlowersHermaphrodite("reproductiveMorphology:flowers:hermaphrodite"),
	reproductiveMorphologyFlowersHypanthium("reproductiveMorphology:flowers:hypanthium"),
	reproductiveMorphologyFlowersLodicules("reproductiveMorphology:flowers:lodicules"),
	reproductiveMorphologyFlowersMale("reproductiveMorphology:flowers:male"),
	reproductiveMorphologyFlowersMaleAndroecium("reproductiveMorphology:flowers:male:androecium"),
	reproductiveMorphologyFlowersMaleBuds("reproductiveMorphology:flowers:male:buds"),
	reproductiveMorphologyFlowersMaleCalyx("reproductiveMorphology:flowers:male:calyx"),
	reproductiveMorphologyFlowersMaleCorolla("reproductiveMorphology:flowers:male:corolla"),
	reproductiveMorphologyFlowersNectaries("reproductiveMorphology:flowers:nectaries"),
	reproductiveMorphologyFlowersPedicel("reproductiveMorphology:flowers:pedicel"),
	reproductiveMorphologyFlowersPerianth("reproductiveMorphology:flowers:perianth"),
	reproductiveMorphologyFlowersReceptacle("reproductiveMorphology:flowers:receptacle"),
	reproductiveMorphologyFlowersSterileParts("reproductiveMorphology:flowers:sterileParts"),
	reproductiveMorphologyFruits("reproductiveMorphology:fruits"),
	reproductiveMorphologyGermination("reproductiveMorphology:germination"),
	reproductiveMorphologyInflorescences("reproductiveMorphology:inflorescences"),
	reproductiveMorphologyInflorescencesBracteole("reproductiveMorphology:inflorescences:bracteole"),
	reproductiveMorphologyInflorescencesBracts("reproductiveMorphology:inflorescences:bracts"),
	reproductiveMorphologyInflorescencesFemale("reproductiveMorphology:inflorescences:female"),
	reproductiveMorphologyInflorescencesMale("reproductiveMorphology:inflorescences:male"),
	reproductiveMorphologyInflorescencesPeduncle("reproductiveMorphology:inflorescences:peduncle"),
	reproductiveMorphologyInflorescencesScape("reproductiveMorphology:inflorescences:scape"),
	reproductiveMorphologyInflorescencesSpikelets("reproductiveMorphology:inflorescences:spikelets"),
	reproductiveMorphologyInflorescencesSpikeletsFemale("reproductiveMorphology:inflorescences:spikelets:female"),
	reproductiveMorphologyInflorescencesSpikeletsMale("reproductiveMorphology:inflorescences:spikelets:male"),
	reproductiveMorphologyInflorescencesSpikeletsPedicelled("reproductiveMorphology:inflorescences:spikelets:pedicelled"),
	reproductiveMorphologyInflorescencesSpikeletsSessile("reproductiveMorphology:inflorescences:spikelets:sessile"),
	reproductiveMorphologyInflorescencesSpikeletsSubsessile("reproductiveMorphology:inflorescences:spikelets:subsessile"),
	reproductiveMorphologyInfructescences("reproductiveMorphology:infructescences"),
	reproductiveMorphologySeeds("reproductiveMorphology:seeds"),
	reproductiveMorphologyStaminodes("reproductiveMorphology:staminodes"),
	rootMorphology("rootMorphology"),
	stemMorphology("stemMorphology"),
	stemMorphologyCladodes("stemMorphology:cladodes"),
	vegetativeMultiplication("vegetativeMultiplication"),
	vegetativeMultiplicationBulbils("vegetativeMultiplication:bulbils"),
	vegetativeMultiplicationBulbs("vegetativeMultiplication:bulbs"),
	vegetativeMultiplicationCorms("vegetativeMultiplication:corms"),
	vegetativeMultiplicationPseudobulbs("vegetativeMultiplication:pseudobulbs"),
	vegetativeMultiplicationRhizome("vegetativeMultiplication:rhizome"),
	vegetativeMultiplicationTubers("vegetativeMultiplication:tubers");

	public static final DescriptionType generalDescriptionType = habit;

	private String uri;

	private String term;

	private DescriptionType(final String newUri, String newTerm) {
		this.uri = newUri;
		this.term = newTerm;
	}

	private DescriptionType(final String newTerm) {
		this.term = newTerm;
	}

	/**
	 *
	 * @param uri The uri being converted into a Feature
	 * @return the matching feature or throw an IllegalArgumentException if no
	 *         feature matches
	 */
	public static DescriptionType fromString(final String string) {
		return lookup(string);
	}

	private static DescriptionType lookup(final String string) {
		for (DescriptionType f : DescriptionType.values()) {
			if ((f.uri != null && f.uri.equals(string)) || (f.term != null && f.term.equals(string))) {
				return f;
			}
		}
		throw new IllegalArgumentException(string
				+ " is not an acceptable value for DescriptionType");
	}
}