package org.vaadin.sliderpanel.client;

import org.vaadin.sliderpanel.SliderPanel;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractSingleComponentContainerConnector;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.shared.ui.Connect;

/**
 * connects SliderPanel with GWT VSliderPanel
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
@Connect(SliderPanel.class)
public class SliderPanelConnector extends AbstractSingleComponentContainerConnector implements SimpleManagedLayout {

	SliderPanelServerRpc rpc = RpcProxy.create(SliderPanelServerRpc.class, this);

	public SliderPanelConnector() {
		super();

		getWidget().setToggleListener(new SliderPanelListener() {

			@Override
			public void onToggle(final boolean expanded) {
				SliderPanelConnector.this.rpc.clicked(expanded);
				getState().expand = expanded;
			}
		});

		registerRpc(SliderPanelClientRpc.class, new SliderPanelClientRpc() {
			@Override
			public void setExpand(final boolean expand, final boolean animated) {
				getWidget().setExpand(expand, animated);
				getState().expand = expand;
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
		getWidget().setAnimationDuration(getState().animationDuration);
		getWidget().setCaption(getState().caption, getState().captionAsHtml);
		getWidget().setMode(getState().mode);
		getWidget().setTabPosition(getState().tabPosition);
	}

	@Override
	public boolean delegateCaptionHandling() {
		return false;
	}

	@Override
	public void layout() {
		getWidget().initialize(getState().expand);
	}

}
