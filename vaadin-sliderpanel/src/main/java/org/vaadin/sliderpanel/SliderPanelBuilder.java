package org.vaadin.sliderpanel;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderPanelListener;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import com.vaadin.ui.Component;

/**
 * Vaadin SliderPanelBuilder
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.2
 */
public class SliderPanelBuilder {

    protected Component content;

    protected SliderMode mode = SliderMode.TOP;

    protected boolean expanded = false;

    protected boolean flowInContent = false;

    protected int tabSize = 40;

    protected List<SliderPanelListener> listeners = null;

    protected String caption = null;

    protected SliderTabPosition tabPosition = SliderTabPosition.BEGINNING;

    protected int animationDuration = 500;

    protected List<String> styles = null;

    protected int pixel = -1;

    /**
     * creates an builder instance that can be configured fluently
     * 
     * @param content
     *            that is wrapped by the SliderPanel<br>
     *            typically it's a Vertical or HorizontalLayout
     */
    public SliderPanelBuilder(Component content) {
        this.content = content;
    }

    /**
     * creates an builder instance that can be configured fluently
     * 
     * @param content
     *            that is wrapped by the SliderPanel<br>
     *            typically it's a Vertical or HorizontalLayout
     * @param caption
     *            of the slider navigation element
     */
    public SliderPanelBuilder(Component content, String caption) {
        this.content = content;
        this.caption = caption;
    }

    /**
     * Which type of display you want to have
     * 
     * @param mode
     *            default <b>TOP</b>
     * @return builder
     */
    public SliderPanelBuilder mode(SliderMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Should the slider be expanded on intial paint<br>
     * 
     * @param expanded
     *            default <b>false</b>
     * @return builder
     */
    public SliderPanelBuilder expanded(boolean expanded) {
        this.expanded = expanded;
        return this;
    }

    /**
     * Should the navigator flow within the content of the other layouts below
     * 
     * @param flowInContent
     *            default <b>false</b>
     * @return builder
     */
    public SliderPanelBuilder flowInContent(boolean flowInContent) {
        this.flowInContent = flowInContent;
        return this;
    }

    /**
     * allows to change the short width/height of the tab-caption<br>
     * you need to change also your css when you change from default value<br>
     * important <b>need to get setted before first attach!</b>
     * 
     * @param tabSize
     *            default <b>40</b>
     * @return builder
     */
    public SliderPanelBuilder tabSize(int tabSize) {
        this.tabSize = tabSize;
        return this;
    }

    /**
     * add the listener to the list of Listeners
     * 
     * @param listener
     * @return builder
     */
    public SliderPanelBuilder listener(SliderPanelListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<SliderPanelListener>();
        }
        this.listeners.add(listener);
        return this;
    }

    /**
     * set caption of the slider navigation element
     * 
     * @param caption
     * @return builder
     */
    public SliderPanelBuilder caption(String caption) {
        this.caption = caption;
        return this;
    }

    /**
     * Which type of display you want to have<br>
     * This controls the position of the navigation element within the {@link SliderPanel} total area
     * 
     * @param tabPosition
     *            default <b>BEGINNING</b>
     * @return builder
     */
    public SliderPanelBuilder tabPosition(SliderTabPosition tabPosition) {
        this.tabPosition = tabPosition;
        return this;
    }

    /**
     * How long a collapse/expand should take in milliseconds
     * 
     * @param animationDuration
     *            defaul <b>500</b>
     * @return builder
     */
    public SliderPanelBuilder animationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        return this;
    }

    /**
     * add a style to the sliderPanel
     * 
     * @param style
     * @return builder
     */
    public SliderPanelBuilder style(String style) {
        if (styles == null) {
            styles = new ArrayList<String>();
        }
        this.styles.add(style);
        return this;
    }

    /**
     * by default the {@link SliderPanel} calculates it's content width/height (depending on it's mode)<br>
     * in some cases it's useful to programmatically set this value
     * 
     * @param pixel
     *            width/height (depending on it's mode)
     * @return builder
     */
    public SliderPanelBuilder fixedContentSize(int pixel) {
        this.pixel = pixel;
        return this;
    }

    /**
     * generates the SliderPanel
     * 
     * @return instance of configured {@link SliderPanel}
     */
    public SliderPanel build() {
        return new SliderPanel(this);
    }
}
