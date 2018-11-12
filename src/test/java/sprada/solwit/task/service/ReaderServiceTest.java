package sprada.solwit.task.service;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class ReaderServiceTest {

    private File file;

    ReaderService readerService = new ReaderService();

    @Before
    public void setUp(){
        URL url = Thread.currentThread().getContextClassLoader().getResource("test/TestJsonFile.json");
        file = new File(url.getPath());
    }


    @Test
    public void parseFile() {
        List<String> result = readerService.parseFile(file.getPath());

        assertNotNull(result);
        assertEquals(7, result.size());
        assertEquals("  \"semi_responsive_properties_label\": \"Layout\",", result.get(3));
    }

    @Test
    public void readFile() {
        InputStream result = readerService.readFile(file.getPath());

        assertNotNull(result);

    }
}