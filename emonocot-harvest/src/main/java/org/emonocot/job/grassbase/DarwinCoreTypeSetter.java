/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package org.emonocot.job.grassbase;

import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.translation.ItemListTypeSetter;
import au.org.ala.delta.translation.PrintFile;

/**
 * Doesn't do much in the way of typesetting or formatting, inserts some blank
 * lines for paragraph marks.
 */
public class DarwinCoreTypeSetter implements ItemListTypeSetter {
    /**
     *
     */
    private static final int NUM_LINES_ON_PAGE = 5;
    /**
     *
     */
    private PrintFile printer;
    /**
     *
     */
    private int blankLinesBeforeItem;

    /**
     *
     * @param output Set the printer
     */
    public DarwinCoreTypeSetter(final PrintFile output) {
        printer = output;
        blankLinesBeforeItem = 0;
    }

    /**
     *
     */
    public void beforeFirstItem() {
    }

    /**
     * @param item Set the item
     */
    public final void beforeItem(final Item item) {
        printer.writeBlankLines(blankLinesBeforeItem, NUM_LINES_ON_PAGE);
    }

    /**
     * @param item Set the item
     */
    public void afterItem(final Item item) {
    }

    /**
     * @param attribute Set the attribute
     */
    public void beforeAttribute(final Attribute attribute) {
    }

    /**
     * @param attribute Set the attribute
     */
    public void afterAttribute(final Attribute attribute) {
    }

    /**
     *
     */
    public final void afterLastItem() {
        printer.printBufferLine();
    }

    /**
     *
     */
    public void beforeItemHeading() {
    }

    /**
     *
     */
    public void afterItemHeading() {
    }

    /**
     *
     */
    public void beforeItemName() {
    }

    /**
     *
     */
    public void afterItemName() {
    }

    /**
     *
     */
    public void newParagraph() {
    }

    /**
     * @param description Set the description
     * @return the typeset description
     */
    public final String typeSetItemDescription(final String description) {
        return description;
    }

    /**
     *
     */
    public void beforeNewParagraphCharacter() {
    }

    /**
     * @return the range separator
     */
    public final String rangeSeparator() {
        return "-";
    }

    /**
     * @param character Set the character
     * @param item Set the item
     */
    public void beforeCharacterDescription(final Character character,
            final Item item) {
    }

    /**
     * @param character Set the character
     * @param item Set the item
     */
    public void afterCharacterDescription(final Character character,
            final Item item) {
    }

    /**
     *
     */
    public void beforeEmphasizedCharacter() {
    }

    /**
     *
     */
    public void afterEmphasizedCharacter() {
    }

}
