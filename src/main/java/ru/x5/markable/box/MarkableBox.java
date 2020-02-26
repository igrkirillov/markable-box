package ru.x5.markable.box;

import org.springframework.boot.SpringApplication;

public class MarkableBox {

    private Class<?> markapiImplConfig;

    public MarkableBox markapiImplConfig(Class<?> markapiImplConfig) {
        this.markapiImplConfig = markapiImplConfig;
        return this;
    }

    public void start(String[] args) {
        SpringApplication.run(new Class[]{
                BoApiConfiguration.class,
                ReferenceApiConfiguration.class,
                SubdocsAndAddonsApiConfiguration.class,
                markapiImplConfig
        }, args);
    }
}
