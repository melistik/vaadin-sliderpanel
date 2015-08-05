package org.vaadin.sliderpanel;

import java.util.Date;

import javax.servlet.annotation.WebServlet;

import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderPanelListener;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
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

        final SliderPanel firstTopSlider =
            new SliderPanelBuilder(dummyContent("First Top Slider Heading", 1), "First Top Slider")
                .tabPosition(SliderTabPosition.MIDDLE).style(SliderPanelStyles.COLOR_WHITE).build();

        final SliderPanel secondTopSlider =
            new SliderPanelBuilder(dummyContent("Second Top Slider Heading", 1), "Second Top Slider fixedContentSize")
                .tabPosition(SliderTabPosition.MIDDLE).style(SliderPanelStyles.COLOR_GREEN)
                .fixedContentSize(Page.getCurrent().getBrowserWindowHeight() - 100).build();

        // Two top slider
        HorizontalLayout topTwoSliderLayout = new HorizontalLayout();
        topTwoSliderLayout.setWidth(100, Unit.PERCENTAGE);
        topTwoSliderLayout.setHeight(40, Unit.PIXELS);
        topTwoSliderLayout.setMargin(false);
        topTwoSliderLayout.setSpacing(false);
        topTwoSliderLayout.addComponent(firstTopSlider);
        topTwoSliderLayout.setExpandRatio(firstTopSlider, 1);
        topTwoSliderLayout.addComponent(secondTopSlider);
        topTwoSliderLayout.setExpandRatio(secondTopSlider, 1);
        mainLayout.addComponent(topTwoSliderLayout);

        // center layout with left and right slider
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSpacing(false);
        contentLayout.setSizeFull();

        // left slider
        VerticalLayout leftDummyContent = dummyContent("Left Slider Heading", 3);
        leftDummyContent.setWidth(400, Unit.PIXELS);
        SliderPanel leftSlider =
            new SliderPanelBuilder(leftDummyContent, "Left slow Slider (flow in Content)").mode(SliderMode.LEFT)
                .tabPosition(SliderTabPosition.BEGINNING)
                .animationDuration(2000).flowInContent(true).build();
        contentLayout.addComponent(leftSlider);

        // dummy middle content
        VerticalLayout contentLabel = dummyContent("Main Content", 10);
        contentLabel.setSizeFull();
        contentLabel.setMargin(false);

        contentLabel.addComponent(new Button("schedule toggle first-top-slider", new Button.ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                Notification.show("start schedule toggle on first-top-slider in 2 secs", Type.TRAY_NOTIFICATION);
                firstTopSlider.scheduleToggle(2000);
            }
        }));

        final Label listenerLabel = new Label("event-listener for right-slider", ContentMode.HTML);
        listenerLabel.setWidth(100, Unit.PERCENTAGE);
        listenerLabel.setHeight(45, Unit.PIXELS);
        contentLabel.addComponent(listenerLabel);

        contentLayout.addComponent(contentLabel);
        contentLayout.setComponentAlignment(contentLabel, Alignment.MIDDLE_CENTER);
        contentLayout.setExpandRatio(contentLabel, 1);

        // right slider
        VerticalLayout rightDummyContent = dummyContent("Right Slider Heading", 2);
        rightDummyContent.setWidth(300, Unit.PIXELS);
        SliderPanel rightSlider =
            new SliderPanelBuilder(rightDummyContent, "Right Slider").mode(SliderMode.RIGHT).tabPosition(SliderTabPosition.MIDDLE)
                .flowInContent(true)
                .style(SliderPanelStyles.COLOR_BLUE)
                .listener(new SliderPanelListener() {
                    @Override
                    public void onToggle(final boolean expand) {
                        listenerLabel.setValue("event-listener for right-slider: <b>" + (expand ? "expand" : "collapsed") + "</b> by: "
                            + new Date().toString());
                    }
                }).build();
        contentLayout.addComponent(rightSlider);

        // fit full screen
        mainLayout.addComponent(contentLayout);
        mainLayout.setExpandRatio(contentLayout, 1);

        // bottom slider
        SliderPanel bottomSlider =
            new SliderPanelBuilder(dummyContent("Bottom Slider Heading", 5), "Bottom Slider").mode(SliderMode.BOTTOM)
                .tabPosition(SliderTabPosition.END).style(SliderPanelStyles.COLOR_RED).build();
        mainLayout.addComponent(bottomSlider);

        setContent(mainLayout);
    }

    private VerticalLayout dummyContent(final String title, final int length) {
        String text = "";
        for (int x = 0; x <= length; x++) {
            text += LOREM_IPSUM + " ";
        }
        Label htmlDummy = new Label(String.format("<h3>%s</h3>%s", title, text.trim()), ContentMode.HTML);
        VerticalLayout component = new VerticalLayout(htmlDummy);
        component.setExpandRatio(htmlDummy, 1);
        component.addComponent(new Button(title, new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                Notification.show("clicked: " + title, Type.HUMANIZED_MESSAGE);
            }
        }));
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
        productionMode = false,
        widgetset = "org.vaadin.slidersample.WidgetSet")
    public static class MyUIServlet extends VaadinServlet {
    }
}
