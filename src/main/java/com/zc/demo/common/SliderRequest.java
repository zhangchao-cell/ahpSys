package com.zc.demo.common;

import com.zc.demo.entity.Node;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SliderRequest {
    private Integer value;
    private String text1;
    private String text2;
    private Integer id1;
    private Integer id2;
    private String location;
    private Node node1;
    private Node node2;

}
