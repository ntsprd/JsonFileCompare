package sprada.solwit.task.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import sprada.solwit.task.dto.Entry;
import sprada.solwit.task.dto.DifferencesDto;
import sprada.solwit.task.service.CompareService;
import sprada.solwit.task.service.ConvertService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class ExportController {

    @Autowired
    private ConvertService convertService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(5000);
    }


    @PostMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, params="action=exportToFile")
    public ResponseEntity getFile(@ModelAttribute("DifferencesDto") DifferencesDto dto, BindingResult result, HttpServletResponse response) throws IOException {

        File file = File.createTempFile("tmp", ".json");
        List<Entry> all = new ArrayList<>();
        all.addAll(dto.getEsList());
        all.addAll(dto.getOnlyEnList());
        all.addAll(dto.getNotTranslatedList());

        Map<String, String> map = convertService.convertEntryToMap(all);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(map);

        Files.write(Paths.get(file.getAbsolutePath()), json.getBytes());


        try {
            InputStream is = new FileInputStream(file);

            String mimeType = "application/octet-stream";
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=" + "FixJson.json");
            responseHeaders.add("Content-Type",mimeType);


            byte[]out=org.apache.commons.io.IOUtils.toByteArray(is);

            return new ResponseEntity(out, responseHeaders,HttpStatus.OK);


        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }

    }

    @PostMapping(value = "/export", params="action=exportAsText", produces="application/json;charset=UTF-8")
    public void getJsonAsText(@ModelAttribute("DifferencesDto") DifferencesDto dto, BindingResult result, HttpServletResponse response) throws IOException {

        File file = File.createTempFile("tmp", ".json");
        List<Entry> all = new ArrayList<>();
        all.addAll(dto.getEsList());
        all.addAll(dto.getOnlyEnList());
        all.addAll(dto.getNotTranslatedList());

        Map<String, String> map = convertService.convertEntryToMap(all);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(map);

        Files.write(Paths.get(file.getAbsolutePath()), json.getBytes("UTF8"));


        try {
            InputStream is = new FileInputStream(file);

            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.setCharacterEncoding("UTF8"); // this line solves the problem
            response.setContentType("application/json");
            response.flushBuffer();
            response.setCharacterEncoding("UTF8"); // this line solves the problem
            response.setContentType("application/json");


        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }

    }
}
