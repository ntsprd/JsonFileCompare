package sprada.solwit.task.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sprada.solwit.task.dto.DifferencesDto;
import sprada.solwit.task.dto.FileFormDto;
import sprada.solwit.task.service.CompareService;

import java.io.*;


@Controller
public class FormController {

    @Autowired
    private CompareService compareService;




    @GetMapping("/home")
    public String getHome(Model model) {
        model.addAttribute("FileFormDto", new FileFormDto());
        model.addAttribute("error", false);
        return "home";
    }


    @PostMapping("/CompareFile")
    public String createTable(@ModelAttribute("FileFormDto") FileFormDto dto, Model model) throws IOException {
        if(StringUtils.isNotEmpty(dto.getFileEn().getOriginalFilename()) && StringUtils.isNotEmpty(dto.getFileEs().getOriginalFilename())){
            File esFile = File.createTempFile("EStmp", ".json");
            File enFile = File.createTempFile("ENtmp", ".json");
            dto.getFileEs().transferTo(esFile);
            dto.getFileEn().transferTo(enFile);
            DifferencesDto result = compareService.compareJson(esFile.getAbsolutePath(), enFile.getAbsolutePath());
            esFile.delete();
            enFile.delete();
            model.addAttribute("DifferencesDto", result);
            return  "form";
        }

        model.addAttribute("FileFormDto", new FileFormDto());
        model.addAttribute("error", true);
        return "home";
    }



}


