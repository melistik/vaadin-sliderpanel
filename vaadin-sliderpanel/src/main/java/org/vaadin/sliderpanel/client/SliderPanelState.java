package org.vaadin.sliderpanel.client;

import com.vaadin.shared.AbstractComponentState;

/**
 * Transfer states to GWT connector
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public class SliderPanelState extends AbstractComponentState {

	public int tabSize = 40;
	public int animationDuration = 500;
	public boolean expand = false;
	public SliderMode mode = null;
	public SliderTabPosition tabPosition = SliderTabPosition.BEGINNING;
}
