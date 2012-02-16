package org.emonocot.portal.driver;

import org.emonocot.model.source.Source;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class SourceFormPage extends PageObject {

    /**
    *
    */
    @FindBy(how = How.ID, using = "source")
    private WebElement createSourceForm;

    /**
     *
     */
    private String sourceName;

    /**
     *
     * @param create true if the form creates a new object
     * @return the current page
     */
    public final PageObject submit(final boolean create) {
        createSourceForm.submit();
        if (create) {
            if (sourceName != null) {
                Source source = new Source();
                source.setIdentifier(sourceName);
                testDataManager.registerObject(source);
                this.sourceName = null;
            }
        }
        return getPage(SourceFormPage.class);
    }

    /**
     *
     * @param uri Set the uri
     */
    public final void setSourceUri(final String uri) {
        createSourceForm.findElement(By.name("uri")).clear();
        createSourceForm.findElement(By.name("uri")).sendKeys(uri);
    }

    /**
     *
     * @param logoUrl Set the logoUrl
     */
    public final void setLogoUrl(final String logoUrl) {
        createSourceForm.findElement(By.name("logoUrl")).clear();
        createSourceForm.findElement(By.name("logoUrl")).sendKeys(logoUrl);
    }

    /**
     *
     * @param title Set the title
     */
    public final void setSourceTitle(final String title) {
        createSourceForm.findElement(By.name("title")).clear();
        createSourceForm.findElement(By.name("title")).sendKeys(title);
    }

    /**
     *
     * @param identifier Set the identifier
     */
    public final void setSourceIdentifier(final String identifier) {
        sourceName = identifier;
        createSourceForm.findElement(By.name("identifier")).clear();
        createSourceForm.findElement(By.name("identifier"))
                .sendKeys(identifier);
    }

}
