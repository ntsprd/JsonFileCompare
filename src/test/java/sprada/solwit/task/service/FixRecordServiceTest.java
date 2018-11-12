package sprada.solwit.task.service;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class FixRecordServiceTest {

    FixRecordService fixRecordService = new FixRecordService();

    @Test
    public void fixRecordNotEndsWithComma() {
        String invalidLine= "\"crossword_property_rows\": \"Renglones\",zxcv";
        Optional<String> validLine = fixRecordService.fixRecord(invalidLine);

        assertEquals("\"crossword_property_rows\": \"Renglones\",", validLine.get());

    }


    @Test
    public void fixRecordNotStartsWithQuote() {
        String invalidLine= "_crossword_property_rows\": \"Renglones\",";
        Optional<String> validLine = fixRecordService.fixRecord(invalidLine);

        assertEquals("\"crossword_property_rows\": \"Renglones\",", validLine.get());

    }
}