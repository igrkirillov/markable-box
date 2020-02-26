package ru.x5.markable.box;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.HashMap;
import java.util.Map;

public class MarkableBox {

    private Class<?> markapiImplConfig;
    private int serverPort = 9999;

    public MarkableBox markapiImplConfig(Class<?> markapiImplConfig) {
        this.markapiImplConfig = markapiImplConfig;
        return this;
    }

    public MarkableBox serverPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public void start() {
        start(new String[]{});
    }
    public void start(String[] args) {
        Map<String, Object> props = new HashMap<>();
        props.put("server.port", serverPort);

        new SpringApplicationBuilder(
                new Class[]{
                        BoApiConfiguration.class,
                        ReferenceApiConfiguration.class,
                        SubdocsAndAddonsApiConfiguration.class,
                        markapiImplConfig})
                .properties(props)
                .run(args);
    }
}
