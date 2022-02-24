import com.alibaba.fastjson.JSON;
import com.liao47.dto.GachaItem;
import com.liao47.util.GenshinGachaUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author liaoshiqing
 * @date 2022/2/23 17:02
 */
class GenshinGachaUtilsTest {
    @Test
    void excelToJsonTest() {
        GenshinGachaUtils.excelToJson("C:\\work\\export\\genshin\\gachaExport-20220224171923.xlsx",
                "C:\\work\\export\\genshin",
                "2333");
    }

    @Test
    void jsonToExcelTest() {
        GenshinGachaUtils.jsonToExcel("C:\\work\\export\\genshin\\gachaData-2333.json",
                "C:\\work\\export\\genshin\\");
    }
}
