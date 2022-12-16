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
package com.devhc.xadmin.rest;

import com.devhc.xadmin.annotation.Log;
import com.devhc.xadmin.domain.Hello;
import com.devhc.xadmin.service.HelloService;
import com.devhc.xadmin.service.dto.HelloQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* 
* 
* @date 2022-12-15
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "Hello管理")
@RequestMapping("/api/hello")
public class HelloController {

    private final HelloService helloService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('hello:list')")
    public void exportHello(HttpServletResponse response, HelloQueryCriteria criteria) throws IOException {
        helloService.download(helloService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Hello")
    @ApiOperation("查询Hello")
    @PreAuthorize("@el.check('hello:list')")
    public ResponseEntity<Object> queryHello(HelloQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(helloService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Hello")
    @ApiOperation("新增Hello")
    @PreAuthorize("@el.check('hello:add')")
    public ResponseEntity<Object> createHello(@Validated @RequestBody Hello resources){
        return new ResponseEntity<>(helloService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Hello")
    @ApiOperation("修改Hello")
    @PreAuthorize("@el.check('hello:edit')")
    public ResponseEntity<Object> updateHello(@Validated @RequestBody Hello resources){
        helloService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除Hello")
    @ApiOperation("删除Hello")
    @PreAuthorize("@el.check('hello:del')")
    public ResponseEntity<Object> deleteHello(@RequestBody Long[] ids) {
        helloService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
