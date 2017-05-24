  define(function(require) {

    var $ = require('jquery');
    var Handlebars = require('handlebars');
    require('libs/bootstrap');
    var resourceRowsTmpl = require('templates/partials/harvester/resource-row.js')
    var resourceCreateTmpl = require('templates/partials/harvester/resource-create.js')
    Handlebars.registerPartial('resource-row', resourceRowsTmpl);
    Handlebars.registerPartial('resource-create', resourceCreateTmpl);


    function loadResources(state) {
      $.getJSON("/harvester/api/1/resource" + state, function(json) {
        $(".resource-form").html(resourceRowsTmpl(json));
      });
    }

    function loadOrganisations()

    function addResource(event) {
      var organisations = $.getJSON("/harvester/api/1/organisation")
      $(".resource-form").prepend(resourceCreateTmpl(organisations));
    }

    function post(type, jsonObject){
      $.ajax({
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        url: "/harvester/api/1/" + type,
        dataType: "json",
        data: JSON.stringify(jsonObject),
      })
    }

    function login(event){
      var request = {}
      request["username"] = $("[name='username']").val();
      request["password"] = $("[name='password']").val();
      $.ajax({
        type: 'POST',
        url: "/harvester/login",
        data: request
      });
    }

    function updateOrganisation(event){
      var organisation = {};
      $(this).parent().parent().find('select, textarea, input').each( function() {
        var name = $(this).attr("name");
        var value =  $(this).val();
        organisation[name] = value;
      });
      var request = {};
      request["organisation"] = organisation;
      post("organisation", organisation);
    }

    function updateResource(event){
      var resource = {};
      $(this).closest(".resource-row").find('select, textarea, input').each( function() {
        var name = $(this).attr("name");
        var value =  $(this).val();
        resource[name] = value;
      });
      var request = {};
      request["resource"] =  resource;
      post("resource", request);
      alert(JSON.stringify(request));
      loadResources("")
    }


    var initialize = function() {

      loadResources("");

      $(".resource-form").on("click", ".dropdown", function(event) {
        $(this).parent().parent().parent().find(".form-dropdown").toggleClass("hidden");
      });
      $(".resource-form").on("click", ".save", updateResource);
      $(".add-resource").on("click", ".add", addResource);
      $(".add-organisation").on("click", ".add", updateOrganisation);
      $(".password").on("click", login);
    };



  return {
    initialize: initialize
  };
  });
