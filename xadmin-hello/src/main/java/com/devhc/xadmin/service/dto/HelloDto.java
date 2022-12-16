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
package com.devhc.xadmin.service.dto;

import lombok.Data;
import java.io.Serializable;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
* 
* @description /
* 
* @date 2022-12-15
**/
@Data
public class HelloDto implements Serializable {

    /** ID */
    /** 防止精度丢失 */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /** name */
    private String name;
}
