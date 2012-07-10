function Key(data) {
  //console.log("Initializing key");
  this.data = data;
  this.selectedCharacters = [];
  this.allowUnscored = true;
  this.unmatchedTaxa = [];
  this.matchedTaxa = this.data.taxa;
  this.characterTree = this.data.characterTree;
  this.descriptiveConcepts = this.data.descriptiveConcepts;
  this.imagePath = data.imagePath;
  this.taxonPath = data.taxonPath;
  this.updateUI = function(key) { };
  this.autoPrune = false;
}

Key.prototype.reset = function() {
    this.selectedCharacters = [];
    this.matchedTaxa = this.data.taxa;
    this.unmatchedTaxa = [];
};

Key.prototype.getCharacterTree = function() {
    return this.characterTree;
};

Key.prototype.setImagePath = function(imagePath) {
    this.imagePath = imagePath;
};

Key.prototype.setTaxonPath = function(taxonPath) {
    this.taxonPath = taxonPath;
};

Key.prototype.getImagePath = function() {
    return this.imagePath;
};

Key.prototype.getTaxonPath = function() {
    return this.taxonPath;
};

Key.prototype.setAllowUnscored = function(allow) {
    this.allowUnscored = allow;
};

Key.prototype.setAutoPrune = function(prune) {
    this.autoPrune = prune;
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
	    	
	    for (var i=0; i<character.selectedValues.length; i++) {
		   var selectedValue = character.selectedValues[i];
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
	    var selectedValue = character.selectedValues;
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

Key.Categorical = "UM";
Key.Quantitative = "RN";

define("jskey", function() {
	  return Key;
});