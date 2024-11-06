package com.dxplab.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ColumnControlModel {

    @ValueMapValue
    private String columnNumber;

    private List<Integer> columnNumberList = Collections.emptyList();

    @PostConstruct
    private void init() {
        if (columnNumber != null && !columnNumber.trim().isEmpty()) {
            columnNumberList = new ArrayList<>();
            try {
                int num = Integer.parseInt(columnNumber);
                for (int i = 0; i < num; i++) {
                    columnNumberList.add(i);
                }
            } catch (NumberFormatException e) {
                // Log an error or handle the exception appropriately
                // For example, you could log the error like this:
                // log.error("Invalid columnNumber format: {}", columnNumber, e);
            }
        }
    }

    public List<Integer> getColumnNumberList() {
        return columnNumberList;
    }
}