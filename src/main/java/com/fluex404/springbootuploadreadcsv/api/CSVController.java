package com.fluex404.springbootuploadreadcsv.api;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value ="/api/csv")
public class CSVController {
    @PostMapping("/upload")
    public ResponseEntity upload(
            @RequestParam("file")MultipartFile file) {
        if (isCSVFile(file)) {
            try {
                return ResponseEntity.ok(csvToListModelMap(file));
            } catch (Exception e) {
                return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity("file isn't csv", HttpStatus.NOT_ACCEPTABLE);
        }
    }




    private boolean isCSVFile(MultipartFile file){
        if(!"text/csv".equals(file.getContentType())){
            return false;
        }
        return true;
    }

    private List<ModelMap> csvToListModelMap(MultipartFile file) throws Exception {
        BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), "UTF-8"));
        CSVParser csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().withDelimiter(';').parse(fileReader);
        List<ModelMap> list = new ArrayList<>();

        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

        for (CSVRecord csvRecord : csvRecords) {
            ModelMap m = new ModelMap();

            m.put("no", csvRecord.get("User Number"));
            m.put("name", csvRecord.get("User Name"));
            m.put("email", csvRecord.get("User Email"));

            list.add(m);
        }

        return list;
    }
}

