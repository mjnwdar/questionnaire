package com.zzq.system.controller;

import com.zzq.system.service.DataAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sherlock
 */
@RestController
@RequestMapping("analysis")
public class DataAnalysisController {

    @Autowired
    private DataAnalysisService dataAnalysisService;

    @GetMapping("")
    public String analysis(String date) {
        return dataAnalysisService.startDataAnalysis(date);
    }
}
