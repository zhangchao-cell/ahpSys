package com.zc.demo.controller;

import com.zc.demo.common.SchemeNodeValue;
import com.zc.demo.common.SliderRequest;
import com.zc.demo.common.SliderRequest2;
import com.zc.demo.entity.NS;
import com.zc.demo.entity.Scheme;
import com.zc.demo.mapper.NSMapper;
import com.zc.demo.service.NSService;
import com.zc.demo.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ns")
public class NSController {
    @Autowired
    NSService nsService;

    @Autowired
    NSMapper nsMapper;

    @Autowired
    private SchemeService schemeService;

    @GetMapping
    public List<NS> selectAllNS(){
        return nsService.list();
    }

    @PostMapping("/saveNS")
    public Integer saveNS(@RequestBody List<NS> nsList){
        int sum = 0;
        for (int i = 0; i < nsList.size(); i++) {
            boolean isInsert = nsService.save(nsList.get(i));
            if(isInsert){
                sum ++;
            }else{
                return 0;
            }

        }
        return sum;
    }

    @DeleteMapping("/del")
    public boolean delById(@RequestParam String nsId){
        return nsService.removeById(nsId);
    }

    @PostMapping("/updateNS")
    public Integer updateNS(@RequestBody List<NS> nsList){
        int sum = 0;
        for (int i = 0; i < nsList.size(); i++) {
            boolean isInsert = nsService.updateById(nsList.get(i));
            if(isInsert){
                sum ++;
            }else{
                return 0;
            }

        }
        return sum;
    }

    @PostMapping("/sliderValues2")
    public NS[][] getSliderValues(@RequestBody List<List<SliderRequest2>> sliderRequest2s){
        for (List<SliderRequest2> s:
                sliderRequest2s) {
            for (int i = 0; i < s.size(); i++) {
                System.out.println(s.get(i));
            }
            System.out.println("----------");
        }
//        for (SliderRequest2 s:
//                sliderRequest2s) {
//            System.out.println(s);
//        }

        Map<String, List<SliderRequest2>> map = new LinkedHashMap<>();
        for (List<SliderRequest2> s : sliderRequest2s) {
            System.out.println("---1---");
            for (int i = 0; i < s.size(); i++) {
                System.out.println(s.get(i).getNode().getTopic());
            }
            System.out.println("---2---");
            System.out.println(s.get(0).getNode().getTopic());
            map.put(s.get(0).getNode().getTopic(), s);
        }

//        Map<String, List<SliderRequest2>> map = new HashMap<>();
//
//
//        for (SliderRequest2 sliderRequest2 : sliderRequest2s) {
//            //返回key中key对应的value，如果为空，则默认为新建一个new ArrayList<>()
//            //这样就可以实现，按照数组元素的location属性进行分割，把相同的分到一个arraylist里面
//            //！！！！！！！！！！！！！！开心(*￣︶￣)
//            List<SliderRequest2> list = map.getOrDefault(sliderRequest2.getNode().getTopic(), new ArrayList<>());
//            list.add(sliderRequest2);
//            map.put(sliderRequest2.getNode().getTopic(), list);
//        }

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
        NS[][] singleValues = nsService.getSchemeValues(mapSingleWeight,checkResult);
       // Map<String, SchemeNodeValue[][]> singleValues = schemeService.getSingleValues(mapMatrix,mapSingleWeight);

        //更新scheme数据库的total_grade值
        nsService.updateSchemeTotal();
        return singleValues;
    }

    @GetMapping("/totalGrades")
    public List<Double> selectTotalGrades(@RequestParam Integer modelId){
        return nsMapper.selectTotalGrades(modelId);
    }
}
