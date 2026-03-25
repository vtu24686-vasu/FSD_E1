<?php
require_once 'config/database.php';
require_once 'includes/auth.php';
requireLogin();

$user = getUser();
$pdo = getDBConnection();
$stmt = $pdo->query("SELECT * FROM exams ORDER BY name");
$exams = $stmt->fetchAll();

$examIcons = [
    'python' => '🐍',
    'java' => '☕',
    'sql' => '🗄️',
    'html' => '📄',
    'css' => '🎨',
    'javascript' => '⚡',
    'machine-learning' => '🤖',
    'gen-ai' => '✨'
];
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Online Exam System</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <header class="header">
        <h1>📚 Dynamic Online Exam System</h1>
        <nav class="header-nav">
            <span style="color: var(--accent-primary);"><?= htmlspecialchars($user['name']) ?></span>
            <a href="results_history.php">My Results</a>
            <a href="logout.php">Logout</a>
        </nav>
    </header>

    <div class="container">
        <h2 style="margin-bottom: 0.5rem;">Choose Your Exam</h2>
        <p style="color: var(--text-muted); margin-bottom: 2rem;">Select an exam to test your knowledge. Each exam has 10 questions with 25 seconds per question.</p>

        <div class="dashboard-grid">
            <?php foreach ($exams as $exam): ?>
                <a href="exam.php?exam=<?= urlencode($exam['slug']) ?>" class="exam-card">
                    <div class="exam-icon"><?= $examIcons[$exam['slug']] ?? '📝' ?></div>
                    <h3><?= htmlspecialchars($exam['name']) ?></h3>
                    <p><?= htmlspecialchars($exam['description']) ?></p>
                    <div class="exam-meta">
                        <span>📋 10 Questions</span>
                        <span>⏱️ 25s each</span>
                    </div>
                </a>
            <?php endforeach; ?>
        </div>
    </div>
</body>
</html>
