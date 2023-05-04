package com.zc.demo.service;


import com.zc.demo.common.MathCal;
import com.zc.demo.common.NodeValue;
import com.zc.demo.common.SliderRequest;
import com.zc.demo.entity.Node;
import com.zc.demo.mapper.NodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NodeService{

    @Autowired
    NodeMapper nodeMapper;
    Map<String, double[][]> mapMatrix;
    Map<String, double[]> mapSingleWeight;
    double ci ;
    double ri ;
    double cr = ci / ri;
    List<Double> checkResult;

    double[] ris = {0,0,0.58,0.90,1.12,1.24,1.32,1.41,1.45,1.49,1.51,1.54,1.56};
    public boolean saveNode(Node node) {
        return true;
    }

//    public Map<String, double[][]> buildMatrix(Map<String, List<SliderRequest>> map) {
//        Map<String, double[][]> mapMatrix = new HashMap<>();
//        for (Map.Entry<String, List<SliderRequest>> entry : map.entrySet()) {
//            //slider组件个数为m，对应表格中的元素个数为n，把么m=n*(n-1)/2
//            //n = (sqrt(1 + 8m) + 1) / 2.
//            //slider组件个数m为entry.getValue().size()
//            int length = (int) ((Math.sqrt(1 + 8 * entry.getValue().size()) + 1) / 2);
//            double[][] matrix = new double[length][length];
//            int index = 0;
//            for (int i = 0; i < length; i++) {
//                for (int j = i; j < length; j++) {
////                    SliderRequest sliderRequest = entry.getValue().get(index);
//                    //Node node = sliderRequest.getNode1();
//                    if (i == j) {
//                        //matrix[i][j] = new NodeValue(node,1.0);
//                        matrix[i][j] = 1.0;
//                    } else {
//                        SliderRequest sliderRequest = entry.getValue().get(index);
//                        double realValue = Math.abs(sliderRequest.getValue()) + 1.0;
//                        if (sliderRequest.getValue() < 0) {
//                            matrix[i][j] = realValue;
//                            DecimalFormat df = new DecimalFormat("#.#####");
//                            matrix[j][i] = Double.parseDouble(df.format(1.0 / realValue));
////                            matrix[i][j] = new NodeValue(node,realValue);
////                            DecimalFormat df = new DecimalFormat("#.#####");
////                            matrix[j][i] = new NodeValue(node,Double.parseDouble(df.format(1.0 / realValue)));
//
//                        } else if (sliderRequest.getValue() == 0) {
////                            matrix[i][j] = new NodeValue(node,1.0);
////                            matrix[j][i] = new NodeValue(node,1.0);
//                            matrix[i][j] = 1.0;
//                            matrix[j][i] = 1.0;
//                        } else if (sliderRequest.getValue() > 0) {
////                            DecimalFormat df = new DecimalFormat("#.#####");
////                            matrix[i][j] = new NodeValue(node,Double.parseDouble(df.format(1.0 / realValue)));
////                            matrix[j][i] = new NodeValue(node,realValue);
//                            DecimalFormat df = new DecimalFormat("#.#####");
//                            matrix[i][j] = Double.parseDouble(df.format(1.0 / realValue));
//                            matrix[j][i] = realValue;
//                        }
//                        index++;
//                    }
//
//                }
//            }
//            mapMatrix.put(entry.getKey(), matrix);
//
//        }
//
//        // 输出结果
//        for (Map.Entry<String, double[][]> entry : mapMatrix.entrySet()) {
//            System.out.println("Location " + entry.getKey() + " double[][]:");
//            for (int i = 0; i < entry.getValue().length; i++) {
//                for (int j = 0; j < entry.getValue().length; j++) {
//                    System.out.print(entry.getValue()[i][j] + " ");
//                }
//                System.out.println("\n");
//            }
//            System.out.println("----------------------------------------");
//        }
//        //this.mapMatrix = mapMatrix;
//        return mapMatrix;
//    }
//
//    public Map<String, double[]> computeValue(Map<String, double[][]> map) {
//        Map<String, double[]> mapSingleWeight = new HashMap<>();
//
//        for (Map.Entry<String, double[][]> entry : map.entrySet()) {
//            double[] mVector = new double[entry.getValue().length];
//            double sum = 0;
//
//            for (int i = 0; i < entry.getValue().length; i++) {
//                double result = 1.0;
//                for (int j = 0; j < entry.getValue().length; j++) {
//                    result *= entry.getValue()[i][j];
//                    if (j == entry.getValue().length - 1) {
//                       // result = Math.sqrt(result);
//                        result = MathCal.sqrt(result,entry.getValue().length);
//                    }
//                }
//                sum += result;
//                mVector[i] = result;
//            }
//
//            for (int i = 0; i < mVector.length; i++) {
//                mVector[i] =  mVector[i]/sum;
//            }
//            mapSingleWeight.put(entry.getKey(),mVector);
//        }
//
//        // 输出结果
//        for (Map.Entry<String, double[]> entry : mapSingleWeight.entrySet()) {
//            System.out.println("Location " + entry.getKey() + " double[]:");
//            //创建节点用来更新singleWeigh
//            //Node node = new Node();
//           // node.setLocation(entry.getKey());
//
//            for (int i = 0; i < entry.getValue().length; i++) {
//                System.out.print(entry.getValue()[i] + " ");
//                //修改节点的单排序权重、指标评价权重
//                //node.setSingle_weight(entry.getValue()[i]);
//               // this.nodeMapper.updateNodeByLocation(node);
//            }
//
//            //this.nodeMapper.updateNodes(entry.getKey(),entry.getValue());
//            System.out.println("----------------------------------------");
//        }
//        this.mapSingleWeight = mapSingleWeight;
//        return mapSingleWeight;
//    }
//
//    public List<Double> check(Map<String,double[][]> mapMatrix, Map<String,double[]> mapSingleWeight) {
//        List<Double> checkResult = new ArrayList<>();
//        for (Map.Entry<String, double[][]> entry : mapMatrix.entrySet()) {
//            double[] singleweightValue = mapSingleWeight.get(entry.getKey());
//            double averageSum = 0;
//            //修改，之前是直接在原数组上做了修改，因为此处只需要得到一个数值结果，现在新建一个数组来操作
//            double[][] newMapMatrix = new double[entry.getValue().length][entry.getValue().length];
//            for (int i = 0; i < entry.getValue().length; i++) {
//                double sum = 0, average;
//                for (int j = 0; j < entry.getValue().length; j++) {
//                    //entry.getValue()[i][j] *=  singleweightValue[j];
//                    //sum += entry.getValue()[i][j];
//                    newMapMatrix[i][j] = entry.getValue()[i][j] * singleweightValue[j];
//                    sum += newMapMatrix[i][j];
//
//                }
//                average = sum/(entry.getValue().length*singleweightValue[i]);
//                averageSum += average;
//            }
//            System.out.println("averageSum:"+averageSum);
//            double ci = (averageSum - entry.getValue().length) / (entry.getValue().length - 1);
//            System.out.println("ci:"+ci);
//            double ri = ris[entry.getValue().length-1];
//            System.out.println("ri:"+ri);
//            double cr = ci / ri;
//            System.out.println("cr:"+cr);
////            this.ci = ci;
////            this.ri = ri;
//            //0为不通过检验，1位通过检验
//            if(cr >= 0.1){
//                checkResult.add(0.0);
//            }else{
//                checkResult.add(1.0);
//            }
//
//        }
//        this.checkResult = checkResult;
//        return checkResult;
//
//    }
//
//    //得到层次单排序的结果矩阵
//    public Map<String, double[][]> getSingleValues(Map<String, double[][]> map){
//        Map<String, double[][]> singleValues = new HashMap<>();
//        double[][] singleResultMatrix;
//        int index = 0;//checkResult数组的下标，对应的值checkResult[index]表示对应的二维矩阵是否符合
//        //一致性检验。
//
//        for (Map.Entry<String, double[][]> entry : map.entrySet()) {
//            singleResultMatrix = new double[entry.getValue().length][entry.getValue().length+2];
//            //和二维数组对应的权重值数组
//            double[] doubles = this.mapSingleWeight.get(entry.getKey());
//            for (int i = 0; i < entry.getValue().length; i++) {
//                for (int j = 0; j < singleResultMatrix[i].length; j++) {
//                    //singleResultMatrix[i][j] = entry.getValue()[i][j];
//                    if( j<entry.getValue().length){
//                        singleResultMatrix[i][j] = entry.getValue()[i][j];
//                    }else if( j==entry.getValue().length){
//                        singleResultMatrix[i][j] = doubles[i];
//                    }else if( j==entry.getValue().length+1){
//                        singleResultMatrix[i][j] = checkResult.get(index);
//                    }
//                }
//
//            }
//            index++;
//            singleValues.put(entry.getKey(),singleResultMatrix);
//        }
//
//        for (Map.Entry<String, double[][]> entry : singleValues.entrySet()) {
//            System.out.println("Location " + entry.getKey() + " singleValues:");
//            for (int i = 0; i < entry.getValue().length; i++) {
//                for (int j = 0; j < entry.getValue()[i].length; j++) {
//                    System.out.print(entry.getValue()[i][j] + " ");
//                }
//                System.out.println("\n");
//            }
//            System.out.println("----------------------------------------");
//        }
//        return singleValues;
//    }


    public Map<String, NodeValue[][]> buildMatrix(Map<String, List<SliderRequest>> map) {
        Map<String, NodeValue[][]> mapMatrix = new HashMap<>();
        for (Map.Entry<String, List<SliderRequest>> entry : map.entrySet()) {
            //slider组件个数为m，对应表格中的元素个数为n，把么m=n*(n-1)/2
            //n = (sqrt(1 + 8m) + 1) / 2.
            //slider组件个数m为entry.getValue().size()
            int length = (int) ((Math.sqrt(1 + 8 * entry.getValue().size()) + 1) / 2);
            NodeValue[][] matrix = new NodeValue[length][length];
            int index = 0;
            for (int i = 0; i < length; i++) {
                for (int j = i; j < length; j++) {
//                    SliderRequest sliderRequest = entry.getValue().get(index);
                    //Node node = sliderRequest.getNode1();
                    if (i == j) {
                        //matrix[i][j] = new NodeValue(node,1.0);
                        if(i == 0){
                            matrix[i][j] = new NodeValue(entry.getValue().get(index).getNode1(),1.0);
                        }else{
                            matrix[i][j] = new NodeValue(null,1.0);
                        }

                    } else {
                        SliderRequest sliderRequest = entry.getValue().get(index);
                        double realValue = Math.abs(sliderRequest.getValue()) + 1.0;
                        Node node1 = sliderRequest.getNode1();
                        Node node2 = sliderRequest.getNode2();
                        if (sliderRequest.getValue() < 0) {
                            matrix[i][j] = new NodeValue(node1,realValue);
                            DecimalFormat df = new DecimalFormat("#.###");
                            matrix[j][i] = new NodeValue(node2,Double.parseDouble(df.format(1.0 / realValue)));

                        } else if (sliderRequest.getValue() == 0) {
                            matrix[i][j] = new NodeValue(node1,1.0);
                            matrix[j][i] = new NodeValue(node2,1.0);

                        } else if (sliderRequest.getValue() > 0) {
                            DecimalFormat df = new DecimalFormat("#.###");
                            matrix[i][j] = new NodeValue(node1,Double.parseDouble(df.format(1.0 / realValue)));
                            matrix[j][i] = new NodeValue(node2,realValue);

                        }
                        index++;
                    }

                }
            }
            mapMatrix.put(entry.getKey(), matrix);

        }

        // 输出结果
        for (Map.Entry<String, NodeValue[][]> entry : mapMatrix.entrySet()) {
            System.out.println("Location " + entry.getKey() + " double[][]:");
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

    public Map<String, NodeValue[]> computeValue(Map<String, NodeValue[][]> map) {
        Map<String, NodeValue[]> mapSingleWeight = new HashMap<>();

        for (Map.Entry<String, NodeValue[][]> entry : map.entrySet()) {
            NodeValue[] mVector = new NodeValue[entry.getValue().length];
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
                mVector[i] = new NodeValue(entry.getValue()[i][0].getNode(),result);
            }

            for (int i = 0; i < mVector.length; i++) {
                //mVector[i].setValue(mVector[i].getValue()/sum);
                DecimalFormat df = new DecimalFormat("#.###");
                mVector[i].setValue(Double.parseDouble(df.format(mVector[i].getValue()/sum)));
            }
            mapSingleWeight.put(entry.getKey(),mVector);
        }

        // 输出结果
        for (Map.Entry<String, NodeValue[]> entry : mapSingleWeight.entrySet()) {
            System.out.println("Location " + entry.getKey() + " double[]:");
            //创建节点用来更新singleWeigh

            // node.setLocation(entry.getKey());

            for (int i = 0; i < entry.getValue().length; i++) {
                System.out.print(entry.getValue()[i] + " ");
                //修改节点的单排序权重、指标评价权重
//                if(i==0){
//                    Node node = entry.getValue()[i].getNode();
//                    node.setSingleWeight(1.0);
//                    this.nodeMapper.updateSingleWeight(node);
//                    continue;
//                }
                Node node = entry.getValue()[i].getNode();
                node.setSingleWeight(entry.getValue()[i].getValue());
                 this.nodeMapper.updateSingleWeight(node);
                 this.nodeMapper.updateIndicatorWeight(node);
            }

            //this.nodeMapper.updateNodes(entry.getKey(),entry.getValue());
            System.out.println("----------------------------------------");
        }
        //this.mapSingleWeight = mapSingleWeight;
        return mapSingleWeight;
    }

    public List<Double> check(Map<String,NodeValue[][]> mapMatrix, Map<String,NodeValue[]> mapSingleWeight) {
        List<Double> checkResult = new ArrayList<>();
        for (Map.Entry<String, NodeValue[][]> entry : mapMatrix.entrySet()) {
            NodeValue[] singleweightValue = mapSingleWeight.get(entry.getKey());
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
        return checkResult;

    }

    //得到层次单排序的结果矩阵
    public Map<String, NodeValue[][]> getSingleValues(Map<String, NodeValue[][]> map,Map<String, NodeValue[]> singleMap){
        Map<String, NodeValue[][]> singleValues = new HashMap<>();
        NodeValue[][] singleResultMatrix;
        int index = 0;//checkResult数组的下标，对应的值checkResult[index]表示对应的二维矩阵是否符合
        //一致性检验。

        for (Map.Entry<String, NodeValue[][]> entry : map.entrySet()) {
            singleResultMatrix = new NodeValue[entry.getValue().length][entry.getValue().length+2];
            //和二维数组对应的权重值数组
            NodeValue[] nodeValues = singleMap.get(entry.getKey());
            for (int i = 0; i < entry.getValue().length; i++) {
                for (int j = 0; j < singleResultMatrix[i].length; j++) {
                    //singleResultMatrix[i][j] = entry.getValue()[i][j];
                    if( j<entry.getValue().length){
                        singleResultMatrix[i][j] = entry.getValue()[i][j];
                    }else if( j==entry.getValue().length){
                        singleResultMatrix[i][j] = nodeValues[i];
                    }else if( j==entry.getValue().length+1){
                        singleResultMatrix[i][j] = new NodeValue(null,checkResult.get(index));
                    }
                }

            }
            index++;
            singleValues.put(entry.getKey(),singleResultMatrix);
        }

        for (Map.Entry<String, NodeValue[][]> entry : singleValues.entrySet()) {
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



    //得到全部方案层节点

    //修改全部方案层节点








}
