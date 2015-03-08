package org.vaadin.sliderpanel;

import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderPanelClientRpc;
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

	@SuppressWarnings("serial")
	private final SliderPanelServerRpc rpc = new SliderPanelServerRpc() {
		@Override
		public void clicked(final boolean visible) {
			getState().expand = visible;
		}
	};

	public SliderPanel(final Component content) {
		this(content, null, true, SliderMode.TOP);
	}

	public SliderPanel(final Component content, final SliderMode mode) {
		this(content, null, true, mode);
	}

	public SliderPanel(final Component content, final String caption) {
		this(content, caption, true, SliderMode.TOP);
	}

	public SliderPanel(final Component content, final String caption, final SliderMode mode) {
		this(content, caption, true, mode);
	}

	public SliderPanel(final Component content, final boolean expanded) {
		this(content, null, expanded, SliderMode.TOP);
	}

	public SliderPanel(final Component content, final boolean expanded, final SliderMode mode) {
		this(content, null, expanded, mode);
	}

	public SliderPanel(final Component content, final String caption, final boolean expanded, final SliderMode mode) {
		setContent(content);
		setWidth(100, Unit.PERCENTAGE);
		setHeight(45, Unit.PIXELS);
		setImmediate(true);

		registerRpc(this.rpc);

		getState().caption = caption;
		getState().expand = expanded;
		getState().mode = mode;
	}

	/**
	 * controls the position of the tab-pabel<br>
	 * by default END
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

	public int getAnimationDuration() {
		return getState().animationDuration;
	}

	public void setAnimationDuration(final int animationDuration) {
		getState().animationDuration = animationDuration;
	}

	public void setExpanded(final boolean value, final boolean animated) {
		getRpcProxy(SliderPanelClientRpc.class).setExpand(value, animated);
	}

	public void toogle() {
		getRpcProxy(SliderPanelClientRpc.class).setExpand(!getState().expand, true);
	}

	public void collapse() {
		getRpcProxy(SliderPanelClientRpc.class).setExpand(false, true);
	}

	public void expand() {
		getRpcProxy(SliderPanelClientRpc.class).setExpand(true, true);
	}

}
