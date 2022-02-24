package com.liao47.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liaoshiqing
 * @date 2022/2/23 16:54
 */
@Data
@AllArgsConstructor
public class GachaType implements Serializable {
    @Excel(name = "id", width = 5)
    private String id;

    @Excel(name = "key", orderNum = "1", width = 5)
    private String key;

    @Excel(name = "名称", orderNum = "2", width = 15)
    private String name;
}
