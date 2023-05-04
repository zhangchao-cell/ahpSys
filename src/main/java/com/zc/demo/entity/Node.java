package com.zc.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("node")
public class Node {
    Integer id;
    String nodeId;
    Boolean isroot;
    String topic;
    String direction;
    Boolean expanded;
    Integer parentid;
    Integer modelId;
    String location;
    Double singleWeight;
    Double indicatorWeight;
    Boolean isleaf;
}
