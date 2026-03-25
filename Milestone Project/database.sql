-- Dynamic Online Examination & Result Processing System
-- Database Schema for MySQL

CREATE DATABASE IF NOT EXISTS online_exam_db;
USE online_exam_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Exams/Categories table
CREATE TABLE IF NOT EXISTS exams (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    total_questions INT DEFAULT 10,
    time_per_question INT DEFAULT 60,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Questions table
CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    exam_id INT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(500) NOT NULL,
    option_b VARCHAR(500) NOT NULL,
    option_c VARCHAR(500) NOT NULL,
    option_d VARCHAR(500) NOT NULL,
    correct_answer CHAR(1) NOT NULL,
    FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE
);

-- Results table
CREATE TABLE IF NOT EXISTS results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    exam_id INT NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    percentage DECIMAL(5,2) NOT NULL,
    badge VARCHAR(20) NOT NULL,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE
);

-- Insert exam categories (IGNORE skips if already exists)
INSERT IGNORE INTO exams (name, slug, description, total_questions, time_per_question) VALUES
('Python', 'python', 'Basic Python programming fundamentals', 10, 60),
('Java', 'java', 'Introduction to Java programming', 10, 60),
('SQL', 'sql', 'Structured Query Language basics', 10, 60),
('HTML', 'html', 'HTML5 markup fundamentals', 10, 60),
('CSS', 'css', 'Cascading Style Sheets basics', 10, 60),
('JavaScript', 'javascript', 'JavaScript programming essentials', 10, 60),
('Machine Learning', 'machine-learning', 'ML concepts for beginners', 10, 60),
('Gen AI', 'gen-ai', 'Generative AI fundamentals', 10, 60);