package ru.x5.bo.markable;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.x5.bo.referenceapi.XRGGkStoreDataReference;
import ru.x5.bo.referenceapi.XRGItemIdentitiesReference;

@Configuration
public class ReferenceConfiguration {
    @Bean
    public XRGItemIdentitiesReference xRGItemIdentitiesReference() {
        return Mockito.mock(XRGItemIdentitiesReference.class);
    }

    @Bean
    public XRGGkStoreDataReference xrgGkStoreDataReference() {
        return Mockito.mock(XRGGkStoreDataReference.class);
    }
}
