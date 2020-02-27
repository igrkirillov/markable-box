package org.x5.bo.mbox;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubdocsAndAddonsApiConfiguration {

    @Bean @Qualifier("subdoc&addons")
    public MockInterfacesBeanFactoryPostProcessor beanFactoryPostProcessorSubdocAndAddons() {
        return new MockInterfacesBeanFactoryPostProcessor("ru.x5.bo.subdoc8addonsapi");
    }
}
