package org.vaadin.sliderpanel;

import javax.servlet.annotation.WebServlet;

import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Widgetset("org.vaadin.sliderpanel.Widgetset")
public class SliderPanelSampleUI extends UI {

	private SliderPanel sliderPanel = null;
	private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet,consetetur sadipscing elitr, "
			+ "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.";

	@Override
	protected void init(final VaadinRequest vaadinRequest) {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setMargin(false);
		mainLayout.setSpacing(false);

		// add some simple content
		this.sliderPanel = new SliderPanel(dummyContent("Slider Heading", 3), "Top Slider", false, SliderMode.TOP);
		this.sliderPanel.setTabPosition(SliderTabPosition.BEGINNING);

		mainLayout.addComponent(this.sliderPanel);

		Component contentLabel = dummyContent("Main Content", 10);
		contentLabel.setSizeFull();
		mainLayout.addComponent(contentLabel);
		mainLayout.setComponentAlignment(contentLabel, Alignment.MIDDLE_CENTER);
		mainLayout.setExpandRatio(contentLabel, 1);

		mainLayout.addComponent(new SliderPanel(dummyContent("Bottom Slider Heading", 3), "Bottom Slider", SliderMode.BOTTOM));

		setContent(mainLayout);
	}

	private Component dummyContent(final String title, final int length) {
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
