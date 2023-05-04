package com.zc.demo.mapper;

import com.zc.demo.entity.Node;
import com.zc.demo.entity.Scheme;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NodeMapper {
    @Insert("insert into node(node_id, isroot ,topic,direction," +
            "expanded,parentid ,model_id ,location,single_weight,indicator_weight,isleaf) " +
            "VALUES(#{nodeId},#{isroot},#{topic},#{direction},#{expanded},#{parentid}," +
            "#{modelId},#{location},#{singleWeight},#{indicatorWeight},#{isleaf})")
    int insertNode(Node node);

    @Insert("insert into node(node_id, isroot ,topic," +
            "model_id,single_weight,indicator_weight ) " +
            "VALUES(#{nodeId},#{isroot},#{topic}," +
            "#{modelId},#{singleWeight},#{indicatorWeight})")
    int insertJsNode(Node node);

//    @Update("UPDATE node SET parentid = " +
//            "(select n1.id from (" +
//            "(SELECT id FROM node WHERE node_id = (select parentid from node where id = #{id}) " +
//            "and model_id = (select model_id from node where id = #{id})))n1) " +
//            "where id = #{id}")
//    int updateJsNodeParentId(Node node);

    @Update("UPDATE node SET parentid = " +
            "(select n1.id from (" +
            "(SELECT id FROM node WHERE node_id =  #{parentId} " +
            "and model_id = (select model_id from node where id = #{n.id})))n1) " +
            "where id = #{n.id}")
    int updateJsNodeParentId(@Param("n")Node node,@Param("parentId") Integer parentId);


    @Update("update node set topic=#{topic}, direction=#{direction}," +
            "expanded=#{expanded},parentid=#{parentid},model_id=#{modelId}," +
            "location=#{location},single_weight=#{singleWeight}," +
            "indicator_weight=#{indicatorWeight},isleaf=#{isleaf} " +
            "where id = #{id};")
    int updateNode(Node node);

    @Select("select * from node")
    List<Node> selectAll();


    @Delete("delete from node where id = #{id}")
    boolean delById(@Param("id") String id);

    @Delete("delete from node")
    boolean delAll();

    @Update("update node set single_weight=#{singleWeight} " +
            "where id = #{id};")
    int updateSingleWeight(Node node);

    @Update("UPDATE node SET indicator_weight = " +
            "(select n1.single_weight from (SELECT single_weight FROM node WHERE id = #{id})n1) " +
            "* (select n2.single_weight from(SELECT single_weight FROM node " +
            "WHERE id = (select n3.parentid from(SELECT parentid FROM node WHERE id = #{id})n3))n2) WHERE id = #{id};")
    int updateIndicatorWeight(Node node);

    @Update("update node set isleaf = 1 " +
            "where model_id=#{modelId} " +
            "and (id not in " +
            "(SELECT n1.parentid from" +
            "(SELECT parentid from node where model_id = #{modelId} and parentid is not null)n1))")
    int updateIsLeafByModelId(@Param("modelId") Integer modelId);

    @Select("select * from node where isleaf = "+1+" and model_id = #{modelId}")
    List<Node> selectLeafNodeByModelId(@Param("modelId") Integer modelId);

    @Select("select * from node where model_id = #{modelId}")
    List<Node> selectNodeByModelId(@Param("modelId") Integer modelId);


}
