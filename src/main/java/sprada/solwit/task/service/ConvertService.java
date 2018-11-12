package sprada.solwit.task.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sprada.solwit.task.dto.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class ConvertService {

    @Autowired
    private ReaderService readerService;
    @Autowired
    private ValidationService validationService;


    public Map<String, String> convertEntryToMap(List<Entry> entries) {
        return entries.stream().collect(toMap(Entry::getKey, Entry::getValue, (first, second) -> second));
    }

    public List<Entry> convertMapToEntryList(Map<String, String> map) {
        return map.entrySet()
                .stream()
                .map(e -> new Entry(e.getKey(), e.getValue()))
                .collect(Collectors.toList());


    }

    public Map<String, String> convertFileToMap(String path) {
        InputStream fis = readerService.readFile(path);
        JsonReader reader = Json.createReader(fis);
        Gson gson = new Gson();
        Map<String, String> map = null;
        try {
            JsonObject result = reader.readObject();
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            map = gson.fromJson(result.toString(), type);
        } catch (JsonParsingException e) {
            List<String> lines = readerService.parseFile(path);
            map = validationService.validateRecords(lines.stream().map(String::trim).collect(toList()));

        }

        reader.close();

        return map;
    }
}
