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

	private static final long serialVersionUID = -700114746583108434L;

	private final List<SliderPanelListener> listeners = new ArrayList<SliderPanelListener>();

	private final SliderPanelServerRpc rpc = new SliderPanelServerRpc() {

		private static final long serialVersionUID = -7181810258679430654L;

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
	 * please use {@link SliderPanelBuilder} in 1.3 this will get removed<br>
	 * expand default <b>false</b><br>
	 * mode default <b>TOP</b>
	 * 
	 * @param content
	 *            that is wrapped by the SliderPanel
	 */
	@Deprecated
	public SliderPanel(final Component content) {
		this(new SliderPanelBuilder(content));
	}

	/**
	 * please use {@link SliderPanelBuilder} in 1.3 this will get removed<br>
	 * expand default <b>false</b>
	 * 
	 * @param content
	 *            that is wrapped by the SliderPanel
	 * @param mode
	 *            default <b>TOP</b>
	 */
	@Deprecated
	public SliderPanel(final Component content, final SliderMode mode) {
		this(new SliderPanelBuilder(content).mode(mode));
	}

	/**
	 * please use {@link SliderPanelBuilder} in 1.3 this will get removed
	 * 
	 * @param content
	 *            that is wrapped by the SliderPanel
	 * @param expanded
	 *            default <b>false</b>
	 */
	@Deprecated
	public SliderPanel(final Component content, final boolean expanded) {
		this(new SliderPanelBuilder(content).expanded(expanded));
	}

	/**
	 * please use {@link SliderPanelBuilder} in 1.3 this will get removed
	 * 
	 * @param content
	 *            that is wrapped by the SliderPanel
	 * @param expanded
	 *            default <b>false</b>
	 * @param mode
	 *            default <b>TOP</b>
	 */
	@Deprecated
	public SliderPanel(final Component content, final boolean expanded, final SliderMode mode) {
		this(new SliderPanelBuilder(content).expanded(expanded)
				.mode(mode));
	}

	/**
	 * {@link SliderPanelBuilder} simplify the configuration of the {@link SliderPanel}<br>
	 * You can write your configuration with the fluent api<br>
	 * Hint: to construct {@link SliderPanel} use {@link SliderPanelBuilder#build()}
	 * 
	 * @param builder
	 *            instance of the builder. normally create instance via {@link SliderPanelBuilder#build()}
	 */
	public SliderPanel(final SliderPanelBuilder builder) {
		setContent(builder.content);
		if (builder.mode.isVertical()) {
			setHeight(100, Unit.PERCENTAGE);
			setWidth(builder.flowInContent ? 0 : builder.tabSize, Unit.PIXELS);
		} else {
			setWidth(100, Unit.PERCENTAGE);
			setHeight(builder.flowInContent ? 0 : builder.tabSize, Unit.PIXELS);
		}
		setImmediate(true);
		registerRpc(this.rpc);

		getState().pixel = builder.pixel;
		getState().expand = builder.expanded;
		getState().mode = builder.mode;
		getState().tabSize = builder.tabSize;
		getState().flowInContent = builder.flowInContent;
		getState().tabPosition = builder.tabPosition;
		getState().animationDuration = builder.animationDuration;

		if (builder.caption != null) {
			getState().caption = builder.caption;
		}
		if (builder.listeners != null) {
			this.listeners.addAll(builder.listeners);
		}
		if (builder.styles != null) {
			for (String style : builder.styles) {
				addStyleName(style);
			}
		}
	}

	/**
	 * add listener on slider interactions
	 * 
	 * @param listener
	 *            class that implements {@link SliderPanelListener}
	 */
	public void addListener(final SliderPanelListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * remove listener on slider interactions
	 * 
	 * @param listener
	 *            {@link SliderPanelListener} that should ge removed
	 * @return true when removed successfully
	 */
	public boolean removeListener(final SliderPanelListener listener) {
		return this.listeners.remove(listener);
	}

	/**
	 * Caption of the tab escape HTML
	 */
	@Override
	public void setCaption(final String caption) {
		getState().caption = caption;
	}

	/**
	 * controls the position of the tab-panel<br>
	 * 
	 * @param tabPosition
	 *            by default BEGINNING
	 */
	public void setTabPosition(final SliderTabPosition tabPosition) {
		getState().tabPosition = tabPosition;
	}

	/**
	 * explicitly map the custom state object to the server implementation
	 *
	 * @return current SliderPanelState
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
	 * by default the {@link SliderPanel} calculates it's content width/height (depending on it's mode)<br>
	 * in some cases it's useful to programmatically set this value
	 * 
	 * @param pixel
	 *            width/height (depending on it's mode)
	 */
	public void setFixedContentSize(final int pixel) {
		getState().pixel = pixel;
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
     * returns the current state of {@link SliderPanel}<br>
     * it look only on state - a possible queued change is not checked 
     * 
     * @return
     */
    public boolean isExpanded() {
        return getState().expand;
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

	/**
	 * schedule a state change of the slider on client site<br>
	 * a recall within the schedule will cancel the previous one
	 * 
	 * @param value
	 *            true means expand
	 * @param animated
	 *            should be animated or not
	 * @param delayMillis
	 *            millis in future the task will happen
	 */
	public void scheduleExpand(final boolean value, final boolean animated, final int delayMillis) {
		getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(value, animated, delayMillis);
	}

	/**
	 * schedule a change from expand to collapse vice versa in future. will trigger a timer on client site that will change the slider state
	 * <br>
	 * a recall within the schedule will cancel the previous one
	 * 
	 * @param delayMillis
	 *            millis in future the task will happen
	 */
	public void scheduleToggle(final int delayMillis) {
		getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(!getState().expand, true, delayMillis);
	}

	/**
	 * schedule a collapse in future. will trigger a timer on client site that will collapse the slider<br>
	 * a recall within the schedule will cancel the previous one
	 * 
	 * @param delayMillis
	 *            millis in future the task will happen
	 */
	public void scheduleCollapse(final int delayMillis) {
		getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(false, true, delayMillis);
	}

	/**
	 * schedule an expand in future. will trigger a timer on client site that will expand the slider<br>
	 * a recall within the schedule will cancel the previous one
	 * 
	 * @param delayMillis
	 *            millis in future the task will happen
	 */
	public void scheduleExpand(final int delayMillis) {
		getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(true, true, delayMillis);
	}

	/**
	 * width/height will get managed internally<br>
	 * to fix conent's height/width please use {@link SliderPanelBuilder#fixedContentSize(int)}
	 */
	@Deprecated
	@Override
	public void setWidth(final String width) {
		super.setWidth(width);
	}

	/**
	 * look {@link #setWidth(String)}
	 */
	@Deprecated
	@Override
	public void setWidth(final float width, final Unit unit) {
		super.setWidth(width, unit);
	}

	/**
	 * width/height will get managed internally<br>
	 * to fix conent's height/width please use {@link SliderPanelBuilder#fixedContentSize(int)}
	 */
	@Deprecated
	@Override
	public void setHeight(final String height) {
		super.setHeight(height);
	}

	/**
	 * look {@link #setHeight(String)}
	 */
	@Deprecated
	@Override
	public void setHeight(final float height, final Unit unit) {
		super.setHeight(height, unit);
	}

}
