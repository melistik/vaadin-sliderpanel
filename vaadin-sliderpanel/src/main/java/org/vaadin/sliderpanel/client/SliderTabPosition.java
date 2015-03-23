package org.vaadin.sliderpanel.client;

/**
 * holds different tab positions
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public enum SliderTabPosition {

	/**
	 * Mode: BOTTOM/TOP = LEFT<br>
	 * Mode: LEFT/RIGHT = TOP
	 */
	BEGINNING,
	/**
	 * Middle of the slider
	 */
	MIDDLE,
	/**
	 * Mode: BOTTOM/TOP = RIGHT<br>
	 * Mode: LEFT/RIGHT = BOTTOM
	 */
	END;
}
