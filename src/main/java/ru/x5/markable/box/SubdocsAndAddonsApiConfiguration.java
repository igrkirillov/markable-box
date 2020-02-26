package ru.x5.markable.box;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.x5.bo.subdoc8addonsapi.XRGItemsReceiptSubDocAddonService;
import ru.x5.bo.subdoc8addonsapi.XRGItemsReceiptSubDocService;
import ru.x5.bo.subdoc8addonsapi.XRGReclamationSubDocService;

@Configuration
public class SubdocsAndAddonsApiConfiguration {

    @Bean @Qualifier("subdoc&addons")
    public MockInterfacesBeanFactoryPostProcessor beanFactoryPostProcessorSubdocAndAddons() {
        return new MockInterfacesBeanFactoryPostProcessor("ru.x5.bo.subdoc8addonsapi");
    }
}
