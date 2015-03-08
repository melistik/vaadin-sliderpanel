package org.vaadin.sliderpanel.client;

import com.vaadin.shared.communication.ClientRpc;

/**
 * is been called from ServerComponent
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public interface SliderPanelClientRpc extends ClientRpc {

    void setExpand(boolean expand, boolean animated);

}
