package com.liao47.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liaoshiqing
 * @date 2022/2/23 16:24
 */
@Data
public class GachaItem implements Serializable {
    private String uid;

    @JSONField(name = "gacha_type")
    private String gachaType;

    @JSONField(name = "item_id")
    private String itemId;

    private String count;

    @Excel(name = "时间", width = 22)
    private String time;

    @Excel(name = "名称", orderNum = "1", width = 18)
    private String name;

    private String lang;

    @JSONField(name = "item_type")
    @Excel(name = "类别", orderNum = "2", width = 8)
    private String itemType;

    @JSONField(name = "rank_type")
    @Excel(name = "星级", orderNum = "3", width = 8)
    private String rankType;

    private String id;
}
