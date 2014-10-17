package ru.dev_server.client.dataloader;

import org.springframework.beans.factory.InitializingBean;

/**.*/
public class DataloaderInit implements InitializingBean{
    private Dataloader dataloader;

    @Override
    public void afterPropertiesSet() throws Exception {
        dataloader.fillDatabase();
    }

    public Dataloader getDataloader() {
        return dataloader;
    }

    public void setDataloader(Dataloader dataloader) {
        this.dataloader = dataloader;
    }
}
