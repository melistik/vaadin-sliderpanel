package org.vaadin.sliderpanel.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * is been called from GWT connector
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public interface SliderPanelServerRpc extends ServerRpc {

    void clicked(boolean visible);
}
