package com.zc.demo.common;

import com.zc.demo.entity.Node;
import com.zc.demo.entity.Scheme;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SliderRequest2 {
    private Integer value;
    private String text1;
    private String text2;
    private Integer id1;
    private Integer id2;
    private Node node;
    private Scheme scheme1;
    private Scheme scheme2;
}
