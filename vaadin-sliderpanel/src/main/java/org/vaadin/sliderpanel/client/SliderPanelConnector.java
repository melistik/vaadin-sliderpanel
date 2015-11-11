package org.vaadin.sliderpanel.client;

import org.vaadin.sliderpanel.SliderPanel;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractSingleComponentContainerConnector;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.shared.ui.ComponentStateUtil;
import com.vaadin.shared.ui.Connect;

/**
 * connects SliderPanel with GWT VSliderPanel
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
@Connect(SliderPanel.class)
public class SliderPanelConnector extends AbstractSingleComponentContainerConnector implements SimpleManagedLayout {

    private static final long serialVersionUID = 8211838780745411232L;

    SliderPanelServerRpc rpc = RpcProxy.create(SliderPanelServerRpc.class, this);

    public SliderPanelConnector() {
        super();

        getWidget().setToggleListener(new SliderPanelListener() {

            @Override
            public void onToggle(final boolean expanded) {
                SliderPanelConnector.this.rpc.clicked(expanded);
            }
        });

        registerRpc(SliderPanelClientRpc.class, new SliderPanelClientRpc() {

            private static final long serialVersionUID = -4626540340348038160L;

            @Override
            public void setExpand(final boolean expand, final boolean animated) {
                getWidget().setExpand(expand, animated);
            }

            @Override
            public void scheduleExpand(final boolean expand, final boolean animated, final int delayMillis) {
                getWidget().scheduleExpand(expand, animated, delayMillis);
            }
        });

    }

    @Override
    public SliderPanelState getState() {
        return (SliderPanelState) super.getState();
    }

    @Override
    public VSliderPanel getWidget() {
        return (VSliderPanel) super.getWidget();
    }

    @Override
    public void updateCaption(final ComponentConnector connector) {
    }

    @Override
    public void onConnectorHierarchyChange(final ConnectorHierarchyChangeEvent event) {
        getWidget().setWidget(getContentWidget());
    }

    @Override
    public void onStateChanged(final StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().configure(getState().mode, getState().flowInContent, getState().tabPosition, getState().pixel);

        if (stateChangeEvent.hasPropertyChanged("animationDuration")) {
            getWidget().setAnimationDuration(getState().animationDuration);
        }
        if (stateChangeEvent.hasPropertyChanged("caption")) {
            getWidget().setCaption(getState().caption, false);
        }
        if (stateChangeEvent.hasPropertyChanged("pixel")) {
            getWidget().setFixedContentSize(getState().pixel);
        }
        if (stateChangeEvent.hasPropertyChanged("autoCollapseSlider")) {
            getWidget().setAutoCollapseSlider(getState().autoCollapseSlider);
        }

        if (ComponentStateUtil.hasStyles(getState())) {
            String extraStyles = "";
            for (String style : getState().styles) {
                extraStyles += " " + style;
            }
            getWidget().setStyles(extraStyles);
        }
    }

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    public void layout() {
        // in case onStateChanged is not fired before
        getWidget().configure(getState().mode, getState().flowInContent, getState().tabPosition, getState().pixel);
        getWidget().initialize(getState().expand, getState().tabSize);
    }

}
