package ru.dev_server.client.viewmodel.italtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.dao.itevents.ItNoteDAO;
import ru.dev_server.client.model.ItNote;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ItNoteVM {
    private static final Logger LOG= LoggerFactory.getLogger(ItNoteVM.class);

    private ItNote itNote;

    @WireVariable
    private ItNoteDAO itNoteDAO;


    private Window itNoteWin;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("itNote") ItNote itNote){
        this.itNote=itNote;
        this.itNoteWin=view;
    }

    @Command
    public void save(){
        itNoteDAO.saveOrUpdate(itNote);
        itNoteWin.detach();
    }

    public ItNote getItNote() {
        return itNote;
    }

    public void setItNote(ItNote itNote) {
        this.itNote = itNote;
    }
}
