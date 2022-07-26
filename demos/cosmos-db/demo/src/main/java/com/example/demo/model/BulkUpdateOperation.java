package com.example.demo.model;

import com.microsoft.azure.documentdb.bulkexecutor.SetUpdateOperation;
import com.microsoft.azure.documentdb.bulkexecutor.internal.UpdateOperation;
import com.microsoft.azure.documentdb.bulkexecutor.internal.UpdateOperationType;
import lombok.*;

@Getter @Setter @NoArgsConstructor
public class BulkUpdateOperation {
    
    private UpdateOperationType type;
    private String field;
    private Object value;
 
    public UpdateOperation<Object> toUpdateOperation() {
        //TODO define the UpdateOperationTypes so they implement the appropriate subclass
        switch(type) {
            default:
                return new SetUpdateOperation<Object>(field, value); 
        }
    }
}
