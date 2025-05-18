package com.dto;

import lombok.Data;
import java.util.List;

@Data
public class BulkAssignRequest {
    private List<Long> projectIds;
    private List<Long> studentIds;
}