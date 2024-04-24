package com.example.yzs.entity.chat;

import lombok.Data;

@Data
public class Candidate {
    private String type;
    private String sdp;
    private String candidate;
    private String sdpMid;
    private String sdpMLineIndex;
    private String usernameFragment;
}
