package org.vaadin.sliderpanel.demo;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.SliderPanelStyles;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;
import org.vaadin.sliderpanel.demo.data.DummyDataGen;
import org.vaadin.sliderpanel.demo.data.Inhabitants;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI()
@StyleSheet("demo.css")
@Widgetset("org.vaadin.sliderpanel.demo.WidgetSet")
public class DemoUI extends UI {

    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet,consetetur sadipscing elitr, "
            + "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.";

    private SliderPanel secondTopSlider;

    @Value("${build.version}")
    private String buildVersion;

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addStyleName("main");
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        // top slider
        final Grid grid = genGrid();
        final VerticalLayout topLeftSliderContent = new VerticalLayout(new Label("Grid works now!"), grid);
        topLeftSliderContent.setMargin(true);
        topLeftSliderContent.setSpacing(true);
        topLeftSliderContent.setExpandRatio(grid, 1);

        final SliderPanel firstTopSlider = new SliderPanelBuilder(topLeftSliderContent, "First Top with Grid").tabPosition(SliderTabPosition.END)
            .style(SliderPanelStyles.COLOR_WHITE)
            .flowInContent(true)
            .build();

        this.secondTopSlider = new SliderPanelBuilder(dummyContent("Second Top Slider Heading", 1), "Second Top fixedContentSize (flowIn)")
            .tabPosition(SliderTabPosition.BEGINNING)
            .style(SliderPanelStyles.COLOR_GREEN)
            .flowInContent(true)
            .fixedContentSize(Page.getCurrent()
                .getBrowserWindowHeight() - 100)
            .build();

        // Two top slider
        final HorizontalLayout topTwoSliderLayout = new HorizontalLayout();
        topTwoSliderLayout.setWidth(100, Unit.PERCENTAGE);
        topTwoSliderLayout.setHeight(0, Unit.PIXELS);
        topTwoSliderLayout.setMargin(false);
        topTwoSliderLayout.setSpacing(false);
        topTwoSliderLayout.addComponent(firstTopSlider);
        topTwoSliderLayout.setExpandRatio(firstTopSlider, 1);
        topTwoSliderLayout.addComponent(this.secondTopSlider);
        topTwoSliderLayout.setExpandRatio(this.secondTopSlider, 1);
        mainLayout.addComponent(topTwoSliderLayout);

        // center layout with left and right slider
        final HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSpacing(false);
        contentLayout.setSizeFull();

        // left slider shoudn't get displayed over this element
        final VerticalLayout paddingLeftComp = new VerticalLayout();
        paddingLeftComp.addStyleName(SliderPanelStyles.COLOR_GRAY);
        paddingLeftComp.setWidth("200px");
        paddingLeftComp.setHeight("100%");
        paddingLeftComp.setMargin(true);

        final Label leftPaddingContent = new Label(
                "<h3>Left Padding</h3>" + "<p><b>Wokring with Grid</b> since version 1.4.2</p>" + "<p>compatible with <b>Vaadin 8</b></p>", ContentMode.HTML);
        leftPaddingContent.setSizeFull();
        paddingLeftComp.addComponentsAndExpand(leftPaddingContent);

        contentLayout.addComponent(paddingLeftComp);

        // left slider
        final VerticalLayout leftDummyContent = dummyContent("Left Slider Heading", 3);
        leftDummyContent.setWidth(400, Unit.PIXELS);
        final SliderPanel leftSlider = new SliderPanelBuilder(leftDummyContent, "Left slow Slider").mode(SliderMode.LEFT)
            .tabPosition(SliderTabPosition.BEGINNING)
            .animationDuration(2000)
            .zIndex(9980)
            .build();
        contentLayout.addComponent(leftSlider);

        // dummy middle content
        final VerticalLayout contentLabel = dummyContent("Main Content", 4);
        contentLabel.setMargin(true);
        contentLabel.setSizeFull();

        contentLabel.addComponent(new Button("schedule toggle first-top-slider", new Button.ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                Notification.show("start schedule toggle on first-top-slider in 2 secs", Type.TRAY_NOTIFICATION);
                firstTopSlider.scheduleToggle(2000);
            }
        }));

        final CheckBox enabledCheckBox = new CheckBox("First top slider enabled");
        enabledCheckBox.setValue(firstTopSlider.isEnabled());
        enabledCheckBox.addValueChangeListener(event -> firstTopSlider.setEnabled(event.getValue()));

        contentLabel.addComponent(enabledCheckBox);

        final Label listenerLabel = new Label("event-listener for right-slider", ContentMode.HTML);
        listenerLabel.setWidth(100, Unit.PERCENTAGE);
        listenerLabel.setHeight(45, Unit.PIXELS);
        contentLabel.addComponent(listenerLabel);

        contentLayout.addComponent(contentLabel);
        contentLayout.setComponentAlignment(contentLabel, Alignment.MIDDLE_CENTER);
        contentLayout.setExpandRatio(contentLabel, 1);

        // right slider
        final VerticalLayout rightDummyContent = dummyContent("Right Slider Heading", 1);
        rightDummyContent.setWidth(400, Unit.PIXELS);

        final ComboBox simpleCombo = new ComboBox("Combo");
        simpleCombo.setItems(SliderMode.values());
        rightDummyContent.addComponent(simpleCombo);
        rightDummyContent.addComponent(new Label("vaadin's combo creates an inner popup that is also get catched by autoCollapse detection"));
        rightDummyContent.addComponent(genGrid());

        final SliderPanel rightSlider = new SliderPanelBuilder(rightDummyContent, "Right Slider + Grid (autoCollapse)").mode(SliderMode.RIGHT)
            .tabPosition(SliderTabPosition.MIDDLE)
            .flowInContent(true)
            .autoCollapseSlider(true)
            .zIndex(9980)
            .style(SliderPanelStyles.COLOR_BLUE)
            .listener(e -> {
                listenerLabel.setValue("event-listener for right-slider: <b>" + (e.isExpand() ? "expand" : "collapsed") + "</b> by: " + new Date().toString());
            })
            .build();
        contentLayout.addComponent(rightSlider);

        // fit full screen
        mainLayout.addComponent(contentLayout);
        mainLayout.setExpandRatio(contentLayout, 1);

        // bottom slider
        // i've created a custom sliderpanel style within demo.scss with different labelwidth
        final SliderPanel bottomSlider = new SliderPanelBuilder(dummyContent("Bottom Slider Heading", 5), "Bottom Custom-Style").mode(SliderMode.BOTTOM)
            .autoCollapseSlider(true)
            .flowInContent(true)
            .tabSize(80)
            .tabPosition(SliderTabPosition.END)
            .style("my-sliderpanel", SliderPanelStyles.COLOR_RED)
            .build();
        mainLayout.addComponent(bottomSlider);

        final HorizontalLayout wrapper = new HorizontalLayout(mainLayout, genInfo());
        wrapper.setSizeFull();
        wrapper.setExpandRatio(mainLayout, 1);
        wrapper.setSpacing(false);

        setContent(wrapper);

        Page.getCurrent()
            .setTitle("SliderPanel Sample");
        Page.getCurrent()
            .addBrowserWindowResizeListener(new BrowserWindowResizeListener() {

                @Override
                public void browserWindowResized(final BrowserWindowResizeEvent event) {
                    DemoUI.this.secondTopSlider.setFixedContentSize(Page.getCurrent()
                        .getBrowserWindowHeight() - 100);
                }
            });
    }

    private Grid genGrid() {
        // init Grid
        final Grid<Inhabitants> grid = new Grid(Inhabitants.class);
        grid.setSizeFull();

        // init Container
        grid.setItems(DummyDataGen.genInhabitants(100));
        grid.setColumnOrder("id", "gender", "name", "bodySize", "birthday", "onFacebook");

        return grid;
    }

    private Component genInfo() {
        final VerticalLayout info = new VerticalLayout();
        info.setMargin(true);
        info.setWidth("320px");
        info.setHeight("100%");
        info.addStyleName("black-bg");

        final Label details = new Label(
                "<h3>Vaadin SliderPanel</h3>" + "<p>Developed by Marten Prieß<br/>" + "<a href=\"http://www.rocketbase.io\">www.rocketbase.io</a><p>"
                        + "<p>Sample & Sourcecode:<br/><a href=\"https://github.com/melistik/vaadin-sliderpanel/\">github.com</a><br/>"
                        + "Vaadin Addon-Site:<br/><a href=\"https://vaadin.com/directory#!addon/sliderpanel\">vaadin.com</a></p>",
                ContentMode.HTML);
        details.setSizeFull();
        info.addComponentsAndExpand(details);
        info.addComponent(new Label("<p class=\"version\">Version: " + this.buildVersion + "</p>", ContentMode.HTML));

        return info;
    }

    private VerticalLayout dummyContent(final String title, final int length) {
        String text = "";
        for (int x = 0; x <= length; x++) {
            text += LOREM_IPSUM + " ";
        }
        final Label htmlDummy = new Label(String.format("<h3>%s</h3>%s", title, text.trim()), ContentMode.HTML);
        final VerticalLayout component = new VerticalLayout(htmlDummy);
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
}
