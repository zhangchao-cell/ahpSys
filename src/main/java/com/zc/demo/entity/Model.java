package com.zc.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("model")
public class Model {
    @TableId(value = "model_id", type = IdType.AUTO)//添加主键
    Integer modelId;
    String modelName;
    String modelCreatedata;
    String modelDetail;
    Integer userId;
}
