package org.vaadin.sliderpanel.client;

/**
 * listener for panel interactions
 * 
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public interface SliderPanelListener {

	/**
	 * get fired on each click on the tab caption
	 * 
	 * @param expand
	 *            true means content is visible now
	 */
	void onToggle(boolean expand);

}
