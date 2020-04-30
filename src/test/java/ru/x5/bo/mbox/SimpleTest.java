package ru.x5.bo.mbox;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.x5.bo.markable.config.MarkableSpringConfiguration;

import static org.junit.Assert.assertEquals;

public class SimpleTest {

    /**
     * Игнорим, чтобы не было обратной петли на табак.
     * Удобно тест запускать локально.
     */
    @Ignore
    @Test
    public void startup() {
        new MarkableBox()
                .markapiImplConfig(MarkableSpringConfiguration.class)
                .serverPort(9988)
                .start();

        BORestTemplate rt = new BORestTemplate();
        ResponseEntity<String> response = rt.getForEntity("http://localhost:9988/markable/main/greeting", String.class);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
