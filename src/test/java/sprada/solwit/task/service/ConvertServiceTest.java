package sprada.solwit.task.service;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sprada.solwit.task.dto.Entry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConvertServiceTest {

    @Mock
    private ValidationService validationService;
    @Mock
    private ReaderService readerService;

    @InjectMocks
    private ConvertService convertService;

    private File file;


    @Before
    public void setUp(){
        URL url = Thread.currentThread().getContextClassLoader().getResource("test/TestJsonFile.json");
        file = new File(url.getPath());
    }

    @Test
    public void convertEntryToMap() {
        Entry firstEntry = new Entry("keyFirst", "valueFirst");
        Entry secondEntry = new Entry("keySecond", "valueSecond");
        Entry thirdEntry = new Entry("keyThird", "valueThird");

        List<Entry> entryList = Arrays.asList(firstEntry, secondEntry, thirdEntry);

        Map<String, String> result = convertService.convertEntryToMap(entryList);

        assertEquals(3, result.size());
        assertEquals("valueFirst", result.get("keyFirst"));
        assertEquals("valueSecond", result.get("keySecond"));
        assertEquals("valueThird", result.get("keyThird"));


    }

    @Test
    public void convertMapToEntryList() {
        Map<String, String> map = new HashMap<>();
        map.put("keyFirst", "valueFirst");
        map.put("keySecond", "valueSecond");
        map.put("keyThird", "valueThird");


        List<Entry> result = convertService.convertMapToEntryList(map)
                .stream()
                .sorted(Comparator.comparing(i -> i.getKey()))
                .collect(toList());


        assertEquals(3, result.size());
        assertEquals("valueFirst", result.get(0).getValue());
        assertEquals("keySecond", result.get(1).getKey());

    }

    @Test
    public void convertFileToMapValidFile() throws IOException {

        String text = "{\n" +
                "  \"orientation_type_vertical\": \"vertical\",\n" +
                "  \"orientation_type_horizontal\": \"horizontal\",\n" +
                "  \"semi_responsive_properties_label\": \"Layout\",\n" +
                "  \"semi_responsive_layout_panel_select_layout_first\": \"You have to select a semi responsive layout.\",\n" +
                "  \"semi_responsive_layout_panel_select_css_style_first\": \"You have to select a CSS style.\"\n" +
                "}";

        InputStream stubInputStream =  IOUtils.toInputStream(text, "UTF-8");

        when(readerService.readFile(file.getPath())).thenReturn(stubInputStream);

        Map<String, String> result = convertService.convertFileToMap(file.getPath());

        assertEquals(5, result.size());
        assertEquals("horizontal", result.get("orientation_type_horizontal"));
        assertEquals("vertical", result.get("orientation_type_vertical"));
        assertEquals("You have to select a CSS style.", result.get("semi_responsive_layout_panel_select_css_style_first"));


    }

    @Test
    public void convertFileToMapInvalidFile() throws IOException {

        String text = "{\n" +
                "  _orientation_type_vertical\": \"vertical\",\n" +
                "  \"orientation_type_horizontal\": \"horizontal\",DODATKOWYTEKST\n" +
                "  \"semi_responsive_properties_label\": \"Layout\",\n" +
                "  _semi_responsive_layout_panel_select_layout_first\": \"You have to select a semi responsive layout.\",DODATKOWYTEKST\n" +
                "  \"semi_responsive_layout_panel_select_css_style_first\": \"You have to select a CSS style.\"\n" +
                "}";

        InputStream stubInputStream =  IOUtils.toInputStream(text, "UTF-8");

        when(readerService.readFile(file.getPath())).thenReturn(stubInputStream);

        String first =  "  _orientation_type_vertical\": \"vertical\",\n";
        String second = "  \"orientation_type_horizontal\": \"horizontal\",DODATKOWYTEKST\n";
        String third =  "  \"semi_responsive_properties_label\": \"Layout\",\n";
        String fourth = "_semi_responsive_layout_panel_select_layout_first\": \"You have to select a semi responsive layout.\",DODATKOWYTEKST\n";
        String fifth = "  \"semi_responsive_layout_panel_select_css_style_first\": \"You have to select a CSS style.\"\n";

        List<String> stringList = Arrays.asList(first, second, third, fourth, fifth);
        when(readerService.parseFile(file.getPath())).thenReturn(stringList);

        Map<String, String> map = new HashMap<>();
        map.put("orientation_type_vertical","vertical");
        map.put("orientation_type_horizontal","horizontal");
        map.put("semi_responsive_properties_label","Layout");
        map.put("semi_responsive_layout_panel_select_layout_first","You have to select a semi responsive layout.");
        map.put("semi_responsive_layout_panel_select_css_style_first","You have to select a CSS style.");

        when(validationService.validateRecords(anyList())).thenReturn(map);



        Map<String, String> result = convertService.convertFileToMap(file.getPath());

        assertEquals(5, result.size());
        assertEquals("horizontal", result.get("orientation_type_horizontal"));
        assertEquals("vertical", result.get("orientation_type_vertical"));
        assertEquals("You have to select a CSS style.", result.get("semi_responsive_layout_panel_select_css_style_first"));


    }
}