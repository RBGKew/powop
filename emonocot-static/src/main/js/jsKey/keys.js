function Key(data) {
  //console.log("Initializing key");
  this.data = data;
  this.selectedCharacters = [];
  this.allowUnscored = false;
  this.unmatchedTaxa = [];
  this.matchedTaxa = this.data.taxa;
  this.characterTree = this.data.characterTree;
  this.descriptiveConcepts = this.data.descriptiveConcepts;
  this.imagePath = data.imagePath;
  this.thumbnailImagePath = data.thumbnailImagePath;
  this.fullsizeImagePath = data.fullsizeImagePath;
  this.taxonPath = data.taxonPath;
  this.updateUI = function(key) { };
  this.applyScoreMethod = this.xperScoringMethod;
  this.autoPrune = false;
  for(var i = 0; i < this.characterTree.length; i++) {
	  var charNode = this.characterTree[i];
	  if(charNode.type == "Character") {
		  var character = this.getCharacter(charNode.character);
		  character.charNode = i;
		  if(!Key.isUndefined(charNode.inapplicableIf) || !Key.isUndefined(charNode.onlyApplicableIf)) {
			  
			  character.inapplicableIf = charNode.inapplicableIf;
			  character.onlyApplicableIf = charNode.onlyApplicableIf;
		  }
	  }
  }
}

Key.prototype.reset = function() {
    this.selectedCharacters = [];
    this.matchedTaxa = this.data.taxa;
    this.unmatchedTaxa = [];

    for(var i = 0; i < this.data.characters.length; i++) {
    	var character = this.data.characters[i];
    	delete character.selectedValues;
    	delete character.isRedundant;
    }
};

Key.prototype.getCharacterTree = function() {
    return this.characterTree;
};

Key.prototype.setImagePath = function(imagePath) {
    this.imagePath = imagePath;
};

Key.prototype.setThumbnailImagePath = function(thumbnailImagePath) {
    this.thumbnailImagePath = thumbnailImagePath;
};

Key.prototype.setFullsizeImagePath = function(fullsizeImagePath) {
    this.fullsizeImagePath = fullsizeImagePath;
};

Key.prototype.setTaxonPath = function(taxonPath) {
    this.taxonPath = taxonPath;
};

Key.prototype.getImagePath = function() {
    return this.imagePath;
};

Key.prototype.getFullsizeImagePath = function() {
    return this.fullsizeImagePath;
};

Key.prototype.getThumbnailImagePath = function() {
    return this.thumbnailImagePath;
};

Key.prototype.getTaxonPath = function() {
    return this.taxonPath;
};

Key.prototype.getAllowUnscored = function() {
    return this.allowUnscored;
};

Key.prototype.setAllowUnscored = function(allow) {
    this.allowUnscored = allow;
};

Key.prototype.setAutoPrune = function(prune) {
    this.autoPrune = prune;
};

Key.prototype.setSelectedCharacters = function(selectedCharacters) {    
    this.selectedCharacters = selectedCharacters;
};

Key.prototype.getSelectedCharacters = function() {    
    this.selectedCharacters.sort(function comparator(obj1, obj2){
      return obj1.id - obj2.id; });
    return this.selectedCharacters;
};
  
Key.prototype.getMatchedTaxa = function() {
    return this.matchedTaxa;
};
  
Key.prototype.getUnmatchedTaxa = function() {
    return this.unmatchedTaxa;
};
  
Key.prototype.calculate = function() {
    //console.log("Key.calculate");
    this.matchedTaxa = [];
    this.unmatchedTaxa = [];
    this.resetScores();
    this.calculateDependencies();
    
    if ( this.selectedCharacters && this.selectedCharacters.length > 0 ) {
		
        this.calculateList();
        if(this.autoPrune) {
          this.calculateRedundant();
        }
    } else {
        //console.log("No Matched Taxa");
	this.matchedTaxa = this.data.taxa;
	this.unmatchedTaxa = [];
    }    
		
    this.updateUI(this);		
};

Key.prototype.pruneRedundants = function() {
    //console.log("Key.pruneRedundants");
    this.matchedTaxa = [];
    this.unmatchedTaxa = [];
    this.resetScores();
    this.calculateDependencies();
    

    if ( this.selectedCharacters && this.selectedCharacters.length > 0 ) {
        this.calculateList();
        this.calculateRedundant();
    } else {
        //console.log("No Matched Taxa");
	    this.matchedTaxa = this.data.taxa;
	    this.unmatchedTaxa = [];
    }    
		
    this.updateUI(this);	
};

Key.prototype.calculateRedundant = function() {
    //console.log("Key.doPruneRedundant");
    if(this.matchedTaxa.length < 2) {
      return;
    }

    var unselectedCharacters = this.getUnselectedCharacters();
    for(var j=0; j < unselectedCharacters.length; j++) {       
        var character = unselectedCharacters[j];        
        //console.log("Checking " + character.name);
        var attributeValue1 = this.findTaxonAttribute(this.matchedTaxa[0], character);
        character.selectedValues = attributeValue1;
        var isRedundant = true;
        for(var i=1; i < this.matchedTaxa.length; i++) {
           var taxon = this.matchedTaxa[i];
           if (this.calculateOne(character, taxon)) {
		     isRedundant = false;
		     break;
	       }
        }
        character.selectedValues = [];
        if(isRedundant) {
          character.isRedundant = true;
        } else {
          character.isRedundant = false;
        }
    }
};

Key.prototype.getUnselectedCharacters = function() {
    var selectedCharacterHash = [];
    for (var i=0; i<this.selectedCharacters.length; i++) {
	    var character = this.selectedCharacters[i];
	    selectedCharacterHash[character.id] = character;
    }

    var unselectedCharacters = [];
	
    for (var i=0; i<this.data.characters.length; i++) {
	var character = this.data.characters[i];
	if ( selectedCharacterHash[character.id] ) {
	    continue;
	}
	unselectedCharacters.push(character);
    }
    return unselectedCharacters;
};
	
Key.prototype.setUpdateUI = function(updateFunction) {
	this.updateUI = updateFunction;
};

Key.prototype.getDescriptiveConcept = function(descriptiveConceptId) {
    var descriptiveConcept = this.data.descriptiveConcepts[descriptiveConceptId];
    return descriptiveConcept;
};

Key.prototype.isExcluded = function(character) {
	if(Key.isUndefined(character.charNode)) {
		return false;
	} else {
	    return this.characterTree[character.charNode].isExcluded;
	}
};

Key.prototype.getCharacter = function(characterId) {
    var character = this.data.characters[characterId];
    return character;
};

Key.prototype.getTaxon = function(taxonId) {
    var taxon = this.data.taxa[taxonId];
    return taxon;
};
	
Key.prototype.selectCharacter = function(characterId, value) {
	var character = this.data.characters[characterId];
    switch (character.type) {
    case Key.Categorical:
        if(value.length == 0) {
            return;
        }
        character.selectedValues = value;
            
        break;
        // Quantitative
        default:
        if(value == "") {
              return;
        }
        character.selectedValues = value;
        break;
        }
    
        var selected = false;
        for(var i = 0; i < this.selectedCharacters.length; i++) {
        	if(this.selectedCharacters[i].id == character.id) {
        		this.selectedCharacters[i] = character;
        		selected = true;
        		break;
        	}
        }
        if(!selected) {
            this.selectedCharacters.push(character);
        }
        
     	
};

Key.prototype.unselectCharacter = function(characterId) {
    var newSelectedCharacters = [];
    for(var i = 0; i < this.selectedCharacters.length; i++) {
      var character = this.selectedCharacters[i];
      if(character.id != characterId) {
         newSelectedCharacters.push(character);
      } else {
  	     character.selectedValues = null;
      }
    }
    this.selectedCharacters = newSelectedCharacters;
};

Key.prototype.isSelected = function(characterId, state) {
	for(var i = 0; i < this.selectedCharacters.length; i++) {
		var character = this.selectedCharacters[i];
		if(character.id == characterId) {
		  switch (character.type) {
          case Key.Categorical:
	        for (var i=0; i<character.selectedValues.length; i++) {
		      var selectedValue = character.selectedValues[i];
		      if ( selectedValue == state) {
			       return true;
              }
	        }
	        return false;
            // Quantitative
          default:
            return true;
          }
    
		}
	}
	return false;
};

Key.prototype.calculateDependency = function(charNodeId) {
	var charNode = this.characterTree[charNodeId];
	
	if(!Key.isUndefined(charNode.onlyApplicableIf)) {
	  var applicable = false;
	  for(var i = 0; i < charNode.onlyApplicableIf.length; i++) {
		var charState = charNode.onlyApplicableIf[i];
		
		if(this.isSelected(charState.character, charState.state)) {
			applicable = true;
		}
	  }
      if(!applicable) {
		return true;
	  }
    }
    if(!Key.isUndefined(charNode.inapplicableIf)) {
	  for(var i = 0; i < charNode.inapplicableIf.length; i++) {
		var charState = charNode.inapplicableIf[i];
		
		if(this.isSelected(charState.character, charState.state)) {
			return true;
		}
	  }
    }
	return false;
};

Key.prototype.resetScores = function() {
	var numberOfCharacters = this.data.characters.length;
	for(var i = 0; i < numberOfCharacters; i++) {
		delete this.data.characters[i].isRedundant;
	}
	var numberOfCharacterNodes = this.characterTree.length;
	for(var i = 0; i < numberOfCharacterNodes; i++) {
		delete this.characterTree[i].isExcluded;
	}
	
};

Key.prototype.calculateDependencies = function() {
	var numberOfCharacterNodes = this.characterTree.length;
	for(var i = 0; i < numberOfCharacterNodes; i++) {
		this.characterTree[i].isExcluded = this.calculateDependency(i);
	}
};

Key.prototype.calculateList = function() {
    //console.log("Key.calculateList");
    var numberOfTaxa = this.data.taxa.length;
    var characterLength = this.selectedCharacters.length;		
	for (var i=0; i<numberOfTaxa; i++) {
		var taxon = this.data.taxa[i];
                //console.log("Checking " + taxon.name);
		var isMatch = true;
		for (var j=0; j<characterLength; j++) {
			var character = this.selectedCharacters[j];
			if ( !this.calculateOne(character, taxon)) {
				isMatch = false;
				break;
			}
		}
		if ( isMatch ) {
			this.matchedTaxa.push(taxon);
		} else {
			this.unmatchedTaxa.push(taxon);
		}
    }
};

Key.prototype.calculateOne = function(character, taxon) {
	return this.hasScore(character, taxon, character.selectedValues);
};

Key.prototype.hasScore = function(character, taxon, scores) {
    //console.log("Key.calculateOne");
    var attributeValue = this.findTaxonAttribute(taxon, character);
    
    if ( !attributeValue ) {
    	if(this.allowUnscored) {
	        return true;
    	} else {
    		return false;
    	}
	}
	switch (character.type) {
	    case Key.Categorical:
           // console.log("Checking " + character.name + " " + character.selectedValues + " against " + taxon.name + " " + attributeValue);	    
	    var values;			
	    if ( $.isArray(attributeValue) ) {
		    values = attributeValue;
	    } else {
		    values = [];
		    values[0] = attributeValue;
	    }
	    if ( attributeValue.length < 0) {
		    return false;
	    }
	    	
	    for (var i=0; i<scores.length; i++) {
		   var selectedValue = scores[i];
		   for (var j=0; j<values.length; j++) {
		       var attributeValue = values[j];
		       if ( selectedValue == attributeValue) {
			       return true;
		       }
		   }
	    }
	    break;
	    default:
        // Quantitative
	    var selectedValue = scores;
	    if ( $.isArray(attributeValue) ) {
	        for (var i=0; i<attributeValue.length; i++) {
			    if ( selectedValue == attributeValue[i]) {
			        return true;
			    }
		    }
		} else if ( !Key.isUndefined(attributeValue.min) 
                 && !Key.isUndefined(attributeValue.max)
                 && !Key.isUndefined(selectedValue.min) 
                 && !Key.isUndefined(selectedValue.max) ) {
		    if ( selectedValue.min == attributeValue.min 
                      && selectedValue.max == attributeValue.max) {
			    return true;
		    }
		} else if ( !Key.isUndefined(attributeValue.min) 
                 && !Key.isUndefined(attributeValue.max) ) {
		    if ( selectedValue >= attributeValue.min 
              && selectedValue <= attributeValue.max) {
			    return true;
		    }
		} else {
		    if ( Key.isString(attributeValue) ) {
			    if ( selectedValue.indexOf(attributeValue+'') > -1 ) {
				    return true;
			    } else if(attributeValue == "-"){  // Unscoped
			    	return true;
			    } else if(attributeValue == "?") { // Uncertain
			    	return true;
			    }
		    } else {
			    if ( selectedValue == attributeValue ) {
			        return true;
			    }
		    }
		 }
		 break;
	}
	return false;
};

Key.isNull = function(value) {
	if (!value || value.length < 1) {
		return true;
	}
	return false;
};



Key.isUndefined = function(val) {
	return val === undefined;
};

Key.isString = function(val) {
	if (typeof val == "string") {
		return true;
	} else {
		return false;
	}
};

Key.parseFloat = function(val) {
	try {
		return parseFloat(val);
	} catch (e) {
	}
	return 0;
};

Key.prototype.findTaxonAttribute = function(taxon, character) {
    if ( Key.isNull(character.data) ) {
	    return;
    }
		
	var attributes = character.data;
		
	if (!attributes || attributes.length < 1 ) {
	    return null;
	}
		
	var index = taxon.id;
	var value = null;
	if ( index < attributes.length ) {
	    value = attributes[index];
	}
	if ( !value ) {
		return value;
	}
		
	if ( $.isArray(value) ) {
		return value;
	}
	if ( Key.isString(value) ) {
		if ( value.indexOf("-") > 0 ) {
			var words = value.split("-");
			if ( words && words.length > 1 ) {
				var object = new Object();
				object.min = Key.parseFloat(words[0]);
				object.max = Key.parseFloat(words[1]);
				return object;
			}
		}
	}
	return value;	
};

Key.prototype.isApplicable = function(taxon, character) {
	if(!Key.isUndefined(character.onlyApplicableIf)) {
		//console.log("checking onlyApplicableIf");
		var applicable = false;
		for(var i = 0; i < character.onlyApplicableIf; i++) {
			var charState = character.onlyApplicableIf[i];
			var scores = [];
			scores.push(charState.state);
			if(this.hasScore(character,taxon,scores)) {
				applicable = true;
			}			
		}
		if(!applicable) {
			//console.log("returning false");
			return false;
		}
	}
	
	if(!Key.isUndefined(character.inapplicableIf)) {
		//console.log("checking inapplicableIf")
		for(var i = 0; i < character.inapplicableIf.length; i++) {
			var charState = character.inapplicableIf[i];
			var scores = [];
			scores.push(charState.state);
			if(this.hasScore(character,taxon,scores)) {
				//console.log("returing false");
				return false;
			}
	    }
	}
	return true;
};

Key.prototype.getAllNumericalValues = function(character, remainingTaxa) {

		var allValues = [];

		for (var i = 0; i < remainingTaxa.length; i++) {
			var attr = this.findTaxonAttribute(remainingTaxa[i], character);
			if (attr != null) {

				if (attr.min != null)
					allValues.push(attr.min);
				if (attr.max != null)
					allValues.push(attr.max);
			}
		}
		return allValues;
};

Key.prototype.splitQuantitativeCharacter = function(character, remainingTaxa) {

		var quantitativeMeasures = [];
		var quantitativeMeasure1 = new Object();
		var quantitativeMeasure2 = new Object();

		// get the Min and Max values of all remaining taxa
		var allValues = this.getAllNumericalValues(character, remainingTaxa);
		allValues.sort(function(val1, val2) {
				var result;
				if (val1 > val2) {
					result = 1;
				} else if (val1 < val2) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
		});
		// determine the best threshold to cut the interval in 2 part
		var threshold = null;
		var bestThreshold = null;
		var difference = allValues.length;
		var differenceMin = difference;
		var taxaBefore = 0;
		var taxaAfter = 0;
		for (var i = 0; i < allValues.length / 2; i++) {
			threshold = allValues[i * 2 + 1];
			taxaBefore = 0;
			taxaAfter = 0;
			for (var j = 0; j < allValues.length / 2; j++) {
				if (allValues[j * 2 + 1] <= threshold)
					taxaBefore++;
				if (allValues[j * 2] >= threshold)
					taxaAfter++;
			}
			difference = Math.abs(taxaBefore - taxaAfter);
			if (difference < differenceMin) {
				differenceMin = difference;
				bestThreshold = threshold;
			}
		}

		// split the interval in 2 part
		if (allValues.length > 2 && bestThreshold != null) {
			quantitativeMeasure1.min = allValues[0];
			quantitativeMeasure1.max = bestThreshold;
			quantitativeMeasure1.maxInclude = false;
			quantitativeMeasure2.min = bestThreshold;
			quantitativeMeasure2.max = allValues[allValues.length - 1];
			quantitativeMeasure2.maxInclude = true;
		}

		// add the 2 new interval to the list
		quantitativeMeasures.push(quantitativeMeasure1);
		quantitativeMeasures.push(quantitativeMeasure2);

		return quantitativeMeasures;
};

Key.prototype.isInclude = function(quantitativeMeasure1, quantitativeMeasure2) {
	if(quantitativeMeasure1.maxInclude) {
		return ((quantitativeMeasure2.min > quantitativeMeasure1.min) && (quantitativeMeasure2.max <= quantitativeMeasure1.max));
	} else {
		return ((quantitativeMeasure2.min > quantitativeMeasure1.min) && (quantitativeMeasure2.max < quantitativeMeasure1.max));
	}
};

Key.prototype.quantitativeCharacterScore = function(character, remainingTaxa) {
	//console.log("Quantitative Character Score " + character.name + " " + remainingTaxa.length);
		var cpt = 0;
		var score = 0;
		var isAlwaysDescribed = true;

		var quantitativeIntervals = this.splitQuantitativeCharacter(character, remainingTaxa);

		for (var i = 0; i < remainingTaxa.length - 1; i++) {
			for (var j = i + 1; j < remainingTaxa.length; j++) {
				if (this.findTaxonAttribute(remainingTaxa[i], character) != null 
				    && this.findTaxonAttribute(remainingTaxa[j], character) != null) {
					// if the character is applicable for both of these taxa
					if (this.isApplicable(remainingTaxa[i], character) && this.isApplicable(remainingTaxa[j], character)) {
						// nb of common states which are absent
						var commonAbsent = 0;
						// nb of common states which are present
						var commonPresent = 0;
						var other = 0;
						var quantitativeMeasure1 = this.findTaxonAttribute(remainingTaxa[i], character);
						var quantitativeMeasure2 = this.findTaxonAttribute(remainingTaxa[j], character);

						// if at least one description is empty for the current character
						if ((quantitativeMeasure1 != null && quantitativeMeasure1 == 0)
							|| (quantitativeMeasure2 != null && quantitativeMeasure2 == 0)) {
							isAlwaysDescribed = false;
						}

						// if one description is unknown and the other have no measure
						if ((quantitativeMeasure1 == null && quantitativeMeasure2 != null && quantitativeMeasure2 == 0)
							|| (quantitativeMeasure2 == null && quantitativeMeasure1 != null && quantitativeMeasure1 == 0)) {
							score++;
							// search common shared values
						} else if (quantitativeMeasure1 != null && quantitativeMeasure2 != null) {

							// if a taxon is described and the other is not, it means that this taxa can be
							// discriminated
							if ((quantitativeMeasure1 == 0 && quantitativeMeasure2 != 0)
								|| (quantitativeMeasure2 == 0 && quantitativeMeasure1 != 0)) {
								score++;
							} else {

								// search common state
								for (var k = 0; k < quantitativeIntervals.length; k++) {
									var quantitativeMeasure = quantitativeIntervals[k];
									
									if (this.isInclude(quantitativeMeasure,quantitativeMeasure1)) {
										if (this.isInclude(quantitativeMeasure,quantitativeMeasure2)) {
											commonPresent++;
										} else {
											other++;
										}
									} else {
										if (this.isInclude(quantitativeMeasure,quantitativeMeasure2)) {
											other++;
										} else {
											commonAbsent++;
										}
									}
								}
								score += this.applyScoreMethod(commonPresent, commonAbsent, other);
							}
						}
						cpt++;
					}
				}
			}
		}

		if (cpt >= 1) {
			score = score / cpt;
		}

		// increasing artificially the score of character containing only described taxa
		//!alreadyUsedCharacter.contains(character)
		if (isAlwaysDescribed && score > 0) {
			score = score + 2.0;
		}

		// fewStatesCharacterFirst option handling
		//if (utils.isFewStatesCharacterFirst() && score > 0) {
			// increasing artificially the score of character with few states
		//	float coeff = (float) 1 - ((float) 2 / (float) maxNbStatesPerCharacter);
		//	score = (float) (score + coeff);
		//}
		return score;
};

Key.prototype.categoricalCharacterScore = function(character, remainingTaxa) {
	    //console.log("Categorical Character Score " + character.name + " " + remainingTaxa.length);
		var cpt = 0;
		var score = 0;
		var isAlwaysDescribed = true;

		for(var i = 0; i < remainingTaxa.length - 1; i++) {
			for(var j = i + 1; j < remainingTaxa.length; j++) {
				// if the character is applicable for both of these taxa
				if (this.isApplicable(remainingTaxa[i], character) && this.isApplicable(remainingTaxa[j], character)) {
						var att1 = this.findTaxonAttribute(remainingTaxa[i], character);
						var att2 = this.findTaxonAttribute(remainingTaxa[j], character);
						var attribute1;
						var attribute2;
						if ( $.isArray(att1) ) {
		                  attribute1 = att1;
	                    } else {
		                  attribute1 = [];
		                  attribute1[0] = att1;
	                    }
						
						if ( $.isArray(att2) ) {
		                  attribute2 = att2;
	                    } else {
		                  attribute2 = [];
		                  attribute2[0] = att2;
	                    }
	                    
						// if at least one description is empty for the current character
						if ((attribute1 != null && attribute1.length == 0)
								|| (attribute2 != null && attribute2.length == 0)) {
							isAlwaysDescribed = false;
						}

						// if one description is unknown and the other have 0 state checked
						if ((attribute1 == null && attribute2 != null && attribute2.length == 0)
								|| (attribute2 == null && attribute1 != null && attribute1.length == 0)) {
							score++;
						} else if (attribute1 != null && attribute2 != null) {

							// nb of common states which are absent
							var commonAbsent = 0;
							// nb of common states which are present
							var commonPresent = 0;
							var other = 0;

							// search common state
							for (var k = 0; k < character.states.length; k++) {
								var attribute1ContainsState = false;
								for(var l = 0; l < attribute1.length; l++) {
									// States are 1-based, with 0 meaning unscored
									if(attribute1[l] == (k+1)) {
										attribute1ContainsState = true;
										break;
									}
								}
								
								var attribute2ContainsState = false;
								for(var l = 0; l < attribute2.length; l++) {
									// States are 1-based, with 0 meaning unscored
									if(attribute2[l] == (k+1)) {
										attribute2ContainsState = true;
										break;
									}
								}

								if (attribute1ContainsState) {
									if (attribute2ContainsState) {
										commonPresent++;
									} else {
										other++;
									}
									// !attribute2ContainsState
								} else {
									if (attribute2ContainsState) {
										other++;
									} else {
										commonAbsent++;
									}
								}
							}
//							alert("Common Present " + commonPresent + " commonAbsent " + commonAbsent + " other " + other);
							score += this.applyScoreMethod(commonPresent, commonAbsent, other);
						}
						cpt++;
					}
				}
		}

		if (cpt >= 1) {
			score = score / cpt;
		}

		// increasing artificially the score of character containing only described taxa
		if (isAlwaysDescribed && score > 0) {
			score =  (score + 2.0);
		}

		// fewStatesCharacterFirst option handling
		//if (utils.isFewStatesCharacterFirst() && score > 0 && character.getStates().size() >= 2) {
			// increasing artificially score of character with few states
			//float coeff = (float) 1
			//		- ((float) character.getStates().size() / (float) maxNbStatesPerCharacter);
			//score = (float) (score + coeff);
		//}

		return score;
}

Key.prototype.calculateScores = function(unselectedCharacters, remainingTaxa) {
	var scores = [];
	for(var i = 0; i < unselectedCharacters.length; i++) {
		
		var character = unselectedCharacters[i];
		switch (character.type) {
        case Key.Categorical:
        scores.push(this.categoricalCharacterScore(character, remainingTaxa));
        break;
        // Quantitative
        default:
        scores.push(this.quantitativeCharacterScore(character, remainingTaxa));
        break;
        }	
		//console.log("Character " + character.name + " " + scores[i]);
	}
	return scores;
};

Key.prototype.nextBest = function() {
	var unselectedCharacters = this.getUnselectedCharacters();
	var scores = this.calculateScores(unselectedCharacters, this.getMatchedTaxa());
	var bestScore = -1;
	var bestIndex = -1;	
	for(var i = 0; i < unselectedCharacters.length; i++) {
		if(scores[i] > bestScore) {
			bestScore = scores[i];
			bestIndex = i;
		}
	}
	var bestCharacter = unselectedCharacters[bestIndex];
	return bestCharacter.id;
};

// yes or no method (Xper)
Key.prototype.xperScoringMethod = function(commonPresent, commonAbsent, other) {
	var out = 0;
	if ((commonPresent == 0) && (other > 0)) {
		out = 1;
	} else {
		out = 0;
	}
	return out;
};


Key.prototype.sokalAndMichenerScoringMethod = function(commonPresent, commonAbsent, other) {
	var out = 0;
	out = 1 - ((commonPresent + commonAbsent) / (commonPresent + commonAbsent + other));
	// round to 10^-3
	out = Math.round(out).toFixed(3);
	return out;
};

// Jaccard Method
Key.prototype.jaccardScoringMethod = function(commonPresent, commonAbsent, other) {
	var out = 0;
	try {
	    // case where description are empty
		out = 1 - (commonPresent / (commonPresent + other));
		// round to 10^-3
		out = Math.round(out).toFixed(3);
	} catch (exception) {
		out = 0;
	}
	return out;
};

Key.Categorical = "UM";
Key.Quantitative = "RN";