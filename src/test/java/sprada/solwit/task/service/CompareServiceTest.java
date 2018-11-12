package sprada.solwit.task.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sprada.solwit.task.dto.DifferencesDto;
import sprada.solwit.task.dto.Entry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompareServiceTest {

    @Mock
    private ConvertService convertService;

    @InjectMocks
    private CompareService compareService;

    @Test
    public void compareJson() {
        Map<String, String> mapEs = new HashMap<>();
        mapEs.put("onlyEs", "onlyEsValue");
        mapEs.put("inBoth", "inBothValue");
        mapEs.put("translated", "TranslatedEs");

        when(convertService.convertFileToMap("ES")).thenReturn(mapEs);

        Map<String, String> mapEn = new HashMap<>();
        mapEn.put("onlyEn", "onlyEnValue");
        mapEn.put("inBoth", "inBothValue");
        mapEn.put("translated", "TranslatedEn");

        when(convertService.convertFileToMap("EN")).thenReturn(mapEn);

        Map<String, String> onlyEn = new HashMap<>();
        onlyEn.put("onlyEn", "onlyEnValue");

        Map<String, String> notTranslated = new HashMap<>();
        notTranslated.put("inBoth", "inBothValue");

        Map<String, String> mapEsAfterRemove= new HashMap<>();
        mapEsAfterRemove.put("inBoth", "inBothValue");
        mapEsAfterRemove.put("translated", "TranslatedEs");

        List<Entry> entryList = Arrays.asList(new Entry("inBoth", "inBothValue"), new Entry("translated", "TranslatedEs"));


        when(convertService.convertMapToEntryList(onlyEn)).thenReturn(Arrays.asList(new Entry("onlyEn", "onlyEnValue")));
        when(convertService.convertMapToEntryList(notTranslated)).thenReturn(Arrays.asList(new Entry("inBoth", "inBothValue")));
        when(convertService.convertMapToEntryList(mapEsAfterRemove)).thenReturn(entryList);

        DifferencesDto result = compareService.compareJson("ES", "EN");

        assertEquals("onlyEn", result.getOnlyEnList().get(0).getKey());
        assertEquals(2, result.getEsList().size());
        assertEquals("inBothValue", result.getNotTranslatedList().get(0).getValue());
        assertThat(entryList, is(result.getEsList()));





    }
}