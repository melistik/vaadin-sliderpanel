package org.vaadin.sliderpanel;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderPanelListener;
import org.vaadin.sliderpanel.client.SliderTabPosition;
import org.vaadin.sliderpanel.client.VSliderPanel;

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
	
    protected boolean autoCollapseSlider = false;

    protected int zIndex = 9990;
    

	/**
	 * creates an builder instance that can be configured fluently
	 * 
	 * @param content
	 *            that is wrapped by the SliderPanel<br>
	 *            typically it's a Vertical or HorizontalLayout
	 */
	public SliderPanelBuilder(final Component content) {
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
	public SliderPanelBuilder(final Component content, final String caption) {
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
	public SliderPanelBuilder mode(final SliderMode mode) {
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
	public SliderPanelBuilder expanded(final boolean expanded) {
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
	public SliderPanelBuilder flowInContent(final boolean flowInContent) {
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
	public SliderPanelBuilder tabSize(final int tabSize) {
		this.tabSize = tabSize;
		return this;
	}

	/**
	 * add the listener to the list of Listeners
	 * 
	 * @param listener
	 *            instance of {@link SliderPanelListener}
	 * @return builder
	 */
	public SliderPanelBuilder listener(final SliderPanelListener listener) {
		if (this.listeners == null) {
			this.listeners = new ArrayList<SliderPanelListener>();
		}
		this.listeners.add(listener);
		return this;
	}

	/**
	 * set caption
	 * 
	 * @param caption
	 *            of the slider navigation element
	 * @return builder
	 */
	public SliderPanelBuilder caption(final String caption) {
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
	public SliderPanelBuilder tabPosition(final SliderTabPosition tabPosition) {
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
	public SliderPanelBuilder animationDuration(final int animationDuration) {
		this.animationDuration = animationDuration;
		return this;
	}

	/**
	 * add a style to the sliderPanel
	 * 
	 * @param style
	 *            style that should get add to each nodes of {@link VSliderPanel}<br>
	 *            could get called multiple times
	 * @return builder
	 */
	public SliderPanelBuilder style(final String style) {
		if (this.styles == null) {
			this.styles = new ArrayList<String>();
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
	public SliderPanelBuilder fixedContentSize(final int pixel) {
		this.pixel = pixel;
		return this;
	}
	
    /**
     * by default the {@link SliderPanel} stays open when use clicks outside<br>
     * when you enable autoCollapse the slider closes in mode of expand when user clicks somewhere else
     * 
     * @param autoCollapseSlider enable auto collapse in expand state
     * @return builder
     */
    public SliderPanelBuilder autoCollapseSlider(final boolean autoCollapseSlider) {
        this.autoCollapseSlider = autoCollapseSlider;
        return this;
    }
    
    /**
     * z-Index of navigator, content and wrapper<br>
     * you can specify for multiple sliders which lays above another
     * @param zIndex
     *            default <b>9990</b>
     * @return
     */
    public SliderPanelBuilder zIndex(int zIndex) {
    	this.zIndex = zIndex;
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
