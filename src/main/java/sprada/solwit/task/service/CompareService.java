package sprada.solwit.task.service;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sprada.solwit.task.dto.DifferencesDto;
import sprada.solwit.task.dto.Entry;

import java.util.List;
import java.util.Map;

@Service
public class CompareService {

    @Autowired
    private ConvertService convertService;

    public DifferencesDto compareJson(String pathEs, String pathEn) {

        Map<String, String> leftMapEs = convertService.convertFileToMap(pathEs);
        Map<String, String> rightMapEn = convertService.convertFileToMap(pathEn);

        MapDifference<String, String> difference = Maps.difference(leftMapEs, rightMapEn);

        Map<String, String> onlyEs = difference.entriesOnlyOnLeft();
        if (!onlyEs.isEmpty())
            removeOnlyEs(onlyEs, leftMapEs);

        List<Entry> onlyEn = convertService.convertMapToEntryList(difference.entriesOnlyOnRight());

        List<Entry> notTranslated = convertService.convertMapToEntryList(difference.entriesInCommon());

        return new DifferencesDto(onlyEn, notTranslated, convertService.convertMapToEntryList(leftMapEs));

    }


    private void removeOnlyEs(Map<String, String> onlyEsMap, Map<String, String> esInputMap) {
        esInputMap.keySet().removeAll(onlyEsMap.keySet());
    }


}
