define(function(require) {

  var $ = require('jquery');
  var _ = require('libs/lodash');
  var Handlebars = require('handlebars');
  require('libs/bootstrap');
  require('libs/jquery.serialize-object');
  require('libs/html.sortable');
  require('libs/select2');
  require('libs/select2.sortable');
  require('libs/bootstrap-datetimepicker');

  var loginTmpl = require('templates/admin/login.js')

  var adminTmpl = require('templates/admin/main.js')

  var organisationsTmpl = require('templates/admin/organisations.js')
  var organisationFormTmpl = require('templates/admin/organisation/form.js')

  var resourceRowsTmpl = require('templates/admin/resource-row.js')
  var resourceCreateTmpl = require('templates/admin/resource-create.js')

  var jobListsTmpl = require('templates/admin/job/lists.js')
  var jobListsCreateTmpl = require('templates/admin/job/create.js')

  Handlebars.registerPartial('admin/organisations', organisationsTmpl);
  Handlebars.registerPartial('admin/job/lists', jobListsTmpl);

  /* Utility functions */

  function formToJson(form) {
    return JSON.stringify(
      _.omitBy($(form).serializeObject(), _.isEmpty)
    );
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

  function listOrganisations(show) {
    $.ajax({
      url: "/harvester/api/1/organisation",

      success: function(organisations) {
        $("#organisations").html(organisationsTmpl(organisations));
        if(!_.isEmpty(show)) {
          $('#' + show + ' > .panel-collapse').addClass('in');
        }
      },

      error: loginIfUnauthorized
    });
  }

  function showAddOrganisation() {
    var section = $(this).parents('section');
    section.children().hide();
    section.after(organisationFormTmpl());
  }

  function hideAddOrganisation() {
    $('#new-organisation').remove();
    $('section').children().show()
  }

  function addOrganisation(e) {
    e.preventDefault();
    post(
      'organisation',
      formToJson('#new-organisation'),
      function(data) {
        hideAddOrganisation();
        listOrganisations(data['identifier']);
      }
    );
  }

  /* Resource interactions */
  function showAddResource() {
    var org = $(this).parents('.panel').attr('id');
    var section = $(this).parents('section');
    section.children().hide();
    section.after(resourceCreateTmpl({ organisation: org }));
  }

  function hideAddResource() {
    $('.new-resource:visible').remove();
    $('section').children().show()
  }

  function addResource(e) {
    e.preventDefault();
    post(
      'resource',
      formToJson('.new-resource:visible'),
      function(resource) {
        hideAddResource();
        listOrganisations(resource['organisation']);
      }
    );
  }

  function harvestResource(e) {
    e.preventDefault();
    var jobId = $(this).data('job');
    post('job/configuration/' + jobId + '/run');
  }

  /* Job Lists */
  function listJobLists() {
    $.ajax({
      url: "/harvester/api/1/job/list",

      success: function(joblists) {
        $("#jobs").html(jobListsTmpl(joblists));
      },

      error: loginIfUnauthorized
    });
  }

  function showAddJobList() {
    var section = $(this).parents('section');

    $.getJSON('/harvester/api/1/job/configuration', function(jobs) {
      section.children().hide();
      section.after(jobListsCreateTmpl(jobs));
      $('.all-jobs-list').select2Sortable();
      $('#next-run').datetimepicker({
        format: 'yyyy-mm-ddThh:ii',
        minuteStep: 15
      });
    });

  }

  function hideAddJobList() {
    $('#new-job-list').remove();
    $('section').children().show()
  }

  function addJobList(e) {
    e.preventDefault();
    post(
      'job/list',
      formToJson('#new-job-list form'),
      showInterface
    );
  }

  function deleteJobList() {
    var id = $(this).data('id');
    var panel = $(this).closest('.panel');

    if(confirm('Are you sure you want to delete this job list?') == true) {
      $.ajax({
        url: '/harvester/api/1/job/list/' + id,
        type: 'DELETE',
        success: function(result) {
          panel.remove();
        }
      });
    }
  }

  /* Login */

  function showLogin() {
    $('.s-page').html(loginTmpl());
    $('.login').submit(function(e) {
      e.preventDefault();
      login();
    })
  }

  function modifyResourceField(id){
    $.ajax({
      url: "/harvester/api/1/resource" + id,

      success: function(resource) {

      },

      error: loginIfUnauthorized
    });
  }

  function initializeEditable(){
    $('.editable').each(function(){
      $(this).editable();
      $(this).on('update', function(e, editable) {
        var id = $(this).closest("tr").attr("id");
      })
    })
  }

  function login(event){
    var request = $('.login').serialize();
    $.post("/harvester/login", request, showInterface);
  }

  /* Overall interface */

  function showInterface() {
    $('.s-page').html(adminTmpl());
    listOrganisations();
    listJobLists();
  }

  var initialize = function() {

    showInterface();

    $('.s-page')
      .on('click', '#add-organisation', showAddOrganisation)
      .on('click', '#save-new-organisation', addOrganisation)
      .on('click', '#cancel-new-organisation', hideAddOrganisation)
      .on('click', '.add-resource', showAddResource)
      .on('click', '.save-new-resource', addResource)
      .on('click', '.cancel-new-resource', hideAddResource)
      .on('click', '.btn.harvest', harvestResource)
      .on('click', '#add-job-list', showAddJobList)
      .on('click', '#save-new-job-list', addJobList)
      .on('click', '#cancel-new-job-list', hideAddJobList)
      .on('click', '.delete-job-list', deleteJobList);
  }

  return {
    initialize: initialize
  };
});
