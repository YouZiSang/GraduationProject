package com.example.yzs.constant;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
//全局，在任何地方都可以用
@Component
public class CaptureConfig {

    public static Map<String,String> CAPTURE_MAP = new HashMap<>();

}
