package ru.x5.bo.markable;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.x5.bo.subdoc8addonsapi.XRGItemsReceiptSubDocAddonService;
import ru.x5.bo.subdoc8addonsapi.XRGItemsReceiptSubDocService;
import ru.x5.bo.subdoc8addonsapi.XRGReclamationSubDocService;

@Configuration
public class SubdocsAndAddonsConfiguration {
    @Bean
    public XRGItemsReceiptSubDocService xrgItemsReceiptSubdocService() {
        return Mockito.mock(XRGItemsReceiptSubDocService.class);
    }

    @Bean
    public XRGItemsReceiptSubDocAddonService xrgItemsReceiptSubDocAddonService() {
        return Mockito.mock(XRGItemsReceiptSubDocAddonService.class);
    }

    @Bean
    public XRGReclamationSubDocService xrgReclamationSubDocService() {
        return Mockito.mock(XRGReclamationSubDocService.class);
    }
}
