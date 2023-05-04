package com.zc.demo.common;

import com.zc.demo.entity.Node;
import com.zc.demo.entity.Scheme;
import lombok.Data;

@Data
public class SchemeNodeValue {
    Scheme scheme;
    Node node;
    double value;

    public SchemeNodeValue(Scheme scheme, Node node, double value) {
        this.scheme = scheme;
        this.node = node;
        this.value = value;
    }
}
