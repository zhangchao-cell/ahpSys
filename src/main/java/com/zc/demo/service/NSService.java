package com.zc.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.demo.common.SchemeNodeValue;
import com.zc.demo.entity.NS;
import com.zc.demo.mapper.NSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NSService extends ServiceImpl<NSMapper, NS> {
    @Autowired
    NSMapper nsMapper;


    public NS[][] getSchemeValues(Map<String, SchemeNodeValue[]> singleMap, List<Double> checkResult) {
        NS[][] schemeValuesMatrix = new NS[checkResult.size()][];

        int index = 0;
        for (Map.Entry<String, SchemeNodeValue[]> entry : singleMap.entrySet()) {
            //和二维数组对应的权重值数组
            SchemeNodeValue[] schemeNodeValues = singleMap.get(entry.getKey());
            schemeValuesMatrix[index] = new NS[schemeNodeValues.length+1];
            for (int j = 0; j < schemeValuesMatrix[index].length; j++) {
                if (j < schemeNodeValues.length) {
                    schemeValuesMatrix[index][j] = new NS(null, schemeNodeValues[j].getNode().getId(),
                            schemeNodeValues[j].getScheme().getSchemeId(), schemeNodeValues[j].getValue());
                } else if (j == entry.getValue().length) {
                    schemeValuesMatrix[index][j] = new NS(null, null,
                            null, checkResult.get(index));
                }
            }
            index++;
        }

        System.out.println("----------schemeValuesMatrix---------");
        for (int i = 0; i < schemeValuesMatrix.length; i++) {
            for (int j = 0; j < schemeValuesMatrix[i].length-1; j++) {
                System.out.print(schemeValuesMatrix[i][j]+" ");
                //保存ns到数据库
                save(schemeValuesMatrix[i][j]);
            }
            System.out.println();
        }
        return schemeValuesMatrix;
    }

    public void updateSchemeTotal(){
        int i = nsMapper.updateSchemeTotal();
        System.out.println("3232652115;;;"+i);
    }

}
