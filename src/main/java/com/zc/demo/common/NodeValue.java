package com.zc.demo.common;

import com.zc.demo.entity.Node;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NodeValue {
    Node node;
    double value;

    public NodeValue() {
    }

    public NodeValue(Node node, double value) {
        this.node = node;
        this.value = value;
    }
}
