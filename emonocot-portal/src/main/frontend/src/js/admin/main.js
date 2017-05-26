define(function(require) {

  var $ = require('jquery');
  var _ = require('libs/lodash');
  var Handlebars = require('handlebars');
  require('libs/jquery.serialize-object');

  var loginTmpl = require('templates/admin/login.js')

  var organisationsTmpl = require('templates/admin/organisations.js')
  var organisationFormTmpl = require('templates/admin/organisation/form.js')

  var resourceRowsTmpl = require('templates/admin/resource-row.js')
  var resourceCreateTmpl = require('templates/admin/resource-create.js')

  Handlebars.registerPartial('resource-row', resourceRowsTmpl);
  Handlebars.registerPartial('resource-create', resourceCreateTmpl);

  /* Utility functions */

  function formToJson(form) {
    return JSON.stringify($(form).serializeObject());
  }

  function loginIfUnauthorized(xhr, status, error) {
    if(error === "Unauthorized") {
      showLogin();
    }
  }

  function post(method, data, success, error){
    $.ajax({
      url: '/harvester/api/1/' + method,
      type: 'POST',
      contentType: 'application/json; charset=utf-8',
      dataType: "json",
      data: data,
      success: success,
      error: _.defaultTo(error, loginIfUnauthorized)
    })
  }

  /* Organisation interactions */

  function listOrganisations() {
    $.ajax({
      url: "/harvester/api/1/organisation",

      success: function(organisations) {
        $(".s-page").html(organisationsTmpl(organisations));
      },

      error: loginIfUnauthorized
    });
  }

  function showAddOrganisation() {
    $(this)
      .hide()
      .after(organisationFormTmpl());
  }

  function hideAddOrganisation() {
    $('#new-organisation').remove();
    $('#add-organisation').show()
  }

  function addOrganisation(e) {
    e.preventDefault();
    post(
      'organisation',
      formToJson('#new-organisation'),
      listOrganisations
    );
  }

  /* Resource interactions */
  function showAddResource() {
    var org = $(this).parents('.panel').data('org');
    $(this)
      .hide()
      .after(resourceCreateTmpl({ organisation: org }));
  }

  function hideAddResource() {
    $(this).closest('.panel-body').find('.add-resource').show()
    $('.new-resource:visible').remove();
  }

  function addResource(e) {
    e.preventDefault();
    post(
      'resource',
      formToJson('.new-resource:visible'),
      listOrganisations
    );
  }

  function harvestResource(e) {
    e.preventDefault();
    var jobId = $(this).data('job');
    post('job/configuration/' + jobId + '/run');
  }

  /* Login */

  function showLogin() {
    $('.s-page').html(loginTmpl());
    $('.login').submit(function(e) {
      e.preventDefault();
      login();
    })
  }

  function login(event){
    var request = $('.login').serialize();
    $.post("/harvester/login", request, listOrganisations);
  }

  var initialize = function() {

    listOrganisations();

    $('.s-page')
      .on('click', '#add-organisation', showAddOrganisation)
      .on('click', '#save-new-organisation', addOrganisation)
      .on('click', '#cancel-new-organisation', hideAddOrganisation)
      .on('click', '.add-resource', showAddResource)
      .on('click', '.save-new-resource', addResource)
      .on('click', '.cancel-new-resource', hideAddResource)
      .on('click', '.btn.harvest', harvestResource);
  };

  return {
    initialize: initialize
  };
});
