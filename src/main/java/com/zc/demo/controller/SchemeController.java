package com.zc.demo.controller;

import com.zc.demo.common.NodeValue;
import com.zc.demo.common.SchemeNodeValue;
import com.zc.demo.common.SliderRequest;
import com.zc.demo.common.SliderRequest2;
import com.zc.demo.entity.Scheme;
import com.zc.demo.mapper.SchemeMapper;
import com.zc.demo.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scheme")
public class SchemeController {
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private SchemeMapper schemeMapper;

    //方案层
    @GetMapping
    public List<Scheme> selectAllScheme(){
        return schemeService.list();
    }

    @GetMapping("/selectByModelId")
    public List<Scheme> selectByModelId(@RequestParam Integer modelId){
        return schemeMapper.selectByModelId(modelId);
    }



    @PostMapping("/saveScheme")
    public Integer saveScheme(@RequestBody List<Scheme> schemes){
        //schemeService.deleteAll();
        int sum = 0;
        for (int i = 0; i < schemes.size(); i++) {
            boolean isInsert = schemeService.save(schemes.get(i));
            if(isInsert){
                sum ++;
            }else{
                return 0;
            }

        }
        return sum;
    }

    @PostMapping("/sliderValues2")
    public Map<String, SchemeNodeValue[][]> getSliderValues(@RequestBody List<List<SliderRequest2>> sliderRequest2s){
        for (List<SliderRequest2> s:
                sliderRequest2s) {
            for (int i = 0; i < s.size(); i++) {
                System.out.println(s.get(i));
            }
            System.out.println("----------");
        }
        Map<String, List<SliderRequest2>> map = new HashMap<>();
        for (List<SliderRequest2> s : sliderRequest2s) {
            map.put(s.get(0).getNode().getTopic(), s);
        }

        // 输出结果
        for (Map.Entry<String, List<SliderRequest2>> entry : map.entrySet()) {
            System.out.println("topic " + entry.getKey() + " List:");
            for (SliderRequest2 sliderRequest : entry.getValue()) {
                System.out.println(sliderRequest.toString());
            }
            System.out.println("----------------------------------------");
        }
        //构建层次单排序矩阵
        //1.得到每一个层次对应的二维数组，存放到map中
        Map<String, SchemeNodeValue[][]> mapMatrix = schemeService.buildMatrix(map);
        //2.得到每一个层次中每一个指标对应的权重，存放到map中
        Map<String, SchemeNodeValue[]> mapSingleWeight = schemeService.computeValue(mapMatrix);
        //3.一致性检验，得到每个二维数组是否通过一致性检验的一维集合List，0为未通过，1位通过
        List<Double> checkResult = schemeService.check(mapMatrix,mapSingleWeight);
        //4. 得到层次单排序的结果矩阵，展示到前端
        Map<String, SchemeNodeValue[][]> singleValues = schemeService.getSingleValues(mapMatrix,mapSingleWeight);

        return singleValues;
    }

    @PostMapping("/saveSchemeOnBatch")
    public boolean saveSchemeOnBatch(@RequestBody List<Scheme> schemes){
        return schemeService.saveOrUpdateBatch(schemes);
    }



}
