package backend.util;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class ExportUtil {

    /**
     * 导出CSV（兼容所有容器，无需额外依赖）
     */
    public static void exportCsv(HttpServletResponse response,
                                 List<String[]> data,
                                 String fileName) throws Exception {
        response.setContentType("text/csv;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".csv");
        PrintWriter out = response.getWriter();
        for (String[] row : data) {
            out.println(String.join(",", row));
        }
        out.flush();
        out.close();
    }
}