package sprada.solwit.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class ValidationService {

    private static final String VALID_ROW = "^(\\s+)?(\"(.+)?\")(\\s+)?:(\\s+)?(\"(.+)?\")(\\s+)?(,)?$";
    private static final String EMPTY = "";
    private static final String COMMA = ",";
    private static final String QUOTE = "\"";
    private static final String SEMICOLON = ":";
    private static final String JSON_START = "^\\{(.*)?$";
    private static final String JSON_END = "^\\}(.*)?$";

    @Autowired
    private FixRecordService fixer;


    public Map<String, String> validateRecords(List<String> lines) {
        Map<String, String> entries = new HashMap<>();
        lines.forEach(i -> parseLine(entries, i));
        return entries;
    }

    private void parseLine(Map<String, String> entries, String line) {
        processInvalidRecord(line).ifPresent(i -> addToMap(entries, i));
        processValidRecord(line).ifPresent(i -> addToMap(entries, i));

    }

    private Optional<String> processInvalidRecord(String line) {
        return Optional.of(line)
                .filter(invalidRecord)
                .map(fixer::fixRecord)
                .flatMap(i -> i);
    }

    private Optional<String> processValidRecord(String line) {
        return Optional.of(line)
                .filter(validRecord);
    }

    private void addToMap(Map<String, String> entries, String line) {
        String[] elements = line.split(SEMICOLON);
        String key = elements[0].trim().replaceAll(QUOTE, EMPTY);
        String value = elements[1].trim().replaceAll(QUOTE, EMPTY).replace(COMMA, EMPTY);
        entries.put(key, value);
    }

    private Predicate<String> invalidRecord = line ->
            !line.matches(JSON_START) && !line.matches(JSON_END) && !line.matches(VALID_ROW);

    private Predicate<String> validRecord = line -> line.matches(VALID_ROW);
}
