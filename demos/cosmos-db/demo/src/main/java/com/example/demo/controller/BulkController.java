package com.example.demo.controller;

import java.util.List;

import com.azure.cosmos.models.CosmosBulkItemResponse;
import com.example.demo.model.BaseModel;
import com.example.demo.service.BulkExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bulk")
public class BulkController {
    
    @Autowired
    BulkExecutorService bulkExecutorService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public CosmosBulkItemResponse create(@RequestBody List<BaseModel> documents) {      
        return this.bulkExecutorService.bulkImport(documents);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public CosmosBulkItemResponse update(@RequestBody List<BaseModel> updates) {
        return this.bulkExecutorService.bulkUpdate(updates);
    }

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    public CosmosBulkItemResponse delete(@RequestBody List<BaseModel> deleteItems){
        return this.bulkExecutorService.bulkDelete(deleteItems);
    }
}