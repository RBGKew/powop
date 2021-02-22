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
package org.powo.model.constants;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
	habit("http://rs.gbif.org/vocabulary/gbif/descriptionType/habit", "habit"),
	cytology("http://rs.gbif.org/vocabulary/gbif/descriptionType/cytology", "cytology"),
	physiology("http://rs.gbif.org/vocabulary/gbif/descriptionType/physiology", "physiology"),
	size("http://rs.gbif.org/vocabulary/gbif/descriptionType/size", "size"),
	weight("http://rs.gbif.org/vocabulary/gbif/descriptionType/weight", "weight"),
	lifespan("http://rs.gbif.org/vocabulary/gbif/descriptionType/lifespan", "lifespan"),
	lifetime("http://rs.gbif.org/vocabulary/gbif/descriptionType/lifetime", "lifetime"),
	biology("http://rs.gbif.org/vocabulary/gbif/descriptionType/biology", "biology"),
	ecology("http://rs.gbif.org/vocabulary/gbif/descriptionType/ecology", "ecology"),
	reproduction("http://rs.gbif.org/vocabulary/gbif/descriptionType/reproduction", "reproduction"),
	conservation("http://rs.gbif.org/vocabulary/gbif/descriptionType/conservation", "conservation"),
	use("http://rs.gbif.org/vocabulary/gbif/descriptionType/use", "use"),
	dispersal( "http://rs.gbif.org/vocabulary/gbif/descriptionType/dispersal", "dispersal"),
	propagation("propagation"),
	yield("yield"),
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
	typematerial("http://rs.gbif.org/vocabulary/gbif/descriptionType/typematerial", "typematerial"),
	typelocality("http://rs.gbif.org/vocabulary/gbif/descriptionType/typelocality", "typelocality"),
	phylogeny("http://rs.gbif.org/vocabulary/gbif/descriptionType/phylogeny", "phylogeny"),
	hybrids("http://rs.gbif.org/vocabulary/gbif/descriptionType/hybrids", "hybrids"),
	culture("http://rs.gbif.org/vocabulary/gbif/descriptionType/culture", "culture"),
	vernacular("http://rs.gbif.org/vocabulary/gbif/descriptionType/vernacular", "vernacular"),
	// non-standard description types
	concept("concept"),
	diagnosis("diagnosis"),
	figure("figure"),
	floralDiagram("floralDiagram"),
	glossary("glossary"),
	hazard("hazard"),
	note("note"),
	phenology("phenology"),
	reference("reference"),
	snippet("snippet"),
	speciesProfile("speciesProfile"),
	summary("summary"),
	type("type"),

	// Morphological
	constructionalOrganisationGrowth("constructionalOrganisation:growth"),
	morphologyGeneral("morphology:general"),
	morphologyGeneralAreole("morphology:general:areole"),
	morphologyGeneralBark("morphology:general:bark"),
	morphologyGeneralBud("morphology:general:bud"),
	morphologyGeneralColleter("morphology:general:colleter"),
	morphologyGeneralCrown("morphology:general:crown"),
	morphologyGeneralCystolith("morphology:general:cystolith"),
	morphologyGeneralDomatium("morphology:general:domatium"),
	morphologyGeneralExudate("morphology:general:exudate"),
	morphologyGeneralFloat("morphology:general:float"),
	morphologyGeneralGall("morphology:general:gall"),
	morphologyGeneralGametophyte("morphology:general:gametophyte"),
	morphologyGeneralGland("morphology:general:gland"),
	morphologyGeneralHabit("morphology:general:habit"),
	morphologyGeneralHaustorium("morphology:general:haustorium"),
	morphologyGeneralHeight("morphology:general:height"),
	morphologyGeneralIndumentum("morphology:general:indumentum"),
	morphologyGeneralPrickle("morphology:general:prickle"),
	morphologyGeneralRhizoid("morphology:general:rhizoid"),
	morphologyGeneralScale("morphology:general:scale"),
	morphologyGeneralShoot("morphology:general:shoot"),
	morphologyGeneralSpine("morphology:general:spine"),
	morphologyGeneralSporophyte("morphology:general:sporophyte"),
	morphologyGeneralSquamule("morphology:general:squamule"),
	morphologyGeneralTendril("morphology:general:tendril"),
	morphologyGeneralThallus("morphology:general:thallus"),
	morphologyGeneralThorn("morphology:general:thorn"),
	morphologyGeneralTrap("morphology:general:trap"),
	morphologyGeneralWood("morphology:general:wood"),
	morphologyLeaf("morphology:leaf"),
	morphologyLeafCataphyll("morphology:leaf:cataphyll"),
	morphologyLeafLamina("morphology:leaf:lamina"),
	morphologyLeafLaminaMargin("morphology:leaf:lamina:margin"),
	morphologyLeafLeaflet("morphology:leaf:leaflet"),
	morphologyLeafLigule("morphology:leaf:ligule"),
	morphologyLeafOcrea("morphology:leaf:ocrea"),
	morphologyLeafPetiole("morphology:leaf:petiole"),
	morphologyLeafPhyllode("morphology:leaf:phyllode"),
	morphologyLeafPinna("morphology:leaf:pinna"),
	morphologyLeafPinnule("morphology:leaf:pinnule"),
	morphologyLeafProphyll("morphology:leaf:prophyll"),
	morphologyLeafRachis("morphology:leaf:rachis"),
	morphologyLeafScaleLeaf("morphology:leaf:scaleLeaf"),
	morphologyLeafSheath("morphology:leaf:sheath"),
	morphologyLeafStipe("morphology:leaf:stipe"),
	morphologyLeafStipel("morphology:leaf:stipel"),
	morphologyLeafStipule("morphology:leaf:stipule"),
	morphologyLeafVenation("morphology:leaf:venation"),
	morphologyReproductive("morphology:reproductive"),
	morphologyReproductiveCone("morphology:reproductive:cone"),
	morphologyReproductiveFlower("morphology:reproductive:flower"),
	morphologyReproductiveFlowerAndroecium("morphology:reproductive:flower:androecium"),
	morphologyReproductiveFlowerAndroeciumStamen("morphology:reproductive:flower:androecium:stamen"),
	morphologyReproductiveFlowerAndroeciumStamenAnther("morphology:reproductive:flower:androecium:stamen:anther"),
	morphologyReproductiveFlowerAndroeciumStamenAntherConnective("morphology:reproductive:flower:androecium:stamen:anther:connective"),
	morphologyReproductiveFlowerAndroeciumStamenFilament("morphology:reproductive:flower:androecium:stamen:filament"),
	morphologyReproductiveFlowerAndroeciumStaminode("morphology:reproductive:flower:androecium:staminode"),
	morphologyReproductiveFlowerAndrogynophore("morphology:reproductive:flower:androgynophore"),
	morphologyReproductiveFlowerAndrophore("morphology:reproductive:flower:androphore"),
	morphologyReproductiveFlowerCallus("morphology:reproductive:flower:callus"),
	morphologyReproductiveFlowerCalyx("morphology:reproductive:flower:calyx"),
	morphologyReproductiveFlowerCarpophore("morphology:reproductive:flower:carpophore"),
	morphologyReproductiveFlowerCleistogene("morphology:reproductive:flower:cleistogene"),
	morphologyReproductiveFlowerColumn("morphology:reproductive:flower:column"),
	morphologyReproductiveFlowerCorolla("morphology:reproductive:flower:corolla"),
	morphologyReproductiveFlowerCorona("morphology:reproductive:flower:corona"),
	morphologyReproductiveFlowerDisc("morphology:reproductive:flower:disc"),
	morphologyReproductiveFlowerEpicalyx("morphology:reproductive:flower:epicalyx"),
	morphologyReproductiveFlowerFloret("morphology:reproductive:flower:floret"),
	morphologyReproductiveFlowerGynoecium("morphology:reproductive:flower:gynoecium"),
	morphologyReproductiveFlowerGynoeciumCarpel("morphology:reproductive:flower:gynoecium:carpel"),
	morphologyReproductiveFlowerGynoeciumOvary("morphology:reproductive:flower:gynoecium:ovary"),
	morphologyReproductiveFlowerGynoeciumOvaryOvule("morphology:reproductive:flower:gynoecium:ovary:ovule"),
	morphologyReproductiveFlowerGynoeciumOvaryPlacenta("morphology:reproductive:flower:gynoecium:ovary:placenta"),
	morphologyReproductiveFlowerGynoeciumPistil("morphology:reproductive:flower:gynoecium:pistil"),
	morphologyReproductiveFlowerGynoeciumPistillode("morphology:reproductive:flower:gynoecium:pistillode"),
	morphologyReproductiveFlowerGynoeciumStigma("morphology:reproductive:flower:gynoecium:stigma"),
	morphologyReproductiveFlowerGynoeciumStyle("morphology:reproductive:flower:gynoecium:style"),
	morphologyReproductiveFlowerGynoeciumStylopodium("morphology:reproductive:flower:gynoecium:stylopodium"),
	morphologyReproductiveFlowerGynophore("morphology:reproductive:flower:gynophore"),
	morphologyReproductiveFlowerGynostegium("morphology:reproductive:flower:gynostegium"),
	morphologyReproductiveFlowerGynostemium("morphology:reproductive:flower:gynostemium"),
	morphologyReproductiveFlowerHypanthium("morphology:reproductive:flower:hypanthium"),
	morphologyReproductiveFlowerLabellum("morphology:reproductive:flower:labellum"),
	morphologyReproductiveFlowerLabellumAuricle("morphology:reproductive:flower:labellum:auricle"),
	morphologyReproductiveFlowerLimen("morphology:reproductive:flower:limen"),
	morphologyReproductiveFlowerLodicule("morphology:reproductive:flower:lodicule"),
	morphologyReproductiveFlowerNectary("morphology:reproductive:flower:nectary"),
	morphologyReproductiveFlowerOperculum("morphology:reproductive:flower:operculum"),
	morphologyReproductiveFlowerPedicel("morphology:reproductive:flower:pedicel"),
	morphologyReproductiveFlowerPerianth("morphology:reproductive:flower:perianth"),
	morphologyReproductiveFlowerPollen("morphology:reproductive:flower:pollen"),
	morphologyReproductiveFlowerPollinarium("morphology:reproductive:flower:pollinarium"),
	morphologyReproductiveFlowerPollinium("morphology:reproductive:flower:pollinium"),
	morphologyReproductiveFlowerPresenter("morphology:reproductive:flower:presenter"),
	morphologyReproductiveFlowerReceptacle("morphology:reproductive:flower:receptacle"),
	morphologyReproductiveFlowerRostellum("morphology:reproductive:flower:rostellum"),
	morphologyReproductiveFlowerSpur("morphology:reproductive:flower:spur"),
	morphologyReproductiveFlowerSterilePart("morphology:reproductive:flower:sterilePart"),
	morphologyReproductiveFlowerTepal("morphology:reproductive:flower:tepal"),
	morphologyReproductiveFlowerViscidium("morphology:reproductive:flower:viscidium"),
	morphologyReproductiveInflorescence("morphology:reproductive:inflorescence"),
	morphologyReproductiveInflorescenceBract("morphology:reproductive:inflorescence:bract"),
	morphologyReproductiveInflorescenceBractGlume("morphology:reproductive:inflorescence:bract:glume"),
	morphologyReproductiveInflorescenceBractInvolucre("morphology:reproductive:inflorescence:bract:involucre"),
	morphologyReproductiveInflorescenceBractLemma("morphology:reproductive:inflorescence:bract:lemma"),
	morphologyReproductiveInflorescenceBractPalea("morphology:reproductive:inflorescence:bract:palea"),
	morphologyReproductiveInflorescenceBractPhyllary("morphology:reproductive:inflorescence:bract:phyllary"),
	morphologyReproductiveInflorescenceBractProbract("morphology:reproductive:inflorescence:bract:probract"),
	morphologyReproductiveInflorescenceBractSpathe("morphology:reproductive:inflorescence:bract:spathe"),
	morphologyReproductiveInflorescenceBracteole("morphology:reproductive:inflorescence:bracteole"),
	morphologyReproductiveInflorescenceCapitulum("morphology:reproductive:inflorescence:capitulum"),
	morphologyReproductiveInflorescenceCyathium("morphology:reproductive:inflorescence:cyathium"),
	morphologyReproductiveInflorescencePeduncle("morphology:reproductive:inflorescence:peduncle"),
	morphologyReproductiveInflorescenceScape("morphology:reproductive:inflorescence:scape"),
	morphologyReproductiveInflorescenceSpadix("morphology:reproductive:inflorescence:spadix"),
	morphologyReproductiveInflorescenceSpikelet("morphology:reproductive:inflorescence:spikelet"),
	morphologyReproductiveInflorescenceSynflorescence("morphology:reproductive:inflorescence:synflorescence"),
	morphologyReproductiveFruit("morphology:reproductive:fruit"),
	morphologyReproductiveFruitCalyx("morphology:reproductive:fruit:calyx"),
	morphologyReproductiveFruitInfructescence("morphology:reproductive:fruit:infructescence"),
	morphologyReproductiveFruitPyrene("morphology:reproductive:fruit:pyrene"),
	morphologyReproductiveFruitVitta("morphology:reproductive:fruit:vitta"),
	morphologyReproductiveSeed("morphology:reproductive:seed"),
	morphologyReproductiveSeedAril("morphology:reproductive:seed:aril"),
	morphologyReproductiveSeedCotyledon("morphology:reproductive:seed:cotyledon"),
	morphologyReproductiveSeedEmbryo("morphology:reproductive:seed:embryo"),
	morphologyReproductiveSeedEndosperm("morphology:reproductive:seed:endosperm"),
	morphologyReproductiveSeedGermination("morphology:reproductive:seed:germination"),
	morphologyReproductiveSeedGerminationVivipary("morphology:reproductive:seed:germination:vivipary"),
	morphologyReproductiveSeedHilum("morphology:reproductive:seed:hilum"),
	morphologyReproductiveSeedHypocotyl("morphology:reproductive:seed:hypocotyl"),
	morphologyReproductiveSeedPappus("morphology:reproductive:seed:pappus"),
	morphologyReproductiveSeedTesta("morphology:reproductive:seed:testa"),
	morphologyRoot("morphology:root"),
	morphologyStem("morphology:stem"),
	morphologyStemCladode("morphology:stem:cladode"),
	morphologyTrunk("morphology:trunk"),
	morphologyTrunkButtress("morphology:trunk:buttress"),
	morphologyTrunkSlash("morphology:trunk:slash"),
	morphologyTwig("morphology:twig"),
	morphologyBole("morphology:bole"),
	morphologyBranch("morphology:branch"),
	morphologyCulm("morphology:culm"),
	vegetativeMultiplication("vegetativeMultiplication"),
	vegetativeMultiplicationBulb("vegetativeMultiplication:bulb"),
	vegetativeMultiplicationBulbil("vegetativeMultiplication:bulbil"),
	vegetativeMultiplicationCorm("vegetativeMultiplication:corm"),
	vegetativeMultiplicationGemma("vegetativeMultiplication:gemma"),
	vegetativeMultiplicationPseudobulb("vegetativeMultiplication:pseudobulb"),
	vegetativeMultiplicationRhizome("vegetativeMultiplication:rhizome"),
	vegetativeMultiplicationStolon("vegetativeMultiplication:stolon"),
	vegetativeMultiplicationTuber("vegetativeMultiplication:tuber"),
	morphologyStemCaudex("morphology:stem:caudex"),
	morphologyGeneralVelum("morphology:general:velum"),
	morphologyReproductiveMegaspore("morphology:reproductive:megaspore"),
	morphologyReproductiveMegasporocarp("morphology:reproductive:megasporocarp"),
	morphologyReproductiveMegasporophyll("morphology:reproductive:megasporophyll"),
	morphologyReproductiveMicrospore("morphology:reproductive:microspore"),
	morphologyReproductiveMicrosporocarp("morphology:reproductive:microsporocarp"),
	morphologyReproductiveMicrosporophyll("morphology:reproductive:microsporophyll"),
	morphologyReproductiveParaphysis("morphology:reproductive:paraphysis"),
	morphologyReproductiveSorus("morphology:reproductive:sorus"),
	morphologyReproductiveSorusIndusium("morphology:reproductive:sorus:indusium"),
	morphologyReproductiveSporangium("morphology:reproductive:sporangium"),
	morphologyReproductiveSporangiumAnnulus("morphology:reproductive:sporangium:annulus"),
	morphologyReproductiveSpore("morphology:reproductive:spore"),
	morphologyReproductiveSporocarp("morphology:reproductive:sporocarp"),
	morphologyReproductiveSporophore("morphology:reproductive:sporophore"),
	morphologyReproductiveSporophyll("morphology:reproductive:sporophyll"),
	morphologyReproductiveStrobilus("morphology:reproductive:strobilus"),
	morphologyReproductiveSynangium("morphology:reproductive:synangium"),
	morphologyGeneralRhizoidAndStolon("morphology:general:rhizoidAndStolon"),
	lifeStageSeedling("lifeStage:seedling"),
	morphologyReproductiveFlowerAndroeciumStamenAndStyle("morphology:reproductive:flower:androecium:stamenAndStyle"),
	morphologyReproductiveFlowerPedicelAndOvary("morphology:reproductive:flower:pedicelAndOvary"),
	morphologyReproductiveFlowerPedicelAndPeduncle("morphology:reproductive:flower:pedicelAndPeduncle"),

	// Usage description types
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
	// Simplified TDWG Category: Food > Preparations Used In > Beverages
	useFoodBeverages("use:food:beverages"),
	useFoodCereals("use:food:cereals"),
	useFoodFoodAdditives("use:food:foodAdditives"),
	useFoodFruitsDessertFruits("use:food:fruitsDessertFruits"),
	useFoodGumsMucilagesResins("use:food:gumsMucilagesResins"),
	// New TDWG Category for Fungi: Food > Mushrooms
	useFoodMushrooms("use:food:mushrooms"),
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
	useGeneSources("use:geneSources"),
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
	cyclicity("http://rs.gbif.org/vocabulary/gbif/descriptionType/cyclicity", "cyclicity"),
	habitat("http://rs.gbif.org/vocabulary/gbif/descriptionType/habitat", "habitat"),
	distribution("http://rs.gbif.org/vocabulary/gbif/descriptionType/distribution", "distribution"),
	threats("http://rs.gbif.org/vocabulary/gbif/descriptionType/threats", "threats"),
	literature("http://rs.gbif.org/vocabulary/gbif/descriptionType/literature", "literature"),

	// Second level non-standard types. They are separated so that they sort after everything else
	morphDisc("morph:disc"),
	morphHomogamous("morph:homogamous"),
	morphInferior("morph:inferior"),
	morphIntermediateStyled("morph:intermediateStyled"),
	morphLongStaminate("morph:longStaminate"),
	morphLongStyled("morph:longStyled"),
	morphPedicelled("morph:pedicelled"),
	morphRay("morph:ray"),
	morphSessile("morph:sessile"),
	morphShortStaminate("morph:shortStaminate"),
	morphShortStyled("morph:shortStyled"),
	morphSubsessile("morph:subsessile"),
	morphSuperior("morph:superior"),
	sexFemale("sex:female"),
	sexMale("sex:male"),
	sexHermaphrodite("sex:hermaphrodite"),
	sexFemaleAndHermaphrodite("sex:femaleAndHermaphrodite"),
	sexMaleAndHermaphrodite("sex:maleAndHermaphrodite"),
	statusFertile("status:fertile"),
	statusSterile("status:sterile"),
	sex("sex");

	public static final DescriptionType generalDescriptionType = habit;

	public static final ImmutableMap<String, Set<DescriptionType>> searchCategories = ImmutableMap.<String, Set<DescriptionType>>builder()
			.put("general", ImmutableSet.<DescriptionType>of(
					conservation,
					distribution,
					ecology,
					general,
					hazard,
					note,
					reference,
					snippet,
					speciesProfile,
					summary))
			.put("appearance", ImmutableSet.<DescriptionType>of(
					constructionalOrganisationGrowth,
					diagnosis,
					diagnostic,
					morphologyGeneral,
					morphologyGeneralAreole,
					morphologyGeneralBark,
					morphologyGeneralBud,
					morphologyGeneralColleter,
					morphologyGeneralCrown,
					morphologyGeneralCystolith,
					morphologyGeneralDomatium,
					morphologyGeneralExudate,
					morphologyGeneralFloat,
					morphologyGeneralGall,
					morphologyGeneralGametophyte,
					morphologyGeneralGland,
					morphologyGeneralHabit,
					morphologyGeneralHaustorium,
					morphologyGeneralHeight,
					morphologyGeneralIndumentum,
					morphologyGeneralPrickle,
					morphologyGeneralRhizoid,
					morphologyGeneralScale,
					morphologyGeneralShoot,
					morphologyGeneralSpine,
					morphologyGeneralSporophyte,
					morphologyGeneralSquamule,
					morphologyGeneralTendril,
					morphologyGeneralThallus,
					morphologyGeneralThorn,
					morphologyGeneralTrap,
					morphologyGeneralWood,
					morphologyReproductive,
					morphologyReproductiveCone,
					morphologyRoot,
					morphologyStem,
					morphologyStemCladode,
					morphologyTrunk,
					morphologyTrunkButtress,
					morphologyTrunkSlash,
					morphologyTwig,
					morphologyBole,
					morphologyBranch,
					morphologyCulm,
					morphologyStemCaudex,
					morphologyGeneralVelum,
					morphologyReproductiveMegaspore,
					morphologyReproductiveMegasporocarp,
					morphologyReproductiveMegasporophyll,
					morphologyReproductiveMicrospore,
					morphologyReproductiveMicrosporocarp,
					morphologyReproductiveMicrosporophyll,
					morphologyReproductiveParaphysis,
					morphologyReproductiveSorus,
					morphologyReproductiveSorusIndusium,
					morphologyReproductiveSporangium,
					morphologyReproductiveSporangiumAnnulus,
					morphologyReproductiveSpore,
					morphologyReproductiveSporocarp,
					morphologyReproductiveSporophore,
					morphologyReproductiveSporophyll,
					morphologyReproductiveStrobilus,
					morphologyReproductiveSynangium,
					morphologyGeneralRhizoidAndStolon))
			.put("flower", ImmutableSet.<DescriptionType>of(
					floralDiagram,
					morphologyReproductiveFlower,
					morphologyReproductiveFlowerAndroecium,
					morphologyReproductiveFlowerAndroeciumStamen,
					morphologyReproductiveFlowerAndroeciumStamenAnther,
					morphologyReproductiveFlowerAndroeciumStamenAntherConnective,
					morphologyReproductiveFlowerAndroeciumStamenFilament,
					morphologyReproductiveFlowerAndroeciumStaminode,
					morphologyReproductiveFlowerAndrogynophore,
					morphologyReproductiveFlowerAndrophore,
					morphologyReproductiveFlowerCallus,
					morphologyReproductiveFlowerCalyx,
					morphologyReproductiveFlowerCarpophore,
					morphologyReproductiveFlowerCleistogene,
					morphologyReproductiveFlowerColumn,
					morphologyReproductiveFlowerCorolla,
					morphologyReproductiveFlowerCorona,
					morphologyReproductiveFlowerDisc,
					morphologyReproductiveFlowerEpicalyx,
					morphologyReproductiveFlowerFloret,
					morphologyReproductiveFlowerGynoecium,
					morphologyReproductiveFlowerGynoeciumCarpel,
					morphologyReproductiveFlowerGynoeciumOvary,
					morphologyReproductiveFlowerGynoeciumOvaryOvule,
					morphologyReproductiveFlowerGynoeciumOvaryPlacenta,
					morphologyReproductiveFlowerGynoeciumPistil,
					morphologyReproductiveFlowerGynoeciumPistillode,
					morphologyReproductiveFlowerGynoeciumStigma,
					morphologyReproductiveFlowerGynoeciumStyle,
					morphologyReproductiveFlowerGynoeciumStylopodium,
					morphologyReproductiveFlowerGynophore,
					morphologyReproductiveFlowerGynostegium,
					morphologyReproductiveFlowerGynostemium,
					morphologyReproductiveFlowerHypanthium,
					morphologyReproductiveFlowerLabellum,
					morphologyReproductiveFlowerLabellumAuricle,
					morphologyReproductiveFlowerLimen,
					morphologyReproductiveFlowerLodicule,
					morphologyReproductiveFlowerNectary,
					morphologyReproductiveFlowerOperculum,
					morphologyReproductiveFlowerPedicel,
					morphologyReproductiveFlowerPerianth,
					morphologyReproductiveFlowerPollen,
					morphologyReproductiveFlowerPollinarium,
					morphologyReproductiveFlowerPollinium,
					morphologyReproductiveFlowerPresenter,
					morphologyReproductiveFlowerReceptacle,
					morphologyReproductiveFlowerRostellum,
					morphologyReproductiveFlowerSpur,
					morphologyReproductiveFlowerSterilePart,
					morphologyReproductiveFlowerTepal,
					morphologyReproductiveFlowerViscidium,
					morphologyReproductiveFlowerAndroeciumStamenAndStyle,
					morphologyReproductiveFlowerPedicelAndOvary,
					morphologyReproductiveFlowerPedicelAndPeduncle,
					phenology))
			.put("leaf", ImmutableSet.<DescriptionType>of(
					morphologyLeaf,
					morphologyLeafCataphyll,
					morphologyLeafLamina,
					morphologyLeafLaminaMargin,
					morphologyLeafLeaflet,
					morphologyLeafLigule,
					morphologyLeafOcrea,
					morphologyLeafPetiole,
					morphologyLeafPhyllode,
					morphologyLeafPinna,
					morphologyLeafPinnule,
					morphologyLeafProphyll,
					morphologyLeafRachis,
					morphologyLeafScaleLeaf,
					morphologyLeafSheath,
					morphologyLeafStipe,
					morphologyLeafStipel,
					morphologyLeafStipule,
					morphologyLeafVenation))
			.put("inflorescence", ImmutableSet.<DescriptionType>of(
					morphologyReproductiveInflorescence,
					morphologyReproductiveInflorescenceBract,
					morphologyReproductiveInflorescenceBractGlume,
					morphologyReproductiveInflorescenceBractInvolucre,
					morphologyReproductiveInflorescenceBractLemma,
					morphologyReproductiveInflorescenceBractPalea,
					morphologyReproductiveInflorescenceBractPhyllary,
					morphologyReproductiveInflorescenceBractProbract,
					morphologyReproductiveInflorescenceBractSpathe,
					morphologyReproductiveInflorescenceBracteole,
					morphologyReproductiveInflorescenceCapitulum,
					morphologyReproductiveInflorescenceCyathium,
					morphologyReproductiveInflorescencePeduncle,
					morphologyReproductiveInflorescenceScape,
					morphologyReproductiveInflorescenceSpadix,
					morphologyReproductiveInflorescenceSpikelet,
					morphologyReproductiveInflorescenceSynflorescence))
			.put("fruit", ImmutableSet.<DescriptionType>of(
					morphologyReproductiveFruit,
					morphologyReproductiveFruitCalyx,
					morphologyReproductiveFruitInfructescence,
					morphologyReproductiveFruitPyrene,
					morphologyReproductiveFruitVitta))
			.put("seed", ImmutableSet.<DescriptionType>of(
					morphologyReproductiveSeed,
					morphologyReproductiveSeedAril,
					morphologyReproductiveSeedCotyledon,
					morphologyReproductiveSeedEmbryo,
					morphologyReproductiveSeedEndosperm,
					morphologyReproductiveSeedGermination,
					morphologyReproductiveSeedGerminationVivipary,
					morphologyReproductiveSeedHilum,
					morphologyReproductiveSeedHypocotyl,
					morphologyReproductiveSeedPappus,
					morphologyReproductiveSeedTesta,
					lifeStageSeedling))
			.put("vegitativePropagation", ImmutableSet.<DescriptionType>of(
					vegetativeMultiplication,
					vegetativeMultiplicationBulb,
					vegetativeMultiplicationBulbil,
					vegetativeMultiplicationCorm,
					vegetativeMultiplicationGemma,
					vegetativeMultiplicationPseudobulb,
					vegetativeMultiplicationRhizome,
					vegetativeMultiplicationStolon,
					vegetativeMultiplicationTuber))
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

	public List<DescriptionType> getTypeHeirarchy() {
		var heirarchy = new ArrayList<DescriptionType>();
		var parts = term.split(":");
		var currentTerm = "";
		for (var part : parts) {
			currentTerm += part;
			try {
				var type = lookup(currentTerm);
				heirarchy.add(type);
			} catch (IllegalArgumentException e) {}
			currentTerm += ":";
		}
		return heirarchy;
	}

	public String getSearchCategory() {
		for(String category : searchCategories.keySet()) {
			if(searchCategories.get(category).contains(this)) {
				return category;
			}
		}

		return null;
	}

	public String getTerm() {
		return term;
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