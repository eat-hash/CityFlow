package backend.src.util;

import backend.src.entity.TrafficData;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 数据导出工具类
 * 支持将交通数据导出为 CSV 格式文件
 */
public class ExportUtil {

    /**
     * 导出交通数据到 CSV 文件
     * @param dataList 交通数据列表
     * @param filePath 导出文件路径（如：/export/traffic_data.csv）
     * @return 导出是否成功
     */
    public static boolean exportTrafficDataToCSV(List<TrafficData> dataList, String filePath) {
        BufferedWriter bw = null;
        try {
            // 创建文件和父目录
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // 以 UTF-8 编码写入，避免乱码
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

            // 写入 CSV 表头
            bw.write("数据ID,路口ID,采集时间,车道流量数据");
            bw.newLine();

            // 写入数据行
            for (TrafficData data : dataList) {
                String line = String.format("%d,%s,%s,%s",
                        data.getDataId(),
                        data.getIntersectionId(),
                        data.getCollectTime(),
                        data.getLaneFlow());
                bw.write(line);
                bw.newLine();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            // 关闭流资源
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 简化版导出（默认导出到项目根目录下的 export 文件夹）
     * @param dataList 交通数据列表
     * @return 导出文件路径（失败返回 null）
     */
    public static String exportTrafficData(List<TrafficData> dataList) {
        String defaultPath = "export/traffic_" + System.currentTimeMillis() + ".csv";
        boolean success = exportTrafficDataToCSV(dataList, defaultPath);
        return success ? defaultPath : null;
    }
}