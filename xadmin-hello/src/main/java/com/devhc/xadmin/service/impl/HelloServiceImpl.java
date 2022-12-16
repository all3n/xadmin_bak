/*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package com.devhc.xadmin.service.impl;

import com.devhc.xadmin.domain.Hello;
import com.devhc.xadmin.utils.ValidationUtil;
import com.devhc.xadmin.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.devhc.xadmin.repository.HelloRepository;
import com.devhc.xadmin.service.HelloService;
import com.devhc.xadmin.service.dto.HelloDto;
import com.devhc.xadmin.service.dto.HelloQueryCriteria;
import com.devhc.xadmin.service.mapstruct.HelloMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.devhc.xadmin.utils.PageUtil;
import com.devhc.xadmin.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* 
* @description 服务实现
* 
* @date 2022-12-15
**/
@Service
@RequiredArgsConstructor
public class HelloServiceImpl implements HelloService {

    private final HelloRepository helloRepository;
    private final HelloMapper helloMapper;

    @Override
    public Map<String,Object> queryAll(HelloQueryCriteria criteria, Pageable pageable){
        Page<Hello> page = helloRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(helloMapper::toDto));
    }

    @Override
    public List<HelloDto> queryAll(HelloQueryCriteria criteria){
        return helloMapper.toDto(helloRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public HelloDto findById(Long id) {
        Hello hello = helloRepository.findById(id).orElseGet(Hello::new);
        ValidationUtil.isNull(hello.getId(),"Hello","id",id);
        return helloMapper.toDto(hello);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HelloDto create(Hello resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        return helloMapper.toDto(helloRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Hello resources) {
        Hello hello = helloRepository.findById(resources.getId()).orElseGet(Hello::new);
        ValidationUtil.isNull( hello.getId(),"Hello","id",resources.getId());
        hello.copy(resources);
        helloRepository.save(hello);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            helloRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<HelloDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HelloDto hello : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("name", hello.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
