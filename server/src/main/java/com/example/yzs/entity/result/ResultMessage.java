package com.example.yzs.entity.result;

import lombok.Data;

@Data
public class ResultMessage {
    private boolean isSystem;
    private Integer fromId;
    private Object message;
}
