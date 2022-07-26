package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.BaseModel;
import com.example.demo.model.BulkDeleteItem;
import com.example.demo.model.BulkUpdateItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.documentdb.bulkexecutor.BulkDeleteResponse;
import com.microsoft.azure.documentdb.bulkexecutor.BulkImportResponse;
import com.microsoft.azure.documentdb.bulkexecutor.BulkUpdateResponse;
import com.microsoft.azure.documentdb.bulkexecutor.DocumentBulkExecutor;
import com.microsoft.azure.documentdb.bulkexecutor.UpdateItem;
import com.microsoft.azure.documentdb.bulkexecutor.DocumentBulkExecutor.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class BulkExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkExecutorService.class);

    @Autowired
    Builder builder;

    @Autowired
    ObjectMapper mapper;

    public BulkDeleteResponse bulkDelete(List<BulkDeleteItem> deleteItems) {
        BulkDeleteResponse response = null;
        try (DocumentBulkExecutor bExecutor = builder.build()) {
            List<Pair<String, String>> pksAndIds = deleteItems.stream().map(BulkDeleteItem::toPair).collect(Collectors.toList());
            response = bExecutor.deleteAll(pksAndIds);
        } catch (Exception ex) {
            LOGGER.error("Error bulk deleting", ex);
        }
        return response;
    }

    private String serialize(BaseModel model)  {
        String rawJson = null;
        try {
            rawJson = mapper.writeValueAsString(model);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Error serializing", ex);
        }
        LOGGER.info(rawJson);
        return rawJson;
    } 

    public BulkImportResponse bulkImport(List<BaseModel> documents) {
        BulkImportResponse response = null;
        List<String> convertedDocuments = documents.stream().map(this::serialize).collect(Collectors.toList());
        try (DocumentBulkExecutor bExecutor = builder.build()) {
            response = bExecutor.importAll(convertedDocuments, true, true, null);
        } catch (Exception ex) {
            LOGGER.error("Error bulk importing", ex);
        }
        return response;
    }

    public BulkUpdateResponse bulkUpdate(List<BulkUpdateItem> updateItems) {
        BulkUpdateResponse response = null;
        List<UpdateItem> updates = updateItems.stream().map(BulkUpdateItem::ToUpdateItem).collect(Collectors.toList());
        try (DocumentBulkExecutor bExecutor = builder.build()) {
            response = bExecutor.updateAll(updates, null);
        } catch (Exception ex) {
            LOGGER.error("Error bulk updating", ex);
        }
        return response;
    }
}
