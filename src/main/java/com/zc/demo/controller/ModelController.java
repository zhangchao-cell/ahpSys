package com.zc.demo.controller;

import com.zc.demo.entity.Model;
import com.zc.demo.entity.Scheme;
import com.zc.demo.mapper.ModelMapper;
import com.zc.demo.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/model")
public class ModelController {
    @Autowired
    ModelService modelService;

    @Autowired
    ModelMapper modelMapper;

    //方案层
    @GetMapping
    public List<Model> selectAllScheme(){
        return modelService.list();
    }

    @GetMapping("/selectByUserId")
    public List<Model> selectModelsByUserId(@RequestParam Integer userId){
        return modelMapper.selectByUserId(userId);
    }


    @PostMapping("/saveModel")
    public Integer saveModel(@RequestBody Model model){
            return modelMapper.insertModel(model);
    }
}
