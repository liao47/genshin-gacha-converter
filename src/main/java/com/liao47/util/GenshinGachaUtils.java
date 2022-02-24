package com.liao47.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liao47.constant.Constants;
import com.liao47.dto.GachaItem;
import com.liao47.dto.GachaType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author liaoshiqing
 * @date 2022/2/23 16:32
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenshinGachaUtils {

    private static final List<GachaType> GACHA_TYPES = Arrays.asList(
            new GachaType("15", "301", "角色活动祈愿"),
            new GachaType("16", "302", "武器活动祈愿"),
            new GachaType("4", "200", "常驻祈愿"),
            new GachaType("14", "100", "新手祈愿"));

    /**
     * excel转json
     * @param excelPath
     * @param jsonExportDir
     * @param uid
     */
    public static void excelToJson(String excelPath, String jsonExportDir, String uid) {
        JSONObject json = new JSONObject();
        json.put(Constants.GACHA_TYPE, GACHA_TYPES);
        json.put(Constants.UID, uid);
        JSONObject gachaLog = new JSONObject();
        json.put(Constants.GACHA_LOG, gachaLog);

        File file = new File(excelPath);
        ImportParams importParams = new ImportParams();
        for (int i = 0; i < GACHA_TYPES.size(); i++) {
            importParams.setStartSheetIndex(i);
            ExcelImportResult<GachaItem> result = ExcelImportUtil.importExcelMore(file, GachaItem.class, importParams);
            GachaType gachaType = null;
            for (GachaType type : GACHA_TYPES) {
                if (type.getName().equals(result.getWorkbook().getSheetName(i))) {
                    gachaType = type;
                    break;
                }
            }
            if (gachaType == null) {
                System.out.println("无法识别sheet:" + result.getWorkbook().getSheetName(i));
                continue;
            }
            List<GachaItem> list = result.getList();
            if (CollectionUtils.isNotEmpty(list)) {
                for (GachaItem t : list) {
                    t.setUid(uid);
                    t.setGachaType(gachaType.getKey());
                    t.setItemId("");
                    t.setCount("1");
                    t.setLang("zh-cn");
                }
                gachaLog.put(gachaType.getKey(), list);
            }
        }

        exportJson(json, jsonExportDir, uid);
    }

    /**
     * json转excel
     * @param jsonPath
     * @param exportDir
     */
    public static void jsonToExcel(String jsonPath, String exportDir) {
        JSONObject gachaLog;
        try (FileInputStream fis = new FileInputStream(jsonPath)) {
            JSONObject jsonObject = JSON.parseObject(fis, JSONObject.class);
            gachaLog = jsonObject.getJSONObject(Constants.GACHA_LOG);
            if (gachaLog == null) {
                System.out.println("没有识别到抽卡记录");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        List<Map<String, Object>> sheetsList = new ArrayList<>();
        for (GachaType gachaType : GACHA_TYPES) {
            JSONArray array = gachaLog.getJSONArray(gachaType.getKey());
            if (CollectionUtils.isEmpty(array)) {
                continue;
            }

            ExportParams exportParams = new ExportParams();
            exportParams.setSheetName(gachaType.getName());
            Map<String, Object> map = new HashMap<>(3);
            map.put("title", exportParams);
            map.put("entity", GachaItem.class);
            map.put("data", array.toJavaList(GachaItem.class));
            sheetsList.add(map);
        }

        String path = exportDir + File.separator + String.format("gachaExport-%s.xlsx",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DATETIME_PATTERN)));
        try (Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);
             FileOutputStream fos = new FileOutputStream(path)) {
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出json
     * @param json
     * @param jsonExportDir
     * @param uid
     */
    private static void exportJson(JSONObject json, String jsonExportDir, String uid) {
        String fileName = jsonExportDir + File.separator + String.format("gachaData-%s.json", uid);
        try {
            File exportDir = new File(jsonExportDir);
            if (!exportDir.exists() && !exportDir.mkdirs()) {
                System.out.println("mkdirs jsonExportDir fail");
            }
            File exportFile = new File(fileName);
            if (!exportFile.exists() && !exportFile.createNewFile()) {
                System.out.println("create new file fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(JSON.toJSONString(json, true).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
