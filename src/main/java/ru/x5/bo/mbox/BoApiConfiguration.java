package ru.x5.bo.mbox;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoApiConfiguration {

    @Bean @Qualifier("boapi")
    public MockInterfacesBeanFactoryPostProcessor beanFactoryPostProcessorSubdocAndAddons() {
        return new MockInterfacesBeanFactoryPostProcessor("ru.x5.bo.boapi");
    }
}
