package org.vaadin.sliderpanel;

import java.util.Date;

import javax.servlet.annotation.WebServlet;

import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderPanelListener;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Widgetset("org.vaadin.sliderpanel.Widgetset")
public class SliderPanelSampleUI extends UI {

	private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet,consetetur sadipscing elitr, "
			+ "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.";

	@Override
	protected void init(final VaadinRequest vaadinRequest) {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setMargin(false);
		mainLayout.setSpacing(false);

		// top slider
		SliderPanel topSlider = new SliderPanel(dummyContent("Top Slider Heading", 3), false, SliderMode.TOP);
		topSlider.setCaption("Top Slider");
		topSlider.setTabPosition(SliderTabPosition.BEGINNING);

		mainLayout.addComponent(topSlider);

		// center layout with left and right slider
		HorizontalLayout contentLayout = new HorizontalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setSizeFull();

		// left slider
		VerticalLayout leftDummyContent = dummyContent("Left Slider Heading", 5);
		leftDummyContent.setWidth(400, Unit.PIXELS);
		SliderPanel leftSlider = new SliderPanel(leftDummyContent, false, SliderMode.LEFT);
		leftSlider.setCaption("Left Slider");
		leftSlider.setTabPosition(SliderTabPosition.BEGINNING);
		contentLayout.addComponent(leftSlider);

		// dummy middle content
		VerticalLayout contentLabel = dummyContent("Main Content", 10);
		contentLabel.setSizeFull();

		final Label listenerLabel = new Label("event-listener for bottom-slider", ContentMode.HTML);
		listenerLabel.setWidth(100, Unit.PERCENTAGE);
		listenerLabel.setHeight(45, Unit.PIXELS);
		contentLabel.addComponent(listenerLabel);

		contentLayout.addComponent(contentLabel);
		contentLayout.setComponentAlignment(contentLabel, Alignment.MIDDLE_CENTER);
		contentLayout.setExpandRatio(contentLabel, 1);

		// right slider
		VerticalLayout rightDummyContent = dummyContent("Right Slider Heading", 3);
		rightDummyContent.setWidth(300, Unit.PIXELS);
		SliderPanel rightSlider = new SliderPanel(rightDummyContent, SliderMode.RIGHT);
		rightSlider.setCaption("Right Slider");
		rightSlider.setTabPosition(SliderTabPosition.MIDDLE);
		contentLayout.addComponent(rightSlider);

		// fit full screen
		mainLayout.addComponent(contentLayout);
		mainLayout.setExpandRatio(contentLayout, 1);

		// bottom slider
		SliderPanel bottomSlider = new SliderPanel(dummyContent("Bottom Slider Heading", 5), false, SliderMode.BOTTOM);
		bottomSlider.setCaption("Bottom Slider");
		bottomSlider.addListener(new SliderPanelListener() {
			@Override
			public void onToggle(final boolean expand) {
				listenerLabel.setValue("event-listener for bottom-slider: <b>" + (expand ? "expand" : "collapsed") + "</b> by: " + new Date().toString());
			}
		});
		mainLayout.addComponent(bottomSlider);

		setContent(mainLayout);
	}

	private VerticalLayout dummyContent(final String title, final int length) {
		String text = "";
		for (int x = 0; x <= length; x++) {
			text += LOREM_IPSUM + " ";
		}
		VerticalLayout component = new VerticalLayout(new Label(String.format("<h3>%s</h3>%s", title, text.trim()), ContentMode.HTML));
		component.setMargin(true);
		component.setSpacing(true);
		return component;
	}

	@WebServlet(
			urlPatterns = "/*",
			name = "SliderPanelSampleUIServlet",
			asyncSupported = true)
	@VaadinServletConfiguration(
			ui = org.vaadin.sliderpanel.SliderPanelSampleUI.class,
			productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
