package org.vaadin.sliderpanel;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderPanelClientRpc;
import org.vaadin.sliderpanel.client.SliderPanelListener;
import org.vaadin.sliderpanel.client.SliderPanelServerRpc;
import org.vaadin.sliderpanel.client.SliderPanelState;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Component;

/**
 * Vaadin SliderPanel
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public class SliderPanel extends AbstractSingleComponentContainer {

	private final List<SliderPanelListener> listeners = new ArrayList<SliderPanelListener>();

	private final SliderPanelServerRpc rpc = new SliderPanelServerRpc() {
		@Override
		public void clicked(final boolean visible) {
			getState().expand = visible;
			// fires event on all listeners
			for (SliderPanelListener listener : SliderPanel.this.listeners) {
				listener.onToggle(visible);
			}
		}
	};

	/**
	 * construct sliderPanel not expanded and TOP-Mode
	 * 
	 * @param content
	 *            component that fills the content of the slider. during first draw size calculations will get done
	 */
	public SliderPanel(final Component content) {
		this(content, false, SliderMode.TOP);
	}

	/**
	 * construct sliderPanel not expanded
	 * 
	 * @param content
	 *            component that fills the content of the slider. during first draw size calculations will get done
	 * @param mode
	 *            TOP, RIGHT, BOTTOM, LEFT
	 */
	public SliderPanel(final Component content, final SliderMode mode) {
		this(content, false, mode);
	}

	/**
	 * construct sliderPanel in TOP-Mode
	 * 
	 * @param content
	 *            component that fills the content of the slider. during first draw size calculations will get done
	 * @param expanded
	 *            should content be shown or not
	 */
	public SliderPanel(final Component content, final boolean expanded) {
		this(content, expanded, SliderMode.TOP);
	}

	/**
	 * construct sliderPanel
	 * 
	 * @param content
	 *            component that fills the content of the slider. during first draw size calculations will get done
	 * @param expanded
	 *            should content be shown or not
	 * @param mode
	 *            TOP, RIGHT, BOTTOM, LEFT
	 */
	public SliderPanel(final Component content, final boolean expanded, final SliderMode mode) {
		setContent(content);

		if (mode.isVertical()) {
			setHeight(100, Unit.PERCENTAGE);
			setWidth(40, Unit.PIXELS);
		} else {
			setWidth(100, Unit.PERCENTAGE);
			setHeight(40, Unit.PIXELS);
		}

		setImmediate(true);
		registerRpc(this.rpc);

		getState().expand = expanded;
		getState().mode = mode;
	}

	/**
	 * add listener on slider interactions
	 * 
	 * @param listener
	 */
	public void addListener(final SliderPanelListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * remove listener on slider interactions
	 * 
	 * @param listener
	 * @return true when removed successfully
	 */
	public boolean removeListener(final SliderPanelListener listener) {
		return this.listeners.remove(listener);
	}

	/**
	 * Caption of the tab
	 */
	@Override
	public void setCaption(final String caption) {
		getState().caption = caption;
	}

	/**
	 * by default the caption escape HTML chars
	 */
	@Override
	public void setCaptionAsHtml(final boolean captionAsHtml) {
		getState().captionAsHtml = captionAsHtml;
	}

	/**
	 * controls the position of the tab-panel<br>
	 * by default BEGINNING
	 * 
	 * @param tabPosition
	 */
	public void setTabPosition(final SliderTabPosition tabPosition) {
		getState().tabPosition = tabPosition;
	}

	/**
	 * explicitly map the custom state object to the server implementation
	 *
	 * @return
	 */
	@Override
	protected SliderPanelState getState() {
		return (SliderPanelState) super.getState();
	}

	/**
	 * @return duration in milliseconds
	 */
	public int getAnimationDuration() {
		return getState().animationDuration;
	}

	/**
	 * set the animation duration<br>
	 * by default 500ms
	 * 
	 * @param animationDuration
	 *            in milliseconds
	 */
	public void setAnimationDuration(final int animationDuration) {
		getState().animationDuration = animationDuration;
	}

	/**
	 * change to value when not already set
	 * 
	 * @param value
	 *            true means expand
	 * @param animated
	 *            should be animated or not
	 */
	public void setExpanded(final boolean value, final boolean animated) {
		getRpcProxy(SliderPanelClientRpc.class).setExpand(value, animated);
	}

	/**
	 * change from expand to collapsed...
	 */
	public void toogle() {
		getRpcProxy(SliderPanelClientRpc.class).setExpand(!getState().expand, true);
	}

	/**
	 * collapse with animation<br>
	 * when not animation is wished use {@link #setExpanded(boolean, boolean)}
	 */
	public void collapse() {
		getRpcProxy(SliderPanelClientRpc.class).setExpand(false, true);
	}

	/**
	 * expand with animation<br>
	 * when not animation is wished use {@link #setExpanded(boolean, boolean)}
	 */
	public void expand() {
		getRpcProxy(SliderPanelClientRpc.class).setExpand(true, true);
	}

}
