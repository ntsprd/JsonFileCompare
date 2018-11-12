package sprada.solwit.task.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileFormDto {
    MultipartFile fileEs;
    MultipartFile fileEn;
}
