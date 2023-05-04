package com.zc.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.demo.entity.Model;
import com.zc.demo.entity.Scheme;
import com.zc.demo.mapper.ModelMapper;
import com.zc.demo.mapper.SchemeMapper;
import org.springframework.stereotype.Service;

@Service
public class ModelService extends ServiceImpl<ModelMapper, Model> {
}
