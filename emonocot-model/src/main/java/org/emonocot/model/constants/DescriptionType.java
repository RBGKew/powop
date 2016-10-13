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

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

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
	morphologyGeneralBark("morphology:general:bark"),
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
	constructionalOrganisation("constructionalOrganisation"),
	morphologyGeneralScales("morphology:general:scales"),

	useAnimalFood("use:animalFood"),
	useAnimalFoodBees("use:animalFood:bees"),
	useAnimalFoodFlowers("use:animalFood:flowers"),
	useAnimalFoodFruits("use:animalFood:fruits"),
	useAnimalFoodGrainsCereals("use:animalFood:grainsCereals"),
	useAnimalFoodHerbage("use:animalFood:herbage"),
	useAnimalFoodOilsFats("use:animalFood:oilsFats"),
	useAnimalFoodOtherAnimalFoodType("use:animalFood:otherAnimalFoodType"),
	useAnimalFoodPulses("use:animalFood:pulses"),
	useAnimalFoodResiduesByProductsFromIndustry("use:animalFood:residuesByProductsFromIndustry"),
	useAnimalFoodRoots("use:animalFood:roots"),
	useAnimalFoodUnspecifiedAnimal("use:animalFood:unspecifiedAnimal"),
	useAnimalFoodUnspecifiedAnimalFoodType("use:animalFood:unspecifiedAnimalFoodType"),
	useEnvironmentalUse("use:environmentalUse"),
	useEnvironmentalUseAgroforestry("use:environmentalUse:agroforestry"),
	useEnvironmentalUseBoundaryBarrierSupportPlants("use:environmentalUse:boundaryBarrierSupportPlants"),
	useEnvironmentalUseCompanionPlants("use:environmentalUse:companionPlants"),
	useEnvironmentalUseErosionControl("use:environmentalUse:erosionControl"),
	useEnvironmentalUseFirebreaks("use:environmentalUse:firebreaks"),
	useEnvironmentalUseIndicatorPlants("use:environmentalUse:indicatorPlants"),
	useEnvironmentalUseOrnamentals("use:environmentalUse:ornamentals"),
	useEnvironmentalUseOtherEnvironmentalUses("use:environmentalUse:otherEnvironmentalUses"),
	useEnvironmentalUsePollutionControl("use:environmentalUse:pollutionControl"),
	useEnvironmentalUseRevegetation("use:environmentalUse:revegetation"),
	useEnvironmentalUseShadeShelter("use:environmentalUse:shadeShelter"),
	useEnvironmentalUseSoilImprovers("use:environmentalUse:soilImprovers"),
	useEnvironmentalUseUnspecifiedEnvironmentalUses("use:environmentalUse:unspecifiedEnvironmentalUses"),
	useFood("use:food"),
	useFoodCereals("use:food:cereals"),
	useFoodFoodAdditives("use:food:foodAdditives"),
	useFoodFruitsDessertFruits("use:food:fruitsDessertFruits"),
	useFoodGumsMucilagesResins("use:food:gumsMucilagesResins"),
	useFoodNuts("use:food:nuts"),
	useFoodOilsFats("use:food:oilsFats"),
	useFoodOtherFoodType("use:food:otherFoodType"),
	useFoodPseudocereals("use:food:pseudocereals"),
	useFoodPulses("use:food:pulses"),
	useFoodStarches("use:food:starches"),
	useFoodSugars("use:food:sugars"),
	useFoodUnspecifiedFood("use:food:unspecifiedFood"),
	useFoodUnspecifiedFoodType("use:food:unspecifiedFoodType"),
	useFoodVegetables("use:food:vegetables"),
	useTrade("trade"),
	useFuel("use:fuel"),
	useFuelCharcoal("use:fuel:charcoal"),
	useFuelFuelwood("use:fuel:fuelwood"),
	useFuelNonWoodyFuel("use:fuel:nonWoodyFuel"),
	useFuelPetroleumSubstitutesAlcoholsEtc("use:fuel:petroleumSubstitutesAlcoholsEtc"),
	useFuelTinder("use:fuel:tinder"),
	useFuelUnspecifiedFuelType("use:fuel:unspecifiedFuelType"),
	useMaterials("use:materials"),
	useMaterialsAlcohols("use:materials:alcohols"),
	useMaterialsCaneRattanBambooReedWickerEtc("use:materials:caneRattanBambooReedWickerEtc"),
	useMaterialsCorkCorkSubstitutes("use:materials:corkCorkSubstitutes"),
	useMaterialsEssentialOils("use:materials:essentialOils"),
	useMaterialsFibres("use:materials:fibres"),
	useMaterialsGumsResins("use:materials:gumsResins"),
	useMaterialsLatexRubber("use:materials:latexRubber"),
	useMaterialsLipidsFatsAndOils("use:materials:lipidsFatsAndOils"),
	useMaterialsOtherChemicals("use:materials:otherChemicals"),
	useMaterialsOtherMaterials("use:materials:otherMaterials"),
	useMaterialsTanninsDyestuffs("use:materials:tanninsDyestuffs"),
	useMaterialsUnspecifiedMaterialsChemicals("use:materials:unspecifiedMaterialsChemicals"),
	useMaterialsWaxes("use:materials:waxes"),
	useMaterialsWood("use:materials:wood"),
	useMedicines("use:medicines"),
	useMedicinesAbnormalities("use:medicines:abnormalities"),
	useMedicinesAntifertiliyAgent("use:medicines:antifertiliyAgent"),
	useMedicinesBloodSystemDisorders("use:medicines:bloodSystemDisorders"),
	useMedicinesCirculatorySystemDisorders("use:medicines:circulatorySystemDisorders"),
	useMedicinesDigestiveSystemDisorders("use:medicines:digestiveSystemDisorders"),
	useMedicinesEndocrineSystemDisorders("use:medicines:endocrineSystemDisorders"),
	useMedicinesGenitourinarySystemDisorders("use:medicines:genitourinarySystemDisorders"),
	useMedicinesIllDefinedSymptoms("use:medicines:illDefinedSymptoms"),
	useMedicinesImmuneSystemDisorders("use:medicines:immuneSystemDisorders"),
	useMedicinesInfectionsInfestations("use:medicines:infectionsInfestations"),
	useMedicinesInflammation("use:medicines:inflammation"),
	useMedicinesInjuries("use:medicines:injuries"),
	useMedicinesLocalCulturalDisorders("use:medicines:localCulturalDisorders"),
	useMedicinesMentalDisorders("use:medicines:mentalDisorders"),
	useMedicinesMetabolicSystemDisorders("use:medicines:metabolicSystemDisorders"),
	useMedicinesMuscularSkeletalSystemDisorders("use:medicines:muscularSkeletalSystemDisorders"),
	useMedicinesNeoplasms("use:medicines:neoplasms"),
	useMedicinesNervousSystemDisorders("use:medicines:nervousSystemDisorders"),
	useMedicinesNutritionalDisorders("use:medicines:nutritionalDisorders"),
	useMedicinesOtherMedicinalDisorders("use:medicines:otherMedicinalDisorders"),
	useMedicinesPain("use:medicines:pain"),
	useMedicinesPoisonings("use:medicines:poisonings"),
	useMedicinesPregnancyBirthPuerpueriumDisorders("use:medicines:pregnancyBirthPuerpueriumDisorders"),
	useMedicinesRespiratorySystemDisorders("use:medicines:respiratorySystemDisorders"),
	useMedicinesSensorySystemDisorders("use:medicines:sensorySystemDisorders"),
	useMedicinesSkinSubcutaneousCellularTissueDisorders("use:medicines:skinSubcutaneousCellularTissueDisorders"),
	useMedicinesUnspecifiedMedicinalDisorders("use:medicines:unspecifiedMedicinalDisorders"),
	usePoisons("use:poisons"),
	usePoisonsAnimalDiseaseControl("use:poisons:animalDiseaseControl"),
	usePoisonsAnimalPestControl("use:poisons:animalPestControl"),
	usePoisonsAnthelmintic("use:poisons:anthelmintic"),
	usePoisonsAntibacterial("use:poisons:antibacterial"),
	usePoisonsAntiprotozoal("use:poisons:antiprotozoal"),
	usePoisonsAntiviral("use:poisons:antiviral"),
	usePoisonsFishing("use:poisons:fishing"),
	usePoisonsFishPoison("use:poisons:fishPoison"),
	usePoisonsHomicide("use:poisons:homicide"),
	usePoisonsHunting("use:poisons:hunting"),
	usePoisonsInfanticide("use:poisons:infanticide"),
	usePoisonsInsecticidal("use:poisons:insecticidal"),
	usePoisonsMammalianPoison("use:poisons:mammalianPoison"),
	usePoisonsOtherPoisonType("use:poisons:otherPoisonType"),
	usePoisonsOtherUsesOfPoisons("use:poisons:otherUsesOfPoisons"),
	usePoisonsPlantDiseaseControl("use:poisons:plantDiseaseControl"),
	usePoisonsPlantPestControl("use:poisons:plantPestControl"),
	usePoisonsPoisonousPropertyNotActivelyUsed("use:poisons:poisonousPropertyNotActivelyUsed"),
	usePoisonsReptilePoison("use:poisons:reptilePoison"),
	usePoisonsSuicide("use:poisons:suicide"),
	usePoisonsUnspecifiedPoisonType("use:poisons:unspecifiedPoisonType"),
	useSocialUse("use:socialUse"),
	useSocialUseAdulterants("use:socialUse:adulterants"),
	useSocialUseHallucinogens("use:socialUse:hallucinogens"),
	useSocialUseMasticatories("use:socialUse:masticatories"),
	useSocialUseOrnamentals("use:socialUse:ornamentals"),
	useSocialUseOtherSocialUses("use:socialUse:otherSocialUses"),
	useSocialUseSacredSpiritualPlants("use:socialUse:sacredSpiritualPlants"),
	useSocialUseSedatives("use:socialUse:sedatives"),
	useSocialUseSmokingMaterials("use:socialUse:smokingMaterials"),
	useSocialUseSnuff("use:socialUse:snuff"),
	useSocialUseStimulants("use:socialUse:stimulants"),
	useSocialUseUnspecifiedSocialUses("use:socialUse:unspecifiedSocialUses"),
	useWeedImpact("use:weedImpact"),
	useWeedImpactAgriculturalWeed("use:weedImpact:agriculturalWeed"),
	useWeedImpactRangelAndPastureWeed("use:weedImpact:rangelAndPastureWeed"),
	useWeedImpactUnspecifiedWeed("use:weedImpact:unspecifiedWeed"),


	// Second level non-standard types. They are separated so that they sort after everything else
	sexHermaphrodite("sex:hermaphrodite"),
	sexFemale("sex:female"),
	sexMale("sex:male"),
	sex("sex");

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

	public boolean isA(DescriptionType type) {
		return getAll(type).contains(this);
	}

	public String getSearchCategory() {
		for(String category : searchCategories.keySet()) {
			if(searchCategories.get(category).contains(this)) {
				return category;
			}
		}

		return null;
	}

	private static Set<DescriptionType> AllTypes = ImmutableSet.copyOf(values());
	private static LoadingCache<DescriptionType, Set<DescriptionType>> sublists = CacheBuilder.newBuilder()
			.maximumSize(1000)
			.build(
					new CacheLoader<DescriptionType, Set<DescriptionType>>() {
						@Override
						public Set<DescriptionType> load(DescriptionType type) {
							return new HashSet<DescriptionType>();
						}
					});

	public static Set<DescriptionType> getAll(final DescriptionType type){
		for(DescriptionType description : DescriptionType.values()){
			if(description.term.startsWith(type.term)){
				sublists.getUnchecked(type).add(description);
			}
		}
		return sublists.getUnchecked(type);
	}

	public static Set<DescriptionType> getAll(final DescriptionType type, final DescriptionType excluding) {
		return Sets.difference(getAll(type), getAll(excluding));
	}

	public static Set<DescriptionType> getAllExcept(final DescriptionType excluding) {
		return Sets.difference(AllTypes, getAll(excluding));
	}

	private static DescriptionType lookup(final String string) {
		for (DescriptionType f : DescriptionType.values()) {
			if ((f.uri != null && f.uri.equals(string)) || (f.term != null && f.term.equals(string))) {
				return f;
			}
		}
		throw new IllegalArgumentException(string + " is not an acceptable value for DescriptionType");
	}
}