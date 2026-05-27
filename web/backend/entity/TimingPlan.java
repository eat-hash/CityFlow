package backend.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class TimingPlan {
    private Integer id;

    @ExcelProperty("方案名称")
    private String name;

    @ExcelProperty("路口ID")
    private Integer intersectionId;

    @ExcelProperty("相位数")
    private Integer phaseCount;

    @ExcelProperty("周期(s)")
    private Integer cycle;

    @ExcelProperty("最小绿灯(s)")
    private Integer minGreen;

    @ExcelProperty("最大周期(s)")
    private Integer maxCycle;

    @ExcelProperty("状态(0禁用/1启用)")
    private Integer status;

    @ExcelProperty("创建时间")
    private Date createTime;
}