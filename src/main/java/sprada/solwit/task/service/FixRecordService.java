package sprada.solwit.task.service;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Predicate;

@Service
public class FixRecordService {


    private static final String COMMA = ",";
    private static final String QUOTE = "\"";
    private static final int ZERO = 0;

    public Optional<String> fixRecord(String line) {
        Optional<String> result = Optional.empty();

        if (notEndsWithComma.test(line)) {
            result = Optional.of(line)
                    .map(String::trim)
                    .map(i -> i.substring(ZERO, line.lastIndexOf(COMMA) + 1));
        }

        if (notStartsWithQuote.test(result.orElse(line))) {
            String trimmed = result.orElse(line).trim();
            return Optional.of(QUOTE + trimmed.substring(1));
        }
        return result;
    }


    private Predicate<String> notEndsWithComma = line -> !line.endsWith(COMMA);
    private Predicate<String> notStartsWithQuote = line -> !line.startsWith(QUOTE);


}
