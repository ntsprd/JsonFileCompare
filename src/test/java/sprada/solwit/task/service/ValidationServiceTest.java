package sprada.solwit.task.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationServiceTest {

    @Mock private FixRecordService fixRecordService;

    @InjectMocks
    ValidationService validationService;

    @Test
    public void validateRecords() {
        String firstLine= "\"crossword_property_rows\": \"Renglones\",zxcv";
        String secondLine= "\"feedback_property_default_response\": \"Default response\",";
        String thirdLine= "_feedback_property_negative\": \"Incorrect answer\",";

        String firstLineValid= "\"crossword_property_rows\": \"Renglones\",";
        String thirdLineValid= "\"feedback_property_negative\": \"Incorrect answer\",";

        List<String> lines =  Arrays.asList(firstLine, secondLine, thirdLine);

        when(fixRecordService.fixRecord(lines.get(0))).thenReturn(Optional.of(firstLineValid));
        when(fixRecordService.fixRecord(lines.get(2))).thenReturn(Optional.of(thirdLineValid));


        Map<String, String> result = validationService.validateRecords(lines);

        assertEquals(3, result.size());
        assertEquals("Default response", result.get("feedback_property_default_response"));
        assertEquals("Incorrect answer", result.get("feedback_property_negative"));
        assertEquals("Renglones", result.get("crossword_property_rows"));


    }
}