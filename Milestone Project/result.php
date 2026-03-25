<?php
require_once 'config/database.php';
require_once 'includes/auth.php';
requireLogin();

$examId = (int)($_GET['exam_id'] ?? 0);
$score = (int)($_GET['score'] ?? 0);
$total = (int)($_GET['total'] ?? 0);
$percentage = (float)($_GET['percentage'] ?? 0);
$badge = $_GET['badge'] ?? 'none';

if (!$examId || $total === 0) {
    header('Location: dashboard.php');
    exit;
}

$pdo = getDBConnection();
$stmt = $pdo->prepare("SELECT name FROM exams WHERE id = ?");
$stmt->execute([$examId]);
$exam = $stmt->fetch();

$badgeLabels = [
    'gold' => '🥇 Gold Badge',
    'silver' => '🥈 Silver Badge',
    'bronze' => '🥉 Bronze Badge',
    'none' => 'Keep Practicing!'
];
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Result - Online Exam System</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <header class="header">
        <h1>Exam Result</h1>
        <nav class="header-nav">
            <a href="dashboard.php">Dashboard</a>
            <a href="results_history.php">My Results</a>
        </nav>
    </header>

    <div class="container">
        <div class="result-card">
            <h2 style="margin-bottom: 0.5rem;"><?= htmlspecialchars($exam['name'] ?? 'Exam') ?> - Result</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">You have completed the exam!</p>

            <div class="score-circle <?= $badge ?>">
                <span><?= $score ?> / <?= $total ?></span>
                <span style="font-size: 1.2rem; margin-top: 0.3rem;"><?= $percentage ?>%</span>
            </div>

            <div class="badge-label"><?= $badgeLabels[$badge] ?? $badgeLabels['none'] ?></div>

            <p style="margin-top: 2rem; color: var(--text-muted);">
                <?php if ($badge === 'gold'): ?>
                    Excellent! You've mastered the basics. 🎉
                <?php elseif ($badge === 'silver'): ?>
                    Great job! You're on the right track.
                <?php elseif ($badge === 'bronze'): ?>
                    Good effort! Keep practicing to improve.
                <?php else: ?>
                    Don't give up! Review the material and try again.
                <?php endif; ?>
            </p>

            <div style="margin-top: 2rem;">
                <a href="dashboard.php" class="btn btn-primary">Take Another Exam</a>
            </div>
        </div>
    </div>
</body>
</html>
