package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.BaseModel;
import com.example.demo.model.BulkDeleteItem;
import com.example.demo.model.BulkUpdateItem;
import com.example.demo.service.BulkExecutorService;
import com.microsoft.azure.documentdb.bulkexecutor.BulkDeleteResponse;
import com.microsoft.azure.documentdb.bulkexecutor.BulkImportResponse;
import com.microsoft.azure.documentdb.bulkexecutor.BulkUpdateResponse;

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
    public BulkImportResponse create(@RequestBody List<BaseModel> documents) {      
        return this.bulkExecutorService.bulkImport(documents);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public BulkUpdateResponse update(@RequestBody List<BulkUpdateItem> updates) {
        return this.bulkExecutorService.bulkUpdate(updates);
    }

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    public BulkDeleteResponse delete(@RequestBody List<BulkDeleteItem> deleteItems){
        return this.bulkExecutorService.bulkDelete(deleteItems);
    }
}
