package ru.dev_server.client.compoment;

import org.zkoss.zk.ui.HtmlMacroComponent;
import ru.dev_server.client.model.ClientDynamicValue;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.DynamicValue;

import java.util.List;
import java.util.Map;

/**.*/
public class DynamicColumnEditor  extends HtmlMacroComponent {
    private List<DynamicColumn> dynamicColumnList;
    private Map<DynamicColumn,? extends DynamicValue> dynamicValueList;

    public DynamicColumnEditor() {
        setMacroURI("/macro/dynamicColumnEditor.zul");
    }

    public List<DynamicColumn> getDynamicColumnList() {
        return dynamicColumnList;
    }

    public void setDynamicColumnList(List<DynamicColumn> dynamicColumnList) {
        this.dynamicColumnList = dynamicColumnList;
    }

    public Map<DynamicColumn, ? extends DynamicValue> getDynamicValueList() {
        return dynamicValueList;
    }

    public void setDynamicValueList(Map<DynamicColumn, ? extends DynamicValue> dynamicValueList) {
        this.dynamicValueList = dynamicValueList;
    }
}
