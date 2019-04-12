package org.vaadin.sliderpanel.client;

import org.vaadin.sliderpanel.SliderPanel;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * the main gwt implementation of the {@link SliderPanel}
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public class VSliderPanel extends SimplePanel implements NativePreviewHandler {

    public static final String CLASSNAME = "v-sliderpanel";

    private String wrapperBaseClassName = "", tabBaseClassName = "";

    private final DivElement wrapperNode, contentNode, navigationElem, captionNode, tabElem;

    private boolean initialized = false;

    private boolean expand = false;

    private boolean flowInContent = false;

    private int animationDuration;

    private Integer componentSize = null;

    private SliderMode mode = null;

    private final SliderTabPosition tabPosition = null;

    private int tabSize;

    private final SliderAnimation slideAnimation = new SliderAnimation();

    private SliderPanelListener listener;

    private boolean autoCollapseSlider = false;

    private boolean enabled = true;

    public VSliderPanel() {
        super();
        // main wrapper of the component
        this.wrapperNode = Document.get()
                                   .createDivElement();
        this.wrapperNode.setClassName(VSliderPanel.CLASSNAME + "-wrapper");
        getElement().appendChild(this.wrapperNode);

        // container that holds the content
        this.contentNode = Document.get()
                                   .createDivElement();
        this.contentNode.setClassName(VSliderPanel.CLASSNAME + "-content");
        this.contentNode.getStyle()
                        .setDisplay(Display.BLOCK);
        this.wrapperNode.appendChild(this.contentNode);

        // wrapper for collapsed content line, tab with caption and icon
        this.navigationElem = Document.get()
                                      .createDivElement();
        this.navigationElem.setClassName(VSliderPanel.CLASSNAME + "-navigator");

        this.tabElem = Document.get()
                               .createDivElement();
        this.tabElem.setClassName(VSliderPanel.CLASSNAME + "-tab");
        this.navigationElem.appendChild(this.tabElem);

        this.captionNode = Document.get()
                                   .createDivElement();
        this.captionNode.setClassName(VSliderPanel.CLASSNAME + "-caption");
        this.tabElem.appendChild(this.captionNode);

        DivElement toggleLabel = Document.get()
                                         .createDivElement();
        toggleLabel.setClassName(VSliderPanel.CLASSNAME + "-icon");
        this.tabElem.appendChild(toggleLabel);

        DOM.sinkEvents(this.tabElem, Event.ONCLICK);
        this.wrapperNode.appendChild(this.navigationElem);

        Event.addNativePreviewHandler(this);
    }

    private void setMode(final SliderMode sliderMode, final boolean flowInContent) {
        if (this.mode == null) {
            this.mode = sliderMode;
            this.flowInContent = flowInContent;
            this.wrapperNode.addClassName("mode-" + this.mode.name()
                                                             .toLowerCase());
            this.wrapperNode.addClassName("layout-" + (this.mode.isVertical() ? "vertical" : "horizontal"));

            if (flowInContent) {
                this.wrapperNode.addClassName("flow-in-content");
            }

            this.wrapperBaseClassName = this.wrapperNode.getClassName();

            if (this.mode.equals(SliderMode.BOTTOM) || this.mode.equals(SliderMode.LEFT)) {
                // rearrange order contentNode after navigationElem
                this.wrapperNode.removeChild(this.contentNode);
                this.wrapperNode.appendChild(this.contentNode);
            }
        }
    }

    private void setTabPosition(final SliderTabPosition tabPosition) {
        if (this.tabPosition != null) {
            this.tabElem.removeClassName("tab-" + tabPosition.name()
                                                             .toLowerCase());
        }
        this.tabElem.addClassName("tab-" + tabPosition.name()
                                                      .toLowerCase());
        this.tabBaseClassName = this.tabElem.getClassName();
    }

    public void configure(final SliderMode sliderMode, final boolean flowInContent, final SliderTabPosition tabPosition, final Integer pixel) {
        if (!this.initialized) {
            setMode(sliderMode, flowInContent);
            setTabPosition(tabPosition);
            if (pixel > 0) {
                this.componentSize = pixel;
            }
            this.initialized = true;
        }
    }

    public void setFixedContentSize(final int pixel) {
        this.componentSize = pixel;
    }

    public void initialize(final boolean expand, final int tabSize) {
        this.expand = expand;
        this.tabSize = tabSize;
        animateTo(expand, 0, false);
    }

    public void setCaption(final String caption, final boolean captionAsHtml) {
        String captionContent = caption != null ? caption : "";
        if (!captionAsHtml) {
            captionContent = SafeHtmlUtils.htmlEscape(captionContent);
        }
        this.captionNode.setInnerHTML(captionContent);
    }

    public void setExpand(final boolean expand, final boolean animated) {
        if (!isEnabled()) {
            return;
        }
        animateTo(expand, animated ? this.animationDuration : 0, true);
    }

    public void setAnimationDuration(final int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public void setAutoCollapseSlider(boolean autoCollapseSlider) {
        this.autoCollapseSlider = autoCollapseSlider;
    }

    public void setZIndex(int zIndex) {
    	this.contentNode.getStyle().setZIndex(zIndex);
    	this.navigationElem.getStyle().setZIndex(zIndex+1);
    	this.wrapperNode.getStyle().setZIndex(zIndex);
    }

    /**
     * handel the closed/open className on the TabElement
     */
    private void updateTabElemClassName() {
        if (this.expand) {
            this.tabElem.removeClassName("closed");
            this.tabElem.addClassName("open");
        }
        else {
            this.tabElem.removeClassName("open");
            this.tabElem.addClassName("closed");
        }
    }

    @Override
    public void onBrowserEvent(final Event event) {
        if (!isEnabled()) {
            return;
        }
        if (event != null && (event.getTypeInt() == Event.ONCLICK)) {
            animateTo(!this.expand, this.animationDuration, true);
        }
        super.onBrowserEvent(event);
    }

    /**
     * used to log in javascript console
     *
     * @param message info to get logged
     */
    native void consoleLog(final String message) /*-{
                                                 console.log( message );
                                                 }-*/;

    @SuppressWarnings("deprecation")
    @Override
    protected com.google.gwt.user.client.Element getContainerElement() {
        return DOM.asOld(this.contentNode);
    }

    public void setToggleListener(final SliderPanelListener toggleListener) {
        this.listener = toggleListener;
    }

    private class SliderAnimation extends Animation {
        private boolean animateToExpand = false;

        private boolean fireToggleEvent = true;

        public void setAnimateToExpand(final boolean expand, final boolean fireToggleEvent) {
            this.animateToExpand = expand;
            this.fireToggleEvent = fireToggleEvent;
        }

        private void changeSize(final double size) {
            if (VSliderPanel.this.mode.isVertical()) {
                if (VSliderPanel.this.mode.equals(SliderMode.RIGHT)) {
                    VSliderPanel.this.contentNode.getStyle()
                                                 .setWidth(size, Style.Unit.PX);

                    if (VSliderPanel.this.flowInContent) {
                        // new
                        VSliderPanel.this.navigationElem.getStyle()
                                                        .setLeft(-1 * (size + VSliderPanel.this.tabSize), Style.Unit.PX);
                        VSliderPanel.this.contentNode.getStyle()
                                                     .setLeft(-1 * size, Style.Unit.PX);
                    }
                    else {
                        VSliderPanel.this.navigationElem.getStyle()
                                                        .setLeft(-1 * size, Style.Unit.PX);
                        VSliderPanel.this.contentNode.getStyle()
                                                     .setLeft(-1 * size + VSliderPanel.this.tabSize, Style.Unit.PX);
                    }
                }
                else {
                    VSliderPanel.this.contentNode.getStyle()
                                                 .setWidth(VSliderPanel.this.componentSize, Style.Unit.PX);
                    VSliderPanel.this.contentNode.getStyle().setWidth(size, Style.Unit.PX);
                    VSliderPanel.this.contentNode.getFirstChildElement().getStyle().setPosition(Position.ABSOLUTE);
                    VSliderPanel.this.contentNode.getFirstChildElement().getStyle().setLeft(-1 * (VSliderPanel.this.componentSize - size), Style.Unit.PX);


                    if (VSliderPanel.this.flowInContent) {
                        VSliderPanel.this.navigationElem.getStyle()
                                                        .setRight(-1 * (size + VSliderPanel.this.tabSize), Style.Unit.PX);
                    }
                    else {
                        VSliderPanel.this.navigationElem.getStyle()
                                                        .setRight(-1 * size, Style.Unit.PX);
                    }
                }
            }
            else {
                VSliderPanel.this.contentNode.getStyle()
                                             .setHeight(size, Style.Unit.PX);
                if (VSliderPanel.this.mode.equals(SliderMode.BOTTOM)) {
                    if (VSliderPanel.this.flowInContent) {
                        // new
                        VSliderPanel.this.contentNode.getStyle()
                                                     .setTop(-1 * size, Style.Unit.PX);
                        VSliderPanel.this.navigationElem.getStyle()
                                                        .setTop(-1 * (size + VSliderPanel.this.tabSize), Style.Unit.PX);
                    }
                    else {
                        VSliderPanel.this.contentNode.getStyle()
                                                     .setTop(-1 * size + VSliderPanel.this.tabSize, Style.Unit.PX);
                        VSliderPanel.this.navigationElem.getStyle()
                                                        .setTop(-1 * size, Style.Unit.PX);
                    }
                }
                else {
                    VSliderPanel.this.contentNode.getFirstChildElement()
                            .getStyle()
                            .setPosition(Position.ABSOLUTE);
                    VSliderPanel.this.contentNode.getFirstChildElement()
                            .getStyle()
                            .setTop(-1 * (VSliderPanel.this.componentSize - size), Style.Unit.PX);
                    VSliderPanel.this.navigationElem.getStyle()
                            .setTop(size, Style.Unit.PX);
                }
            }
        }

        @Override
        protected void onUpdate(final double progress) {
            changeSize(extractProportionalLength(progress));
        }

        @Override
        protected void onStart() {
            VSliderPanel.this.contentNode.getStyle()
                                         .setDisplay(Display.BLOCK);
            if (VSliderPanel.this.componentSize == null || VSliderPanel.this.componentSize <= 0) {
                if (VSliderPanel.this.mode.isVertical()) {
                    VSliderPanel.this.contentNode.getStyle()
                                                 .clearWidth();
                    if (VSliderPanel.this.contentNode.getFirstChildElement() != null) {
                        VSliderPanel.this.componentSize = VSliderPanel.this.contentNode.getFirstChildElement()
                                                                                       .getOffsetWidth();
                    }
                }
                else {
                    VSliderPanel.this.contentNode.getStyle()
                                                 .clearHeight();
                    if (VSliderPanel.this.contentNode.getFirstChildElement() != null) {
                        VSliderPanel.this.componentSize = VSliderPanel.this.contentNode.getFirstChildElement()
                                                                                       .getOffsetHeight();
                    }
                }
            }
        };

        @Override
        protected void onComplete() {
            VSliderPanel.this.expand = this.animateToExpand;
            updateTabElemClassName();

            if (!VSliderPanel.this.expand) {
                changeSize(0);
            }
            else {
                changeSize(VSliderPanel.this.componentSize);
            }

            if (VSliderPanel.this.listener != null && this.fireToggleEvent) {
                VSliderPanel.this.listener.onToggle(VSliderPanel.this.expand);
            }
        };

        private int extractProportionalLength(final double progress) {
            if (this.animateToExpand) {
                return (int) (VSliderPanel.this.componentSize * progress);
            }
            else {
                return (int) (VSliderPanel.this.componentSize * (1.0 - progress));
            }
        }
    }

    /**
     * run animation with params
     *
     * @param expand final state
     * @param duration milliseconds how long the animation takes
     * @param fireToggleEvent should an event get fired afterwards
     */
    public void animateTo(final boolean expand, final int duration, final boolean fireToggleEvent) {

        if (this.slideAnimation.isRunning())
            return;

        this.slideAnimation.setAnimateToExpand(expand, fireToggleEvent);
        this.slideAnimation.run(duration);
    }

    public class ScheduleTimer extends Timer {
        private boolean expand, animated;

        @Override
        public void run() {
            setExpand(this.expand, this.animated);
        }

        public void setValues(final boolean expand, final boolean animated) {
            this.expand = expand;
            this.animated = animated;
        }
    }

    private ScheduleTimer scheduleTimer;

    /**
     * schedule animation with delay
     *
     * @param expand final state
     * @param animated should get animated
     * @param delayMillis milliseconds of delayed execution
     */
    public void scheduleExpand(final boolean expand, final boolean animated, final int delayMillis) {
        if (this.scheduleTimer == null) {
            this.scheduleTimer = new ScheduleTimer();
        }
        this.scheduleTimer.setValues(expand, animated);
        this.scheduleTimer.schedule(delayMillis);
    }

    /**
     * adds custom styleNames to wrapper
     *
     * @param styles add styleName to all nodes
     */
    public void setStyles(final String styles) {
        this.wrapperNode.setClassName(this.wrapperBaseClassName + styles);
        this.contentNode.setClassName(VSliderPanel.CLASSNAME + "-content" + styles);
        this.navigationElem.setClassName(VSliderPanel.CLASSNAME + "-navigator" + styles);
        this.tabElem.setClassName(this.tabBaseClassName + styles);
        // to set old open/closed class
        updateTabElemClassName();
    }

    /**
     * checks whether the event comes from an element within the slider dom tree
     *
     * @param event NativeEvent
     * @return true when events comes from within
     */
    private boolean eventTargetsPopup(NativeEvent event) {
        EventTarget target = event.getEventTarget();
        if (Element.is(target)) {
            return getElement().isOrHasChild(Element.as(target));
        }
        return false;
    }

    /**
     * checks whether the event come's from a elements that lays visually within the slider<br>
     * it doesn't lay directly in the dom tree - for example dropdown popups
     *
     * @param event NativeEvent
     * @return true when events comes from within
     */
    private boolean eventTargetsInnerElementsPopover(NativeEvent event) {
        EventTarget target = event.getEventTarget();
        if (Element.is(target)) {
            Element targetElement = Element.as(target);

            int absoluteLeft = targetElement.getAbsoluteLeft();
            int absoluteTop = targetElement.getAbsoluteTop();

            return contentNode.getAbsoluteLeft() <= absoluteLeft && contentNode.getAbsoluteRight() >= absoluteLeft && contentNode.getAbsoluteTop() <= absoluteTop
                    && contentNode.getAbsoluteBottom() >= absoluteTop;
        }
        return false;
    }

    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event) {
        if (autoCollapseSlider && event != null && !event.isCanceled() && expand) {
            Event nativeEvent = Event.as(event.getNativeEvent());

            switch (nativeEvent.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                case Event.ONTOUCHSTART:
                case Event.ONDBLCLICK:

                    if (eventTargetsPopup(nativeEvent)) {
                        return;
                    }
                    if (eventTargetsInnerElementsPopover(nativeEvent)) {
                        return;
                    }
                    setExpand(false, true);
            }
        }
    }


    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            wrapperNode.addClassName("v-disabled");
            tabElem.setAttribute("disabled", "on");
            return;
        }
        wrapperNode.removeClassName("v-disabled");
        tabElem.removeAttribute("disabled");
    }
}
