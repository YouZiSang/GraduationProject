package com.example.yzs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageBean<T> {
    private Long total;//总条数
    private List<T> items;//当前页数据集合
}
