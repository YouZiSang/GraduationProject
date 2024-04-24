package com.example.yzs.entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
    private Integer toId;
    private String message;
    public Message(String message) {
        this.message = message;
    }
}
