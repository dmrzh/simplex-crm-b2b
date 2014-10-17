package ru.dev_server.client.compoment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class UrlEditor extends HtmlMacroComponent {
    private static final Logger LOG= LoggerFactory.getLogger(UrlEditor.class);

    private String url;

    @Wire("window#urlEditWin")
    private Window window;

    @Wire("window textbox#urlTextbox")
    Textbox textbox;

    @Wire("#link")
    A link;

    @Wire("#linkText")
    Label linkText;

    public UrlEditor() {
        setMacroURI("/macro/url-editor.zul");
    }

    @Override
    public void afterCompose() {
        super.afterCompose();
        textbox.setValue(url);
    }

    @Listen("onClick=button#changeBtn")
    public void onChange(){
        window.setVisible(true);
    }

    @Listen("onClick=window button#saveBtn")
    @NotifyChange("self.spaceOwner.url")
    public void onSave(){
        window.setVisible(false);
        recreate();
        Events.postEvent("onEdited", this, url);
    }

    @Listen("onClick=window button#cancelBtn")
    public void onCancel(){
        window.setVisible(false);
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
