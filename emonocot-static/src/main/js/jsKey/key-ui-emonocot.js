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

   if(!Key.isUndefined(node.isExcluded) && node.isExcluded) {
	     return html;
   }
   if(!Key.isUndefined(node.concept)) {
	  var children = "";
      for(var i = 0; i < node.children.length; i++) {
	     var child = node.children[i];
	     children += writeNode(key, child); 
	  }
	  if(children.length == 0) {
		 return "";
      }
      html += "<li class='descriptiveConcept'><div data-toggle='collapse' data-target='#node" + node.id +"'>";
      if(!Key.isUndefined(node.images) && node.images.length > 0) {
         var image = node.images[0];
         html += "<a class='pull-left' href='#'><img id='descriptiveConcept" + node.id + "' class='thumbnail' src='" + key.getImagePath() +  image.href + "' title='" + node.concept + "'/></a>";     
         html += "<a class='pull-left'>" + node.concept + "</a>";   
     } else {
         html += "<a >" + node.concept + "</a>";
     }
     html += "</div>";
     html += "<div id='node" + node.id + "' class='collapse'><ul class='unstyled'>";
     
     html += children;
     html += "</ul></div></li>";
   } else {
     var character = key.getCharacter(node.character);
     if((!character.selectedValues || character.selectedValues.length == 0) && !character.isRedundant) {
       html += "<li class='character'>";
       if(!Key.isUndefined(character.images) && character.images.length > 0) {
         var image = character.images[0];
         html  += "<a class='pull-left' id='" + character.id + "'>" + character.name + "</a></br>";
         html  += "<img id='character" + character.id + "' class='thumbnail' src='" + key.getImagePath() +  image.href + "' title='" + character.name + "'/>";
         
       } else {
         html  += "<a id='" + character.id + "'>" + character.name + "</a>";
       }
       html += "</li>";
     }
   }
   return html;
}

function characterModal(characterId, key) {
    var character = key.getCharacter(characterId);
    $('#characterModal .modal-header h3').html(character.name);
    var body = "";
    switch(character.type) {
      case Key.Categorical:
      body += "<ul class='unstyled'>";
      var imageIndex = 0;
      for(var i = 0; i < character.states.length; i++) {
        var state = character.states[i];
        if(!Key.isUndefined(state.images) && state.images.length > 0) {
            var image = state.images[0];
            body += "<li><label class='checkbox'><input type='checkbox'>" + state.name + "</label>";
            body += "<a href='#'><img id='character" + character.id + "-" + i + "-" + imageIndex + "' class='thumbnail' src='" + key.getImagePath() +  image.href + "'title='" + state.name + "'/></a></li><br/>";
            for(var j =0; j < state.images.length; j++) {
           	 imageIndex++;
            }
        } else {
            body += "<li class='noimage'><label class='checkbox'><input type='checkbox'>" + state.name + "</label></li><br/>";
        }
      }
      body += "</ul>";
      $('#characterModal .modal-body').html(body);
      $('#save').unbind("click");
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
        $('#characterModal').modal('hide');
        return false;
      });
      $("#characterModal").after("<div id='modal-gallery' class='modal modal-gallery hide fade modal-fullscreen'><div class='modal-header'><a class='close' data-dismiss='modal'>&#215;</a></div><div class='modal-body'><div class='modal-image'></div><div class='carousel-caption'><a class='modal-title'></a></div></div></div>");
      var galleryBody = "";
      for (var i=0; i< character.states.length; i++){
        var state = character.states[i];
        if(!Key.isUndefined(state.images) && state.images.length > 0) {
           for(var j = 0; j < state.images.length; j++) {
              //body = "<img src='" + key.getImagePath() +  character.states[i].images[j].href + "'/>";
              galleryBody += "<a href='" + key.getFullsizeImagePath() +  state.images[j].href + "' rel='gallery' title='" + state.name + "'>" + state.name + "</a>";
           }
        }
      }
      
      //var title = event.target.title;
      $('#gallery').html(galleryBody);

      $("#characterModal .thumbnail").click(function(event) {
         var id = event.target.id;
         var temp = new Array();
         temp = id.split('-');
         var characterId = temp[0].substring(9);
         
         var stateIndex = temp[1];
         var imageIndex = temp[2];
         var character = key.getCharacter(characterId);
         
         $('#characterModal').modal('hide');
         
         var options = {target:"#modal-gallery", slideshow:"5000", selector:"#gallery a[rel=gallery]", index: imageIndex};
         var modal = $('#modal-gallery');
         
         options = jQuery.extend(modal.data(), options);
         
         modal.on('hidden', function() {
       	  $('#characterModal').modal('show');
         });

         modal.modal(options);
         return false;
       });
      
       $('#characterModal').modal({});
       break;
       default:
       // Continuous
       body += "<div class='row'><label class='span3' for='quantitative'>Enter a value between " + character.min + " and " + character.max + "</label>"   
       body += "<input name='quantitative' type='text' class='span3' placeholder='Type something'/><span class='help-inline'>" + character.unit + "</span></div>";
       $('#characterModal .modal-body').html(body);
       $('#save').unbind("click");
       $('#save').click(function() {
          var value = $('#characterModal .modal-body input').val();
          key.selectCharacter(character.id,value);
          key.calculate();
          $('#characterModal').modal('hide');
          return false;
       });
       $('#characterModal').modal({});
       break;
     }
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
        matched +="<td><img src=\"../css/images/glyphicons/halfsize/glyphicons_001_leaf.png\" alt=\"Taxon\" style=\"width:20px ; height:20px\"/></td>";
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
        		matched += "<td><img class='thumbnail pull-right' src=\"../css/images/no_image.jpg\"/></td>";
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
          characterModal(event.target.id, key); 
      });
      
      $(".thumbnail").click(function(event) {

          if(event.target.id.indexOf("character") == 0){
  
            var character = key.getCharacter(event.target.id.substring(9));

            var body = "";
            for(var i=0; i< character.images.length; i++){
              body += "<a data-icon='icon-white icon-picture' href='" + key.getFullsizeImagePath() +  character.images[i].href + "' rel='gallery' title='" + character.images[i].caption +"'>" + character.images[i].caption +"</a>";
            }
            
            $('#gallery').html(body);
          
          } else {
            var title = event.target.title;
           
            var descriptiveConcept = key.getDescriptiveConcept(event.target.id.substring(18));

            var body = "";
            for(var i=0; i< descriptiveConcept.images.length; i++){
              body += "<a data-icon='icon-white icon-picture' href='" + key.getFullsizeImagePath() +  descriptiveConcept.images[i].href + "' rel='gallery' title='" + descriptiveConcept.images[i].caption + "'>" + descriptiveConcept.images[i].caption +"</a>";
            }
            $('#gallery').html(body);
            
          }
          $('#modal-gallery').unbind('hidden');
         
          var options = {target:"#modal-gallery", slideshow:"5000", selector:"#gallery a[rel=gallery]", index: 0};
          var modal = $('#modal-gallery');
          options = jQuery.extend(modal.data(), options);
          modal.find('.modal-slideshow').find('i').removeClass('icon-play').addClass('icon-pause');
          modal.modal(options);
          return false;
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