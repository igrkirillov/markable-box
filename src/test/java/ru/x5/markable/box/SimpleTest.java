package ru.x5.markable.box;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.x5.bo.markable.config.MarkableSpringConfiguration;

import static org.junit.Assert.assertEquals;

public class SimpleTest {

    @Test
    public void startup() {
        MarkableBox box = new MarkableBox()
                .markapiImplConfig(MarkableSpringConfiguration.class);
        box.start(new String[]{});

        BORestTemplate rt = new BORestTemplate();
        ResponseEntity<String> response = rt.getForEntity("http://localhost:8080/markable/main/greeting", String.class);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
