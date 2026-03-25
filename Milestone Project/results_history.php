<?php
require_once 'config/database.php';
require_once 'includes/auth.php';
requireLogin();

$user = getUser();
$pdo = getDBConnection();

$stmt = $pdo->prepare("
    SELECT r.*, e.name as exam_name 
    FROM results r 
    JOIN exams e ON r.exam_id = e.id 
    WHERE r.user_id = ? 
    ORDER BY r.completed_at DESC
");
$stmt->execute([$user['id']]);
$results = $stmt->fetchAll();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Results - Online Exam System</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <header class="header">
        <h1>My Exam Results</h1>
        <nav class="header-nav">
            <a href="dashboard.php">Dashboard</a>
            <a href="logout.php">Logout</a>
        </nav>
    </header>

    <div class="container">
        <h2 style="margin-bottom: 1rem;">Your Exam History</h2>
        
        <?php if (empty($results)): ?>
            <p style="color: var(--text-muted);">You haven't taken any exams yet. <a href="dashboard.php" style="color: var(--accent-primary);">Start your first exam</a>!</p>
        <?php else: ?>
            <div class="dashboard-grid" style="margin-top: 1rem;">
                <?php foreach ($results as $r): ?>
                    <div class="exam-card" style="cursor: default;">
                        <div class="exam-icon" style="font-size: 1.2rem;">
                            <?php
                            $badgeIcons = ['gold' => '🥇', 'silver' => '🥈', 'bronze' => '🥉', 'none' => '📋'];
                            echo $badgeIcons[$r['badge']] ?? '📋';
                            ?>
                        </div>
                        <h3><?= htmlspecialchars($r['exam_name']) ?></h3>
                        <p><strong><?= $r['score'] ?> / <?= $r['total_questions'] ?></strong> (<?= $r['percentage'] ?>%)</p>
                        <div class="exam-meta">
                            <span><?= ucfirst($r['badge']) ?> Badge</span>
                            <span><?= date('M j, Y', strtotime($r['completed_at'])) ?></span>
                        </div>
                    </div>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>
    </div>
</body>
</html>
