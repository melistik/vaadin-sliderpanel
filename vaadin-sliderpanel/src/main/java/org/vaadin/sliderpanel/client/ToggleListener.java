package org.vaadin.sliderpanel.client;

/**
 * listener for panel interactions
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 2.0
 */
public interface ToggleListener {

    /**
     * get fired on each click on the tab caption
     *
     * @param expand true means content is visible now
     */
    void onToggle(boolean expand);

}
