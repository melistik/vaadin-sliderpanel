package org.vaadin.sliderpanel;

import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;
import org.vaadin.sliderpanel.client.*;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Vaadin SliderPanel
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public class SliderPanel extends AbstractSingleComponentContainer {


    private final SliderPanelServerRpc rpc = new SliderPanelServerRpc() {

        @Override
        public void clicked(final boolean visible) {
            getState().expand = visible;
            SliderPanel.this.fireEvent(new SliderPanelToggleEvent(SliderPanel.this, visible));
        }
    };

    /**
     * {@link SliderPanelBuilder} simplify the configuration of the {@link SliderPanel}<br>
     * You can write your configuration with the fluent api<br>
     * Hint: to construct {@link SliderPanel} use {@link SliderPanelBuilder#build()}
     *
     * @param builder instance of the builder. normally create instance via {@link SliderPanelBuilder#build()}
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
        registerRpc(this.rpc);

        getState().pixel = builder.pixel;
        getState().expand = builder.expanded;
        getState().mode = builder.mode;
        getState().tabSize = builder.tabSize;
        getState().flowInContent = builder.flowInContent;
        getState().tabPosition = builder.tabPosition;
        getState().animationDuration = builder.animationDuration;
        getState().autoCollapseSlider = builder.autoCollapseSlider;
        getState().zIndex = builder.zIndex;

        if (builder.caption != null) {
            getState().caption = builder.caption;
        }
        if (builder.listeners != null) {
            builder.listeners.forEach(l -> {
                addToggleListener(l);
            });
        }
        if (builder.styles != null) {
            for (String style : builder.styles) {
                addStyleName(style);
            }
        }
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
     * @param tabPosition by default BEGINNING
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
     * @param animationDuration in milliseconds
     */
    public void setAnimationDuration(final int animationDuration) {
        getState().animationDuration = animationDuration;
    }

    /**
     * by default the {@link SliderPanel} calculates it's content width/height (depending on it's mode)<br>
     * in some cases it's useful to programmatically set this value
     *
     * @param pixel width/height (depending on it's mode)
     */
    public void setFixedContentSize(final int pixel) {
        getState().pixel = pixel;
    }

    /**
     * by default the {@link SliderPanel} stays open when use clicks outside<br>
     * when you enable autoCollapse the slider closes in mode of expand when user clicks somewhere else
     *
     * @param autoCollapseSlider enable auto collapse in expand state
     */
    public void setAutoCollapseSlider(boolean autoCollapseSlider) {
        getState().autoCollapseSlider = autoCollapseSlider;
    }

    /**
     * z-Index of navigator, content and wrapper<br>
     * you can specify for multiple sliders which lays above another
     *
     * @param zIndex default <b>9990</b>
     */
    public void setZIndex(int zIndex) {
        getState().zIndex = zIndex;
    }

    /**
     * change to value when not already set
     *
     * @param value    true means expand
     * @param animated should be animated or not
     */
    public void setExpanded(final boolean value, final boolean animated) {
        getRpcProxy(SliderPanelClientRpc.class).setExpand(value, animated);
    }

    /**
     * returns the current state of {@link SliderPanel}<br>
     * it look only on state - a possible queued change is not checked
     *
     * @return is expanded
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
     * @param value       true means expand
     * @param animated    should be animated or not
     * @param delayMillis millis in future the task will happen
     */
    public void scheduleExpand(final boolean value, final boolean animated, final int delayMillis) {
        getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(value, animated, delayMillis);
    }

    /**
     * schedule a change from expand to collapse vice versa in future. will trigger a timer on client site that will change the slider state
     * <br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
    public void scheduleToggle(final int delayMillis) {
        getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(!getState().expand, true, delayMillis);
    }

    /**
     * schedule a collapse in future. will trigger a timer on client site that will collapse the slider<br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
    public void scheduleCollapse(final int delayMillis) {
        getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(false, true, delayMillis);
    }

    /**
     * schedule an expand in future. will trigger a timer on client site that will expand the slider<br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
    public void scheduleExpand(final int delayMillis) {
        getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(true, true, delayMillis);
    }


    public Registration addToggleListener(SliderPanelToggleListener listener) {
        return this.addListener(SliderPanelToggleEvent.class, listener, SliderPanelToggleListener.ELEMENT_TOGGLED_METHOD);
    }


    public interface SliderPanelToggleListener extends Serializable {

        Method ELEMENT_TOGGLED_METHOD = ReflectTools
                .findMethod(SliderPanel.SliderPanelToggleListener.class, "toggle",
                        SliderPanel.SliderPanelToggleEvent.class);

        void toggle(SliderPanelToggleEvent event);
    }

    public static class SliderPanelToggleEvent extends Component.Event {

        private boolean expand;

        public SliderPanelToggleEvent(SliderPanel source, boolean expand) {
            super(source);
            this.expand = expand;
        }

        @Override
        public SliderPanel getSource() {
            return (SliderPanel) super.getSource();
        }

        public boolean isExpand() {
            return expand;
        }
    }

    /**
     * allow to disable changing toggle<br>
     *     content is not disabled
     *
     * @param enabled by default enabled
     */
    public void setEnabledToggle(boolean enabled) {
        getState().enableToggle = enabled;
    }

    public boolean isEnabledToggle() {
        return getState().enableToggle;
    }
}
