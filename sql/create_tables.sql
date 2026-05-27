-- 创建数据库
CREATE DATABASE IF NOT EXISTS traffic_db;
USE traffic_db;

-- 路口表
CREATE TABLE IF NOT EXISTS intersection (
    intersection_id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50),
    region VARCHAR(100),
    lane_count INT
);

-- 交通数据表
CREATE TABLE IF NOT EXISTS traffic_data (
    data_id INT AUTO_INCREMENT PRIMARY KEY,
    intersection_id VARCHAR(32) NOT NULL,
    collect_time DATETIME NOT NULL,
    lane_flow VARCHAR(255),
    FOREIGN KEY (intersection_id) REFERENCES intersection(intersection_id)
);

-- 配时方案表
CREATE TABLE IF NOT EXISTS timing_scheme (
    scheme_id INT AUTO_INCREMENT PRIMARY KEY,
    intersection_id VARCHAR(32) NOT NULL,
    cycle_length INT,
    phase_green VARCHAR(255),
    optimize_effect VARCHAR(255),
    FOREIGN KEY (intersection_id) REFERENCES intersection(intersection_id)
);

-- 配时方案表
CREATE TABLE timing_plan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    intersection_id INT NOT NULL,
    phase_count INT NOT NULL,
    cycle INT NOT NULL,
    min_green INT DEFAULT 15,
    max_cycle INT DEFAULT 120,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_intersection (intersection_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 统计相关表索引（提升查询速度）
CREATE INDEX idx_device_status ON device(status);
CREATE INDEX idx_traffic_create_time ON traffic_record(create_time);