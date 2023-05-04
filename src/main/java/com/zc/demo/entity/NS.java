package com.zc.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("ns")
public class NS {
    @TableId(value = "nm_id", type = IdType.AUTO)//添加主键
    Integer nmId;
    Integer nodeId;
    Integer schemeId;
    Double grade;

    public NS(Integer nmId, Integer nodeId, Integer schemeId, Double grade) {
        this.nmId = nmId;
        this.nodeId = nodeId;
        this.schemeId = schemeId;
        this.grade = grade;
    }
}
