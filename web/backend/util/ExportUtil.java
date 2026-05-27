package backend.util;

import com.alibaba.excel.EasyExcel;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class ExportUtil {

    /**
     * 通用Excel导出（适配配时方案、统计数据等）
     * @param response 响应
     * @param data 数据列表
     * @param headClazz 表头实体
     * @param fileName 文件名（不含后缀）
     * @param sheetName sheet名
     */
    public static void exportExcel(HttpServletResponse response,
                                   List<?> data,
                                   Class<?> headClazz,
                                   String fileName,
                                   String sheetName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String encodedName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), headClazz)
                .sheet(sheetName)
                .doWrite(data);
    }
}