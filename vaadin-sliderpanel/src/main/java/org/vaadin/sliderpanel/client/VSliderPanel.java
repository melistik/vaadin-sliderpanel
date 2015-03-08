package org.vaadin.sliderpanel.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;

public class VSliderPanel extends SimplePanel {

	public static final String CLASSNAME = "v-sliderpanel";

	private final DivElement wrapperNode, contentNode, navigationElem, captionNode, line, tabElem;
	private boolean expand = false;
	private int animationDuration;
	private Integer componentHeight = null;
	private SliderMode mode = null;
	private final SliderTabPosition tabPosition = null;

	private final SliderAnimation slideAnimation = new SliderAnimation();
	private VSliderPanelToggleListener listener;

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

		this.line = Document.get()
				.createDivElement();
		this.line.setClassName(VSliderPanel.CLASSNAME + "-line");
		this.navigationElem.appendChild(this.line);

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
	}

	public void setMode(final SliderMode sliderMode) {
		if (this.mode == null) {
			this.mode = sliderMode;
			this.wrapperNode.addClassName("mode-" + this.mode.name()
					.toLowerCase());

			if (this.mode.equals(SliderMode.BOTTOM)) {
				// rearrange order contentNode after navigationElem
				this.wrapperNode.removeChild(this.contentNode);
				this.wrapperNode.appendChild(this.contentNode);

				this.navigationElem.removeChild(this.line);
				this.navigationElem.appendChild(this.line);
			}
		}
	}

	public void setTabPosition(final SliderTabPosition tabPosition) {
		if (this.tabPosition != null) {
			this.tabElem.removeClassName("tab-" + tabPosition.name()
					.toLowerCase());
		}
		this.tabElem.addClassName("tab-" + tabPosition.name()
				.toLowerCase());
	}

	public void initialize(final boolean expand) {
		this.expand = expand;
		animateTo(expand, 0);
	}

	public void setCaption(final String caption) {
		this.captionNode.setInnerHTML(caption);
	}

	public void setExpand(final boolean expand, final boolean animated) {
		animateTo(expand, animated ? this.animationDuration : 0);
	}

	public void setAnimationDuration(final int animationDuration) {
		this.animationDuration = animationDuration;
	}

	/**
	 * handel the closed/open className on the TabElement
	 */
	private void updateTabElemClassName() {
		if (this.expand) {
			this.tabElem.removeClassName("closed");
			this.tabElem.addClassName("open");
		} else {
			this.tabElem.removeClassName("open");
			this.tabElem.addClassName("closed");
		}
	}

	@Override
	public void onBrowserEvent(final Event event) {
		if (event != null && (event.getTypeInt() == Event.ONCLICK)) {
			animateTo(!this.expand, this.animationDuration);
		}
		super.onBrowserEvent(event);
	}

	native void consoleLog(String message) /*-{
											console.log( message );
											}-*/;

	@SuppressWarnings("deprecation")
	@Override
	protected com.google.gwt.user.client.Element getContainerElement() {
		return DOM.asOld(this.contentNode);
	}

	public void setToggleListener(final VSliderPanelToggleListener toggleListener) {
		this.listener = toggleListener;
	}

	private class SliderAnimation extends Animation {
		private boolean animateToExpand = false;

		public void setAnimateToExpand(final boolean expand) {
			this.animateToExpand = expand;
		}

		@Override
		protected void onUpdate(final double progress) {
			final double height = extractProportionalLength(progress);
			VSliderPanel.this.contentNode.getStyle()
					.setHeight(height, Style.Unit.PX);
			if (VSliderPanel.this.mode.equals(SliderMode.BOTTOM)) {
				VSliderPanel.this.contentNode.getStyle()
						.setTop((-1) * height, Style.Unit.PX);
				VSliderPanel.this.tabElem.getStyle()
						.setTop((-1) * height, Style.Unit.PX);
			}
		}

		@Override
		protected void onStart() {
			VSliderPanel.this.contentNode.getStyle()
					.setDisplay(Display.BLOCK);
			consoleLog("mode: " + VSliderPanel.this.mode + ", onStart: " + this.animateToExpand);
			if (VSliderPanel.this.componentHeight == null || VSliderPanel.this.componentHeight <= 0) {
				VSliderPanel.this.contentNode.getStyle()
						.clearHeight();
				if (VSliderPanel.this.contentNode.getFirstChildElement() != null) {
					VSliderPanel.this.componentHeight = VSliderPanel.this.contentNode.getFirstChildElement()
							.getOffsetHeight();
				}
				consoleLog("componentHeight: " + VSliderPanel.this.componentHeight);
			}
		};

		@Override
		protected void onComplete() {
			VSliderPanel.this.expand = this.animateToExpand;
			updateTabElemClassName();

			if (!VSliderPanel.this.expand) {
				VSliderPanel.this.contentNode.getStyle()
						.setDisplay(Display.NONE);
			} else {
				VSliderPanel.this.contentNode.getStyle()
						.setHeight(VSliderPanel.this.componentHeight, Style.Unit.PX);

				if (VSliderPanel.this.mode.equals(SliderMode.BOTTOM)) {
					VSliderPanel.this.contentNode.getStyle()
							.setTop((-1) * VSliderPanel.this.componentHeight, Style.Unit.PX);
					VSliderPanel.this.tabElem.getStyle()
							.setTop((-1) * VSliderPanel.this.componentHeight, Style.Unit.PX);
				}
			}

			if (VSliderPanel.this.listener != null) {
				VSliderPanel.this.listener.onToggle(VSliderPanel.this.expand);
			}
		};

		private int extractProportionalLength(final double progress) {
			if (this.animateToExpand) {
				return (int) (VSliderPanel.this.componentHeight * progress);
			} else {
				return (int) (VSliderPanel.this.componentHeight * (1.0 - progress));
			}
		}
	}

	public void animateTo(final boolean expand, final int duration) {
		this.slideAnimation.setAnimateToExpand(expand);
		this.slideAnimation.run(duration);
	}

}
