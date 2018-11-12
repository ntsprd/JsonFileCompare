package sprada.solwit.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DifferencesDto {
    List<Entry> onlyEnList;
    List<Entry> notTranslatedList;
    List<Entry> esList;
}
