package org.emonocot.portal.driver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class ClassificationPage extends PageObject {
    /**
     *
     */
    @FindBy(how = How.ID, using = "classification")
    private WebElement treeRoot;

    /**
     *
     * @return the number of nodes in the classification tree
     */
    public final int getNodeNumber() {
        List<WebElement> nodes = treeRoot.findElements(By.xpath("//a[ancestor::div[@id = 'classification'] and not(ancestor::ul[ancestor::li[contains(@class,'jstree-closed')]])]"));
        Set<WebElement> uniqueNodes = new HashSet<WebElement>();
        uniqueNodes.addAll(nodes);
        return uniqueNodes.size();
    }

    /**
     *
     * @return a list of nodes from the classification tree
     */
    public final List<String[]> getNodes() {
        List<WebElement> nodes = treeRoot.findElements(By.xpath("//a[ancestor::div[@id = 'classification'] and not(ancestor::ul[ancestor::li[contains(@class,'jstree-closed')]])]"));
        SortedSet<WebElement> uniqueNodes = new TreeSet<WebElement>(new PositionComparator());
        uniqueNodes.addAll(nodes);
        List<String[]> results = new ArrayList<String[]>();
        for (WebElement node : uniqueNodes) {
            String[] result = new String[2];
            String href = node.getAttribute("href");
            result[0] = href.substring(href.lastIndexOf("/") + 1);
            result[1] = node.getText().trim();
            results.add(result);
        }
        return results;
    }

    /**
     *
     * @author ben
     *
     */
    class PositionComparator implements Comparator<WebElement> {

        /**
         * @param o1 The first web element
         * @param o2 The second web element
         * @return -1 if o1 comes before o2, 1 if o1 comes after o2, 0 otherwise
         */
        public final int compare(final WebElement o1, final WebElement o2) {
            if (o1.getLocation().y > o2.getLocation().y) {
                return 1;
            } else if (o1.getLocation().y < o2.getLocation().y) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     *
     * @param nodeName The name of the node to expand
     */
    public final void expandNode(final String nodeName) {
        WebElement node = treeRoot.findElement(By.xpath("//li[a/text() = '"
                + nodeName + "']"));
        node.findElement(By.tagName("ins")).click();
    }
}
