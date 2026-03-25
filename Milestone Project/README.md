# Dynamic Online Examination & Result Processing System

A modern web application for conducting online exams with real-time scoring and badge-based results.

## Tech Stack
- **Frontend:** HTML5, CSS3, ES6 JavaScript
- **Backend:** PHP 7.4+
- **Database:** MySQL 5.7+
- **Server:** XAMPP / Apache with PHP

## Features
- User Registration & Login
- 8 Exam Categories: Python, Java, SQL, HTML, CSS, JavaScript, Machine Learning, Gen AI
- 10 questions per exam (MCQ format)
- 1 minute per question timer
- Auto-submit when time expires
- Score badges: Gold (≥80%), Silver (≥60%), Bronze (≥40%)
- Results history
- Modern dark theme UI

## Setup Instructions

### 1. Install XAMPP
- Download and install [XAMPP](https://www.apachefriends.org/)
- Start **Apache** and **MySQL** from XAMPP Control Panel

### 2. Create Database
1. Open phpMyAdmin: http://localhost/phpmyadmin
2. Create a new database or run the SQL files:
   - Import `database.sql` (creates database and tables)
   - Import `database_questions.sql` (adds exam questions)

Or via command line:
```bash
mysql -u root -p < database.sql
mysql -u root -p < database_questions.sql
```

### 3. Deploy Project
1. Copy the project folder to `C:\xampp\htdocs\` (or your web root)
2. Rename folder to `online-exam` (optional)
3. Access: http://localhost/online-exam/

### 4. Database Config
Edit `config/database.php` if needed:
- `DB_HOST`: localhost
- `DB_NAME`: online_exam_db
- `DB_USER`: root
- `DB_PASS`: (empty for default XAMPP)

## Pages
- **index.php** - Redirects to login/dashboard
- **register.php** - User registration
- **login.php** - User login
- **dashboard.php** - Exam selection
- **exam.php** - Take exam (with timer)
- **submit.php** - Process submission
- **result.php** - View score and badge
- **results_history.php** - Past exam results

## Badge System
| Score | Badge |
|-------|-------|
| 80%+ | Gold |
| 60-79% | Silver |
| 40-59% | Bronze |
| Below 40% | Keep Practicing |
