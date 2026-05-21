# 交通信号配时优化算法设计文档
## 一、算法名称
基于流量绿信比分配的单路口信号配时优化算法

## 二、输入数据
1. roadnet_4_4.json：路口拓扑、相位结构
2. anon_xxx.json：杭州真实交通流数据

## 三、输入格式
- intersectionId：路口ID
- phaseFlow：四相位车流量
- minGreen：最小绿灯
- maxCycle：最大周期

## 四、输出格式
{
"intersection_id": "路口ID",
"cycle_length": 周期,
"yellow_time": 黄灯时长,
"timing_plan": 各相位绿灯时长,
"average_delay": 平均延误,
"capacity": 通行能力
}

## 五、算法步骤
1. 读取真实JSON路网与流量数据
2. 按流量占比分配绿灯
3. 约束最小绿灯与最大周期
4. 输出配时方案与评价指标