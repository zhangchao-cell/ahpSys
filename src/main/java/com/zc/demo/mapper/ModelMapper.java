package com.zc.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zc.demo.entity.Model;
import com.zc.demo.entity.Scheme;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ModelMapper extends BaseMapper<Model> {
    @Insert("insert into model (model_id,model_name,model_detail,user_id) values " +
            "(#{modelId},#{modelName},#{modelDetail},#{userId})")
    int insertModel(Model model);

    @Select("select * from model where user_id = #{userId}")
    List<Model> selectByUserId(Integer userId);
}
