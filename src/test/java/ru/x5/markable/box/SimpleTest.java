package ru.x5.markable.box;

import org.junit.Test;
import ru.x5.bo.markable.config.MarkableSpringConfiguration;

public class SimpleTest {

    @Test
    public void startup() throws InterruptedException {
        MarkableBox box = new MarkableBox()
                .markapiImplConfig(MarkableSpringConfiguration.class);
        box.start(new String[]{});
        Thread.sleep(100000);
    }
}
