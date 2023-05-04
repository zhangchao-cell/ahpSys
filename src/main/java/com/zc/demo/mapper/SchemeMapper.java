package com.zc.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zc.demo.entity.Scheme;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SchemeMapper extends BaseMapper<Scheme> {
    //得到所有方案节点


    @Insert("insert into scheme (scheme_name,scheme_detail,model_id) values " +
            "(#{scheme_name},#{scheme_detail},#{model_id})")
    int insertScheme(Scheme scheme);


    int updateScheme(Scheme scheme);

    @Update("truncate table scheme")
    void deleteAll();

    @Select("select * from scheme where model_id = #{modelId}")
    List<Scheme> selectByModelId(@Param("modelId") Integer modelId);
}
