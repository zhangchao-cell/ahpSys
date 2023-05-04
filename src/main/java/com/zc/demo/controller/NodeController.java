package com.zc.demo.controller;

import com.zc.demo.common.NodeValue;
import com.zc.demo.common.SliderRequest;
import com.zc.demo.entity.Node;
import com.zc.demo.entity.Scheme;
import com.zc.demo.mapper.NodeMapper;
import com.zc.demo.mapper.SchemeMapper;
import com.zc.demo.service.NodeService;
import com.zc.demo.service.SchemeService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private NodeService nodeService;
    @Autowired
    private NodeMapper nodeMapper;




    @PostMapping("/save")
    public Integer saveNodes(@RequestBody List<Node> nodes){
        //nodeMapper.delAll();
        int sum = 0,num;
        for (int i = 0; i < nodes.size(); i++) {
            num = nodeMapper.insertNode(nodes.get(i));
            sum += num;
        }
        return sum;
    }

    @PostMapping("/saveJsNodes")
    public Integer saveJsNodes(@RequestBody List<Node> nodes){
        //nodeMapper.delAll();
        int sum = 0,num,model_id;
        model_id = nodes.get(0).getModelId();
        for (int i = 0; i < nodes.size(); i++) {
            System.out.println(nodes.get(i));
            if(i==0){
                nodes.get(i).setSingleWeight(1.0);
                nodes.get(i).setIndicatorWeight(1.0);
            }
            num = nodeMapper.insertJsNode(nodes.get(i));
            //从根节点的下一个节点开始，更新node的parentid，使其和id对应起来
//            if(i>0){
//                System.out.println("i>0");
//                nodeMapper.updateJsNodeParentId(nodes.get(i));
//            }

            sum += num;
        }
        //从根节点的下一个节点开始，更新node的parentid，使其和id对应起来
        List<Node> nodeList = nodeMapper.selectNodeByModelId(model_id);
        for (int i = 1; i < nodeList.size(); i++) {
                System.out.println("i>0");
            System.out.println(nodeList.get(i));
                nodeMapper.updateJsNodeParentId(nodeList.get(i),nodes.get(i).getParentid());
        }
        //遍历更新记录的isLeaf字段
        List<Node> nodeList2 = nodeMapper.selectNodeByModelId(model_id);
        for (int i = 1; i < nodeList2.size(); i++) {

            System.out.println(nodeList2.get(i));
            nodeMapper.updateIsLeafByModelId(model_id);
        }

        return sum;
    }




    @PostMapping("/sliderValues")
    public Map<String, NodeValue[][]> getSliderValues(@RequestBody List<SliderRequest> sliderRequests){
        for (SliderRequest s:
                sliderRequests) {
            System.out.println(s);
        }
        Map<String, List<SliderRequest>> map = new HashMap<>();
        for (SliderRequest sliderRequest : sliderRequests) {
            //返回key中key对应的value，如果为空，则默认为新建一个new ArrayList<>()
            //这样就可以实现，按照数组元素的location属性进行分割，把相同的分到一个arraylist里面
            //！！！！！！！！！！！！！！开心(*￣︶￣)
            List<SliderRequest> list = map.getOrDefault(sliderRequest.getLocation(), new ArrayList<>());
            list.add(sliderRequest);
            map.put(sliderRequest.getLocation(), list);
        }

        // 输出结果
        for (Map.Entry<String, List<SliderRequest>> entry : map.entrySet()) {
            System.out.println("Location " + entry.getKey() + " List:");
            for (SliderRequest sliderRequest : entry.getValue()) {
                System.out.println(sliderRequest.toString());
            }
            System.out.println("----------------------------------------");
        }
        //构建层次单排序矩阵
        //1.得到每一个层次对应的二维数组，存放到map中
        Map<String, NodeValue[][]> mapMatrix = nodeService.buildMatrix(map);
        //2.得到每一个层次中每一个指标对应的权重，存放到map中
        Map<String, NodeValue[]> mapSingleWeight = nodeService.computeValue(mapMatrix);
        //3.一致性检验，得到每个二维数组是否通过一致性检验的一维集合List，0为未通过，1位通过
        List<Double> checkResult = nodeService.check(mapMatrix,mapSingleWeight);
        //4. 得到层次单排序的结果矩阵，展示到前端
        Map<String, NodeValue[][]> singleValues = nodeService.getSingleValues(mapMatrix,mapSingleWeight);

        // 输出结果
//        for (Map.Entry<String, double[][]> entry : singleValues.entrySet()) {
//            System.out.println("Location " + entry.getKey() + " singleValues:");
//            for (int i = 0; i < entry.getValue().length; i++) {
//                for (int j = 0; j < entry.getValue().length; j++) {
//                    System.out.print(entry.getValue()[i][j] + " ");
//                }
//                System.out.println("\n");
//            }
//            System.out.println("----------------------------------------");
//        }

//        Map<String,Map<String,SliderRequest>> map1 = new HashMap<>();
//        Map<String,SliderRequest> map2 = new HashMap<>();
//
//        int index = -1;
//        for (int i = 0; i < requests.size(); i++) {
//            if(requests.get(i).getValue() == 9){
//                index = i;
//                break;
//            }
//            map2.put(requests.get(i).getId1()+"+"+requests.get(i).getId2(),requests.get(i));
//        }
//        map1.put(requests.get(index-1).getLocation(),map2);

//        System.out.println("------map2------");
//        for (SliderRequest s:
//        map2.values()) {
//            System.out.println(s);
//        }
//        System.out.println("------map1------");
//        for (int i = 0; i < map1.size(); i++) {
//            for (int j = 0; j < map1.get(i).size(); j++) {
//                System.out.println(map1.get(i).get(j));
//            }
//            System.out.println(map2.get(i));
//        }
//        JSON.toJSONString("开心就好~~");
        return singleValues;
    }



    @PostMapping("/update")
    public Integer updateNodes(@RequestBody List<Node> nodes){
        int sum = 0,num;
        for (int i = 0; i < nodes.size(); i++) {
            num = nodeMapper.updateNode(nodes.get(i));
            sum += num;
        }
        return sum;
    }

//    @PostMapping("/save")
//    public Integer saveNode(@RequestBody Node node){
//        return nodeMapper.insertNode(node);
//    }

    @GetMapping("/")
    public List<Node> selectAll(){
        return nodeMapper.selectAll();
    }

    @GetMapping("/selectByModelId")
    public List<Node> selectByModelId(@RequestParam Integer modelId){
        return nodeMapper.selectNodeByModelId(modelId);
    }

    @GetMapping("/leaf")
    public List<Node> selectLeafNode(@RequestParam Integer modelId){
        return nodeMapper.selectLeafNodeByModelId(modelId);
    }

    @DeleteMapping("/del")
    public boolean delById(@RequestParam String nodeId){
        return nodeMapper.delById(nodeId);
    }








}
