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