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
     * @return the current page
     */
    public final PageObject submit() {
        createSourceForm.submit();
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

}
