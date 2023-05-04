package com.zc.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("scheme")
public class Scheme {
    @TableId(value = "scheme_id", type = IdType.AUTO)//添加主键
    Integer schemeId;
    String schemeName;
    String schemeDetail;
    Integer modelId;
    Double totalGrade;
}
