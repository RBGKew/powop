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

import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * @see http://rs.gbif.org/vocabulary/gbif/description_type.xml
 */
public enum DescriptionType {

	// standard gbif-extension types
	general("http://rs.gbif.org/vocabulary/gbif/descriptionType/general", "general"),
	diagnostic("http://rs.gbif.org/vocabulary/gbif/descriptionType/diagnostic", "diagnostic"),
	morphology("http://rs.gbif.org/vocabulary/gbif/descriptionType/morphology", "morphology"),
	habit("http://rs.gbif.org/vocabulary/gbif/descriptionType/habit", "morphology:general:habit"),
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
	figures("figures"),
	floralDiagram("floralDiagram"),
	glossary("glossary"),
	note("note"),
	reference("reference"),
	morphologyGeneral("morphology:general"),
	morphologyGeneralAreoles("morphology:general:areoles"),
	morphologyGeneralBuds("morphology:general:buds"),
	morphologyGeneralColleters("morphology:general:colleters"),
	morphologyGeneralCystoliths("morphology:general:cystoliths"),
	morphologyGeneralExudate("morphology:general:exudate"),
	morphologyGeneralGlands("morphology:general:glands"),
	morphologyGeneralHabit("morphology:general:habit"),
	morphologyGeneralIndumentum("morphology:general:indumentum"),
	morphologyGeneralSpines("morphology:general:spines"),
	morphologyGeneralTendrils("morphology:general:tendrils"),
	morphologyGeneralThallus("morphology:general:thallus"),
	morphologyGeneralTraps("morphology:general:traps"),
	morphologyLeaf("morphology:leaf"),
	morphologyLeafScaleLeaves("morphology:leaf:scaleLeaves"),
	morphologyLeafLamina("morphology:leaf:lamina"),
	morphologyLeafLaminaMargins("morphology:leaf:lamina:margins"),
	morphologyLeafLaminaVenation("morphology:leaf:lamina:venation"),
	morphologyLeafLeafSheaths("morphology:leaf:leaf-sheaths"),
	morphologyLeafLigule("morphology:leaf:ligule"),
	morphologyLeafLigules("morphology:leaf:ligules"),
	morphologyLeafOcrea("morphology:leaf:ocrea"),
	morphologyLeafPetiole("morphology:leaf:petiole"),
	morphologyLeafStipules("morphology:leaf:stipules"),
	morphologyReproductive("morphology:reproductive"),
	morphologyReproductiveCones("morphology:reproductive:cones"),
	morphologyReproductiveFlowers("morphology:reproductive:flowers"),
	morphologyReproductiveFlowersAndroecium("morphology:reproductive:flowers:androecium"),
	morphologyReproductiveFlowersAndrogynophore("morphology:reproductive:flowers:androgynophore"),
	morphologyReproductiveFlowersBracts("morphology:reproductive:flowers:bracts"),
	morphologyReproductiveFlowersBuds("morphology:reproductive:flowers:buds"),
	morphologyReproductiveFlowersCalyx("morphology:reproductive:flowers:calyx"),
	morphologyReproductiveFlowersColumn("morphology:reproductive:flowers:column"),
	morphologyReproductiveFlowersCorolla("morphology:reproductive:flowers:corolla"),
	morphologyReproductiveFlowersCorona("morphology:reproductive:flowers:corona"),
	morphologyReproductiveFlowersDisc("morphology:reproductive:flowers:disc"),
	morphologyReproductiveFlowersEpicalyx("morphology:reproductive:flowers:epicalyx"),
	morphologyReproductiveFlowersGynoecium("morphology:reproductive:flowers:gynoecium"),
	morphologyReproductiveFlowersGynophore("morphology:reproductive:flowers:gynophore"),
	morphologyReproductiveFlowersHypanthium("morphology:reproductive:flowers:hypanthium"),
	morphologyReproductiveFlowersLodicules("morphology:reproductive:flowers:lodicules"),
	morphologyReproductiveFlowersNectaries("morphology:reproductive:flowers:nectaries"),
	morphologyReproductiveFlowersPedicel("morphology:reproductive:flowers:pedicel"),
	morphologyReproductiveFlowersPerianth("morphology:reproductive:flowers:perianth"),
	morphologyReproductiveFlowersReceptacle("morphology:reproductive:flowers:receptacle"),
	morphologyReproductiveFlowersSterileParts("morphology:reproductive:flowers:sterileParts"),
	morphologyReproductiveFruits("morphology:reproductive:fruits"),
	morphologyReproductiveGermination("morphology:reproductive:germination"),
	morphologyReproductiveInflorescences("morphology:reproductive:inflorescences"),
	morphologyReproductiveInflorescencesBracts("morphology:reproductive:inflorescences:bracts"),
	morphologyReproductiveInflorescencesPeduncle("morphology:reproductive:inflorescences:peduncle"),
	morphologyReproductiveInflorescencesScape("morphology:reproductive:inflorescences:scape"),
	morphologyReproductiveInflorescencesSpikelets("morphology:reproductive:inflorescences:spikelets"),
	morphologyReproductiveInflorescencesSpikeletsPedicelled("morphology:reproductive:inflorescences:spikelets:pedicelled"),
	morphologyReproductiveInflorescencesSpikeletsSessile("morphology:reproductive:inflorescences:spikelets:sessile"),
	morphologyReproductiveInflorescencesSpikeletsSubsessile("morphology:reproductive:inflorescences:spikelets:subsessile"),
	morphologyReproductiveInfructescences("morphology:reproductive:infructescences"),
	morphologyReproductiveSeeds("morphology:reproductive:seeds"),
	morphologyRoot("morphology:root"),
	morphologyStem("morphology:stem"),
	morphologyStemCladodes("morphology:stem:cladodes"),
	vegetativeMultiplicationBulbils("vegetativeMultiplication:bulbils"),
	vegetativeMultiplicationBulbs("vegetativeMultiplication:bulbs"),
	vegetativeMultiplicationCorms("vegetativeMultiplication:corms"),
	vegetativeMultiplicationPseudobulbs("vegetativeMultiplication:pseudobulbs"),
	vegetativeMultiplicationRhizome("vegetativeMultiplication:rhizome"),
	vegetativeMultiplicationTubers("vegetativeMultiplication:tubers"),
	morphologyGeneralWood("morphology:general:wood"),
	vegetativeMultiplication("vegetativeMultiplication"),
	constructionalOrganisationGrowth("constructionalOrganisation:growth"),
	morphologyGeneralScales("morphology:general:scales"),

	// Second level non-standard types. They are separated so that they sort after everything else
	sexHermaphrodite("sex:hermaphrodite"),
	sexFemale("sex:female"),
	sexMale("sex:male");

	public static final DescriptionType generalDescriptionType = habit;

	public static final ImmutableMap<String, Set<DescriptionType>> searchCategories = ImmutableMap.<String, Set<DescriptionType>>builder()
			.put("appearance", ImmutableSet.<DescriptionType>of(
					morphologyGeneral,
					morphologyGeneralAreoles,
					morphologyGeneralBuds,
					morphologyGeneralColleters,
					morphologyGeneralCystoliths,
					morphologyGeneralExudate,
					morphologyGeneralGlands,
					morphologyGeneralHabit,
					morphologyGeneralIndumentum,
					morphologyGeneralSpines,
					morphologyGeneralTendrils,
					morphologyGeneralThallus,
					morphologyGeneralTraps,
					morphologyRoot,
					morphologyStem,
					morphologyStemCladodes,
					morphologyGeneralWood,
					vegetativeMultiplication,
					constructionalOrganisationGrowth))
			.put("inflorescence", ImmutableSet.<DescriptionType>of(
					morphologyReproductiveCones,
					morphologyReproductiveInflorescences,
					morphologyReproductiveInflorescencesBracts,
					morphologyReproductiveInflorescencesPeduncle,
					morphologyReproductiveInflorescencesScape,
					morphologyReproductiveInflorescencesSpikelets,
					morphologyReproductiveInflorescencesSpikeletsPedicelled,
					morphologyReproductiveInflorescencesSpikeletsSessile,
					morphologyReproductiveInflorescencesSpikeletsSubsessile))
			.put("fruit", ImmutableSet.<DescriptionType>of(
					morphologyReproductiveFruits,
					morphologyReproductiveInfructescences))
			.put("leaves", ImmutableSet.<DescriptionType>of(
					morphologyLeaf,
					morphologyLeafScaleLeaves,
					morphologyLeafLamina,
					morphologyLeafLaminaMargins,
					morphologyLeafLaminaVenation,
					morphologyLeafLeafSheaths,
					morphologyLeafLigule,
					morphologyLeafLigules,
					morphologyLeafOcrea,
					morphologyLeafPetiole,
					morphologyLeafStipules))
			.put("flower", ImmutableSet.<DescriptionType>of(
					morphologyReproductive,
					morphologyReproductiveFlowers,
					morphologyReproductiveFlowersAndroecium,
					morphologyReproductiveFlowersAndrogynophore,
					morphologyReproductiveFlowersBracts,
					morphologyReproductiveFlowersBuds,
					morphologyReproductiveFlowersCalyx,
					morphologyReproductiveFlowersColumn,
					morphologyReproductiveFlowersCorolla,
					morphologyReproductiveFlowersCorona,
					morphologyReproductiveFlowersEpicalyx,
					morphologyReproductiveFlowersGynoecium,
					morphologyReproductiveFlowersGynophore,
					morphologyReproductiveFlowersHypanthium,
					morphologyReproductiveFlowersLodicules,
					morphologyReproductiveFlowersNectaries,
					morphologyReproductiveFlowersPedicel,
					morphologyReproductiveFlowersPerianth,
					morphologyReproductiveFlowersReceptacle,
					morphologyReproductiveFlowersSterileParts,
					morphologyReproductiveFlowersDisc))
			.put("seed", ImmutableSet.<DescriptionType>of(
					morphologyReproductiveGermination,
					morphologyReproductiveSeeds))
			.put("vegitativePropagation", ImmutableSet.<DescriptionType>of(
					vegetativeMultiplicationBulbils,
					vegetativeMultiplicationBulbs,
					vegetativeMultiplicationCorms,
					vegetativeMultiplicationPseudobulbs,
					vegetativeMultiplicationRhizome,
					vegetativeMultiplicationTubers))
			.build();

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

	public boolean hasSearchCategory() {
		return getSearchCategory() != null;
	}

	public String getSearchCategory() {
		for(String category : searchCategories.keySet()) {
			if(searchCategories.get(category).contains(this)) {
				return category;
			}
		}

		return null;
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