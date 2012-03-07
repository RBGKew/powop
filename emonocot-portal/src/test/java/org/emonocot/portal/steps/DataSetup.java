package org.emonocot.portal.steps;

import java.util.List;

import org.emonocot.portal.rows.AccessControlRow;
import org.emonocot.portal.rows.AnnotationRow;
import org.emonocot.portal.rows.GroupRow;
import org.emonocot.portal.rows.ImageRow;
import org.emonocot.portal.rows.JobExecutionRow;
import org.emonocot.portal.rows.JobInstanceRow;
import org.emonocot.portal.rows.ReferenceRow;
import org.emonocot.portal.rows.SourceRow;
import org.emonocot.portal.rows.TaxonRow;
import org.emonocot.portal.rows.UserRow;
import org.emonocot.test.TestDataManager;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.After;
import cucumber.annotation.en.Given;

/**
 *
 * @author ben
 *
 */
public class DataSetup {

    /**
    *
    */
    @Autowired
    private TestDataManager testDataManager;

    /**
     *
     * @param imageRows
     *            set the image rows
     */
    @Given("^there are images with the following properties:$")
    public final void thereAreImagesWithTheFollowingProperties(
            final List<ImageRow> imageRows) {
        for (ImageRow imageRow : imageRows) {
            testDataManager.createImage(imageRow.identifier, imageRow.caption,
                    imageRow.url, imageRow.source, imageRow.description,
                    imageRow.locality, imageRow.creator, imageRow.license,
                    imageRow.keywords);
        }
    }

    /**
     *
     * @param jobInstanceRows
     *            set the job instance rows
     */
    @Given("^there are job instances with the following properties:$")
    public final void thereAreJobInstancesWithTheFollowingProperties(
            final List<JobInstanceRow> jobInstanceRows) {
        for (JobInstanceRow jobInstanceRow : jobInstanceRows) {
            testDataManager.createJobInstance(jobInstanceRow.jobId,
                    jobInstanceRow.jobName, jobInstanceRow.authorityName,
                    jobInstanceRow.version);
        }
    }

    /**
     *
     * @param jobExecutionRows
     *            set the job execution rows
     */
    @Given("^there are job executions with the following properties:$")
    public final void thereAreJobExecutionsWithTheFollowingProperties(
            final List<JobExecutionRow> jobExecutionRows) {
        for (JobExecutionRow jobExecution : jobExecutionRows) {
            testDataManager.createJobExecution(jobExecution.jobId,
                    jobExecution.jobInstance, jobExecution.createTime,
                    jobExecution.endTime, jobExecution.startTime,
                    jobExecution.lastUpdated, jobExecution.status,
                    jobExecution.version, jobExecution.exitCode,
                    jobExecution.exitMessage);
        }
    }

    /**
     *
     * @param annotationRows
     *            set the annotation rows
     */
    @Given("^there are annotations with the following properties:$")
    public final void thereAreAnnotationsWithTheFollowingProperties(
            final List<AnnotationRow> annotationRows) {
        for (AnnotationRow annotationRow : annotationRows) {
            testDataManager.createAnnotation(annotationRow.identifier,
                    annotationRow.code, annotationRow.type,
                    annotationRow.recordType, annotationRow.value,
                    annotationRow.text, annotationRow.jobId,
                    annotationRow.dateTime, annotationRow.source,
                    annotationRow.object);
        }
    }

    /**
    *
    * @param aceRows
    *            set the ACE rows
    */
   @Given("^there are the following access controls:$")
   public final void thereAreTheFollowingAccessControls(
           final List<AccessControlRow> aceRows) {
       for (AccessControlRow aceRow : aceRows) {
            testDataManager.createAcl(aceRow.principal, aceRow.principalType,
                    aceRow.object, aceRow.objectType, aceRow.permission);
       }
   }

    /**
     * @param rows
     *            Set the rows
     */
    @Given("^there are groups with the following properties:$")
    public final void thereGroupsWithTheFollowingProperties(
            final List<GroupRow> rows) {
        for (GroupRow row : rows) {
            testDataManager.createGroup(row.identifier, row.permission1);
        }
    }

    /**
     *
     * @param rows
     *            Set the rows
     */
    @Given("^there are users with the following properties:$")
    public final void thereAreUsersWithTheFollowingProperties(
            final List<UserRow> rows) {
        for (UserRow row : rows) {
            testDataManager
                    .createUser(row.identifier, row.password, row.group1);
        }
    }

    /**
     *
     * @param rows
     *            Set the rows
     */
    @Given("^there are source systems with the following properties:$")
    public final void thereAreSourceSystemsWithTheFollowingProperties(
            final List<SourceRow> rows) {
        for (SourceRow row : rows) {
            testDataManager.createSourceSystem(row.identifier, row.uri, row.title);
        }
    }

    /**
     *
     * @param name
     *            Set the name
     */
    @Given("^there are no taxa called \"([^\"]*)\"$")
    public final void thereAreNoTaxaCalled(final String name) {
        testDataManager.assertNoTaxaWithName(name);
    }

    /**
     *
     * @param rows
     *            set the taxon rows
     */
    @Given("^there are taxa with the following properties:$")
    public final void thereAreTaxaWithTheFollowingProperties(
            final List<TaxonRow> rows) {
        for (TaxonRow row : rows) {
            testDataManager.createTaxon(row.identifier, row.name, null,
                    row.family, row.genus, row.specificEpithet, row.rank,
                    row.status, row.diagnostic, row.diagnosticReference1,
                    row.habitat, row.general, row.protologue,
                    row.protologueMicroReference, row.protologLink, row.image1, row.image2,
                    row.image3, row.distribution1, row.distribution2,
                    row.distribution3, row.source, row.created, row.parent,
                    row.accepted, row.reference1, row.reference2);
        }

    }

    /**
     *
     * @param rows
     *            set the rows
     */
    @Given("^there are references with the following properties:$")
    public final void thereAreReferencesWithTheFollowingProperties(
            final List<ReferenceRow> rows) {
        for (ReferenceRow row : rows) {
            testDataManager.createReference(row.identifier, row.title,
                    row.authors, row.datePublished, row.volume, row.page,
                    row.citation, row.publisher);
        }
    }

    /**
    *
    */
    @After
    public final void tearDown() {
        testDataManager.tearDown();
    }

}
