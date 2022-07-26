package com.example.demo.model;

import java.util.List;
import java.util.stream.Collectors;

import com.microsoft.azure.documentdb.bulkexecutor.UpdateItem;
import com.microsoft.azure.documentdb.bulkexecutor.UpdateOperationBase;


import lombok.*;

@Getter @Setter @NoArgsConstructor
public class BulkUpdateItem {

    private String id;
    private String partitionKey;
    private List<BulkUpdateOperation> operations;

    public UpdateItem ToUpdateItem() {
        List<UpdateOperationBase> updateOperations = operations.stream().map(BulkUpdateOperation::toUpdateOperation)
            .collect(Collectors.toList());
        return new UpdateItem(id, partitionKey, updateOperations);
    }
}
