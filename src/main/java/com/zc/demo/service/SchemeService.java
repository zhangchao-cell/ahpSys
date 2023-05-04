package com.zc.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.demo.common.*;
import com.zc.demo.entity.NS;
import com.zc.demo.entity.Node;
import com.zc.demo.entity.Scheme;
import com.zc.demo.mapper.SchemeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class SchemeService  extends ServiceImpl<SchemeMapper, Scheme> {
    @Autowired
    SchemeMapper schemeMapper;
    private List<Double> checkResult;
    double[] ris = {0,0,0.58,0.90,1.12,1.24,1.32,1.41,1.45,1.49,1.51,1.54,1.56};
    public boolean insertOrUpdate(Scheme scheme){
        if(scheme.getSchemeId() == null){
            return save(scheme);
        }else{
            return updateById(scheme);
        }
//        return saveOrUpdate(scheme);
    }

    public boolean saveOrUpdateBatch(List<Scheme> schemes){
        return saveOrUpdateBatch(schemes);
    }

//    public List<Scheme> selectAllScheme() {
//        List<Scheme> schemes = list();
//
//       return   schemes;
//    }

    public void deleteAll() {
        schemeMapper.deleteAll();
    }

    public Map<String, SchemeNodeValue[][]> buildMatrix(Map<String, List<SliderRequest2>> map) {
        Map<String, SchemeNodeValue[][]> mapMatrix = new LinkedHashMap<>();
        for (Map.Entry<String, List<SliderRequest2>> entry : map.entrySet()) {
            //slider组件个数为m，对应表格中的元素个数为n，把么m=n*(n-1)/2
            //n = (sqrt(1 + 8m) + 1) / 2.
            //slider组件个数m为entry.getValue().size()
            int length = (int) ((Math.sqrt(1 + 8 * entry.getValue().size()) + 1) / 2);
            SchemeNodeValue[][] matrix = new SchemeNodeValue[length][length];
            int index = 0;
            for (int i = 0; i < length; i++) {
                for (int j = i; j < length; j++) {
                    if (i == j) {
                        if(i == 0){
                            matrix[i][j] = new SchemeNodeValue(entry.getValue().get(index).getScheme1(),entry.getValue().get(index).getNode(),1.0);
                        }else{
                            matrix[i][j] = new SchemeNodeValue(null,null,1.0);
                        }

                    } else {
                        SliderRequest2 sliderRequest = entry.getValue().get(index);
                        double realValue = Math.abs(sliderRequest.getValue()) + 1.0;
                        Node node = sliderRequest.getNode();
                        Scheme scheme1 = sliderRequest.getScheme1();
                        Scheme scheme2 = sliderRequest.getScheme2();
                        if (sliderRequest.getValue() < 0) {
                            matrix[i][j] = new SchemeNodeValue(scheme1,node,realValue);
                            DecimalFormat df = new DecimalFormat("#.#####");
                            matrix[j][i] = new SchemeNodeValue(scheme2,node,Double.parseDouble(df.format(1.0 / realValue)));

                        } else if (sliderRequest.getValue() == 0) {
                            matrix[i][j] = new SchemeNodeValue(scheme1,node,1.0);
                            matrix[j][i] = new SchemeNodeValue(scheme2,node,1.0);

                        } else if (sliderRequest.getValue() > 0) {
                            DecimalFormat df = new DecimalFormat("#.#####");
                            matrix[i][j] = new SchemeNodeValue(scheme1,node,Double.parseDouble(df.format(1.0 / realValue)));
                            matrix[j][i] = new SchemeNodeValue(scheme2,node,realValue);

                        }
                        index++;
                    }

                }
            }
            mapMatrix.put(entry.getKey(), matrix);

        }

        // 输出结果
        for (Map.Entry<String, SchemeNodeValue[][]> entry : mapMatrix.entrySet()) {
            System.out.println("topic " + entry.getKey() + " SchemeNodeValue[][]:");
            for (int i = 0; i < entry.getValue().length; i++) {
                for (int j = 0; j < entry.getValue().length; j++) {
                    System.out.print(entry.getValue()[i][j] + " ");
                }
                System.out.println("\n");
            }
            System.out.println("----------------------------------------");
        }
        //this.mapMatrix = mapMatrix;
        return mapMatrix;
    }

    public Map<String, SchemeNodeValue[]> computeValue(Map<String, SchemeNodeValue[][]> map) {
        Map<String, SchemeNodeValue[]> mapSingleWeight = new LinkedHashMap<>();

        for (Map.Entry<String, SchemeNodeValue[][]> entry : map.entrySet()) {
            SchemeNodeValue[] mVector = new SchemeNodeValue[entry.getValue().length];
            double sum = 0;

            for (int i = 0; i < entry.getValue().length; i++) {
                double result = 1.0;
                for (int j = 0; j < entry.getValue().length; j++) {
                    result *= entry.getValue()[i][j].getValue();
                    if (j == entry.getValue().length - 1) {
                        // result = Math.sqrt(result);
                        result = MathCal.sqrt(result,entry.getValue().length);
                    }
                }
                sum += result;
                mVector[i] = new SchemeNodeValue(entry.getValue()[i][0].getScheme(),entry.getValue()[i][0].getNode(),result);
            }

            for (int i = 0; i < mVector.length; i++) {
                //mVector[i].setValue(mVector[i].getValue()/sum);
                DecimalFormat df = new DecimalFormat("#.###");
                mVector[i].setValue(Double.parseDouble(df.format(mVector[i].getValue()/sum)));

            }
            mapSingleWeight.put(entry.getKey(),mVector);
        }

        // 输出结果
        for (Map.Entry<String, SchemeNodeValue[]> entry : mapSingleWeight.entrySet()) {
            System.out.println("topic " + entry.getKey() + " SchemeNodeValue[]:");
            //创建节点用来更新singleWeigh

            // node.setLocation(entry.getKey());

            for (int i = 0; i < entry.getValue().length; i++) {
                System.out.print(entry.getValue()[i] + " ");
                //修改节点的单排序权重、指标评价权重
//                Node node = entry.getValue()[i].getNode();
//                node.setSingleWeight(entry.getValue()[i].getValue());
//                this.nodeMapper.updateSingleWeight(node);
//                this.nodeMapper.updateIndicatorWeight(node);
            }

            //this.nodeMapper.updateNodes(entry.getKey(),entry.getValue());
            System.out.println("----------------------------------------");
        }
        //this.mapSingleWeight = mapSingleWeight;
        return mapSingleWeight;
    }

    public List<Double> check(Map<String, SchemeNodeValue[][]> mapMatrix, Map<String, SchemeNodeValue[]> mapSingleWeight) {
        List<Double> checkResult = new ArrayList<>();
        for (Map.Entry<String, SchemeNodeValue[][]> entry : mapMatrix.entrySet()) {
            SchemeNodeValue[] singleweightValue = mapSingleWeight.get(entry.getKey());
            double averageSum = 0;
            //修改，之前是直接在原数组上做了修改，因为此处只需要得到一个数值结果，现在新建一个数组来操作
            double[][] newMapMatrix = new double[entry.getValue().length][entry.getValue().length];
            for (int i = 0; i < entry.getValue().length; i++) {
                double sum = 0, average;
                for (int j = 0; j < entry.getValue().length; j++) {
                    //entry.getValue()[i][j] *=  singleweightValue[j];
                    //sum += entry.getValue()[i][j];
                    newMapMatrix[i][j] = entry.getValue()[i][j].getValue() * singleweightValue[j].getValue();
                    sum += newMapMatrix[i][j];

                }
                average = sum/(entry.getValue().length * singleweightValue[i].getValue());
                averageSum += average;
            }
            System.out.println("averageSum:"+averageSum);
            double ci = (averageSum - entry.getValue().length) / (entry.getValue().length - 1);
            System.out.println("ci:"+ci);
            double ri = ris[entry.getValue().length-1];
            System.out.println("ri:"+ri);
            double cr = ci / ri;
            System.out.println("cr:"+cr);
//            this.ci = ci;
//            this.ri = ri;
            //0为不通过检验，1位通过检验
            if(cr >= 0.1){
                checkResult.add(0.0);
            }else{
                checkResult.add(1.0);
            }

        }
        this.checkResult = checkResult;
        System.out.println("checkResult:-----");
        for (int i = 0; i <  this.checkResult.size(); i++) {
            System.out.print(this.checkResult.get(i)+" ");
        }
        return checkResult;
    }

    public Map<String, SchemeNodeValue[][]> getSingleValues(Map<String, SchemeNodeValue[][]> map, Map<String, SchemeNodeValue[]> singleMap) {
        Map<String, SchemeNodeValue[][]> singleValues = new LinkedHashMap<>();
        SchemeNodeValue[][] singleResultMatrix;
        int index = 0;//checkResult数组的下标，对应的值checkResult[index]表示对应的二维矩阵是否符合
        //一致性检验。

        for (Map.Entry<String, SchemeNodeValue[][]> entry : map.entrySet()) {
            singleResultMatrix = new SchemeNodeValue[entry.getValue().length][entry.getValue().length+2];
            //和二维数组对应的权重值数组
            SchemeNodeValue[] nodeValues = singleMap.get(entry.getKey());
            for (int i = 0; i < entry.getValue().length; i++) {
                for (int j = 0; j < singleResultMatrix[i].length; j++) {
                    //singleResultMatrix[i][j] = entry.getValue()[i][j];
                    if( j<entry.getValue().length){
                        singleResultMatrix[i][j] = entry.getValue()[i][j];
                    }else if( j==entry.getValue().length){
                        singleResultMatrix[i][j] = nodeValues[i];
                    }else if( j==entry.getValue().length+1){
                        singleResultMatrix[i][j] = new SchemeNodeValue(null,null,checkResult.get(index));
                    }
                }

            }
            index++;
            singleValues.put(entry.getKey(),singleResultMatrix);
        }

        for (Map.Entry<String, SchemeNodeValue[][]> entry : singleValues.entrySet()) {
            System.out.println("Location " + entry.getKey() + " singleValues:");
            for (int i = 0; i < entry.getValue().length; i++) {
                for (int j = 0; j < entry.getValue()[i].length; j++) {
                    System.out.print(entry.getValue()[i][j] + " ");
                }
                System.out.println("\n");
            }
            System.out.println("----------------------------------------");
        }
        return singleValues;

    }

//    public Map<String, NS[][]> getSchemeValues(Map<String, SchemeNodeValue[][]> map, Map<String, SchemeNodeValue[]> singleMap){
//
//    }
}
