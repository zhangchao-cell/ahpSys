package com.zc.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zc.demo.entity.NS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NSMapper extends BaseMapper<NS> {

    @Update("UPDATE scheme s " +
            " LEFT JOIN (" +
            "  SELECT ns.scheme_id, SUM(n.indicator_weight * ns.grade) as total" +
            "  FROM ns" +
            "  LEFT JOIN node n ON ns.node_id = n.id" +
            "  GROUP BY ns.scheme_id" +
            ") t ON s.scheme_id = t.scheme_id " +
            " SET s.total_grade = t.total;")
    int updateSchemeTotal();

    @Select("select total_grade from scheme where model_id = #{modelId}")
    List<Double> selectTotalGrades(@Param("modelId") Integer modelId);
}
