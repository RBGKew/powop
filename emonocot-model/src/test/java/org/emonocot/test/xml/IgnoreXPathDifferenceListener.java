package org.emonocot.test.xml;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 *
 * @author ben
 *
 */
public class IgnoreXPathDifferenceListener implements DifferenceListener {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(IgnoreXPathDifferenceListener.class);

    /**
     *
     */
    private String[] xpaths;

    /**
     *
     * @param newXPaths Set the xpaths to ignore
     */
    public IgnoreXPathDifferenceListener(final String ... newXPaths) {
        this.xpaths = newXPaths;
    }

    /**
     * @param difference A difference between two xml documents found
     * @return 1 if the nodes are listed in the xpaths to ignore, 0 otherwise
     */
    public final int differenceFound(final Difference difference) {
        logger.info(difference.getControlNodeDetail().getXpathLocation());
        for (String xpath : xpaths) {
            if (difference.getControlNodeDetail().getXpathLocation()
                    .equals(xpath)) {
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            }
        }

        return RETURN_ACCEPT_DIFFERENCE;
    }

    /**
     * @param node1 Node 1
     * @param node2 Node 2
     */
    public void skippedComparison(final Node node1, final Node node2) {

    }
}
