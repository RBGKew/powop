Key.SimpleView = "Simple";
Key.ListView = "List";

Key.prototype.view = Key.SimpleView;

Key.prototype.getView = function() {
	return this.view;
};

Key.prototype.setView = function(view) {
	this.view = view;
};

function writeNode(key, node) {
   var html = "";
   if(!Key.isUndefined(node.concept)) {
     html += "<li class='descriptiveConcept'><div data-toggle='collapse' data-target='#node" + node.id +"'>";
     if(!Key.isUndefined(node.images) && node.images.length > 0) {
         var image = node.images[0];
         html += "<a class='pull-left' href='#'><img class='thumbnail' src='" + key.getImagePath() +  image.href + "'/></a>";     
         html += "<a class='pull-left'>" + node.concept + "</a>";   
     } else {
         html += "<a >" + node.concept + "</a>";
     }
     html += "</div>";
     html += "<div id='node" + node.id + "' class='collapse'><ul class='unstyled'>";
        
     for(var i = 0; i < node.children.length; i++) {
       var child = node.children[i];
       html += writeNode(key, child); 
     }
     html += "</ul></div></li>";
   } else {
     
     var character = key.getCharacter(node.character);
     if(!character.selectedValues || character.selectedValues.length == 0) {
       html += "<li class='character'>";
       if(!Key.isUndefined(character.images) && character.images.length > 0) {
         var image = character.images[0];
         html += "<a class='pull-left' href='#'><img class='thumbnail' src='" + key.getImagePath() +  image.href + "'/></a>";
         html  += "<a class='pull-left' id='" + character.id + "'>" + character.name + "</a>";
       } else {
         html  += "<a id='" + character.id + "'>" + character.name + "</a>";
       }
       html += "</li>";
     }
   }
   return html;
}

function updateUI(key) {
      var selectedCharacters = key.getSelectedCharacters();
      var unselectedCharacters = key.getUnselectedCharacters();
      var matchedTaxa = key.getMatchedTaxa();
      var unmatchedTaxa = key.getUnmatchedTaxa();
      var characterTree = key.getCharacterTree();

      var matched = "";
      
      for(var i = 0; i < matchedTaxa.length; i++) {
        var taxon = matchedTaxa[i];
        matched += "<tr>";
        matched +="<td><img src=\"http://build.e-monocot.org/uat/portal/images/taxonPageIcon.png\" alt=\"Taxon\" style=\"width:20px ; height:20px\"/></td>";
        if(!Key.isUndefined(taxon.links) && taxon.links.length > 0) {
            var link = taxon.links[0];
            matched += "<td><a href='" + key.getTaxonPath() + link.href + "' title='" + link.title + "'><h4>" + taxon.name + "</h4></a></td>";
        } else {
            matched += "<td><h4>" + taxon.name + "</h4></td>";
        }
        if(key.getView() == Key.ListView ) {
        	if (!Key.isUndefined(taxon.images) && taxon.images.length > 0){
            var image = taxon.images[0];
            matched += "<td><a class='pull-right' href='#'><img class='thumbnail' src='" + key.getImagePath() +  image.href + "'/></a></td>";
        	} else{
        		matched += "<td><img class='thumbnail pull-right' src='http://build.e-monocot.org/uat/portal/images/no_image_3.jpg'/></td>";
        	}
        } else {
            matched += "<td></td>";
        }        
        matched += "</tr>";
      }
      $("#matchedTaxa table tbody").html(matched);
      $("#pages").html(matchedTaxa.length + " taxa remaining");
      
      var unSelected = "";
      var nonRedundant = 0;
      for(var i = 0; i < unselectedCharacters.length; i++) {
        var character = unselectedCharacters[i];
        if(!character.isRedundant) {
            nonRedundant++;
        }
      }
      for(var i = 0; i < characterTree.length; i++) {
         unSelected += writeNode(key, characterTree[i]);
      }
      
	  $("#unselectedCharacters").html("<li class='nav-header'>Features Available: " + nonRedundant + "</li>" + unSelected);	  
      
      $("#unselectedCharacters li.character a").click(function(event) {
         var character = key.getCharacter(event.target.id);
         $('#characterModal .modal-header h3').html(character.name);
         var body = "";
         switch(character.type) {
           case Key.Categorical:           
           body += "<ul class='unstyled'>";
           for(var i = 0; i < character.states.length; i++) {
             
             var state = character.states[i];
             if(!Key.isUndefined(state.images) && state.images.length > 0) {
            	 var image = state.images[0];
            	 body += "<li><a class='pull-left' href='#'><img class='thumbnail' src='" + key.getImagePath() +  image.href + "'/></a>";
            	 body += "<label class='checkbox'><input type='checkbox'>" + state.name + "</label></li>";
             } else {
                 body += "<li class='noimage'><label class='checkbox'><input type='checkbox'>" + state.name + "</label></li>";
             }
           }
           body += "</ul>";
           $('#characterModal .modal-body').html(body);
           $('#save').click(function() {
             var s = 1;
             var selectedValues = [];
             $('#characterModal .modal-body input').each(function() {
               if($(this).is(':checked')) {
                 selectedValues.push(s);
               }
               s++;
             });
             key.selectCharacter(character.id,selectedValues);
             key.calculate();
             var func = arguments.callee;
             $('#save').unbind("click",func);
             $('#characterModal').modal('hide');
             return false;
           });
           $('#characterModal').modal({});
           break;
           default:
           // Continuous
           body += "<div class='row'><label class='span3' for='quantitative'>Enter a value between " + character.min + " and " + character.max + "</label>"   
           body += "<input name='quantitative' type='text' class='span3' placeholder='Type something'/><span class='help-inline'>" + character.unit + "</span></div>";
           $('#characterModal .modal-body').html(body);
           $('#save').click(function() {
             var value = $('#characterModal .modal-body input').val();
             key.selectCharacter(character.id,value);
             key.calculate();
             var func = arguments.callee;
             $('#save').unbind("click",func);
             $('#characterModal').modal('hide');
             return false;
           });
           $('#characterModal').modal({});
           break;
         }
       });

      var selected = "";
      for(var i = 0; i < selectedCharacters.length; i++) {
          var character = selectedCharacters[i];
    	  if(!Key.isUndefined(character)) {
    		  selected += "<li class='selectedCharacter'><h4 id='" + character.id + "'>"  + character.name + "</h4>";
                  switch(character.type) {
                    case Key.Categorical:
                      var values;
                      if($.isArray(character.selectedValues)) {
                        values = character.selectedValues;
                      } else {
                        values = [];
                        values.push(character.selectedValues);
                      }
                      for(var j = 0; j < values.length; j++) {
                        if(j > 0) {
                          selected += ", ";
                        }
                        var state = character.states[values[j] - 1];
                        selected += state.name;
                        
                      }
                    break;
                    default:
                    // categorical
                    selected += character.selectedValues + " " + character.unit;
                    break;                  
                  }
                  selected += "</li>";
    	  }
      }      
      $("#selectedCharacters").html("<li class='nav-header'>Features Chosen: " + selectedCharacters.length + "</li>" + selected);
      $("#selectedCharacters li h4").click(function(event) {
          key.unselectCharacter(event.target.id);
          key.calculate();
      });
}
