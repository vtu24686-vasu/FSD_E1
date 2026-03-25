<?php
require_once 'config/database.php';
require_once 'includes/auth.php';
requireLogin();

// Global exam settings (must match exam.php)
$QUESTIONS_PER_EXAM = 10;

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    header('Location: dashboard.php');
    exit;
}

$examId = (int)($_POST['exam_id'] ?? 0);
$examSlug = $_POST['exam_slug'] ?? '';

if (!$examId || !$examSlug) {
    header('Location: dashboard.php');
    exit;
}

$pdo = getDBConnection();

// Get only the questions that were shown in the exam
$stmt = $pdo->prepare("SELECT id, correct_answer FROM questions WHERE exam_id = ? ORDER BY id LIMIT {$QUESTIONS_PER_EXAM}");
$stmt->execute([$examId]);
$questions = $stmt->fetchAll();

$score = 0;
foreach ($questions as $q) {
    $userAnswer = $_POST['q_' . $q['id']] ?? '';
    if ($userAnswer === strtolower($q['correct_answer'])) {
        $score++;
    }
}

$totalQuestions = count($questions);
$percentage = $totalQuestions > 0 ? round(($score / $totalQuestions) * 100, 2) : 0;

// Badge logic: Gold >= 80%, Silver >= 60%, Bronze >= 40%
if ($percentage >= 80) {
    $badge = 'gold';
} elseif ($percentage >= 60) {
    $badge = 'silver';
} elseif ($percentage >= 40) {
    $badge = 'bronze';
} else {
    $badge = 'none';
}

// Save result
$userId = $_SESSION['user_id'];
$stmt = $pdo->prepare("INSERT INTO results (user_id, exam_id, score, total_questions, percentage, badge) VALUES (?, ?, ?, ?, ?, ?)");
$stmt->execute([$userId, $examId, $score, $totalQuestions, $percentage, $badge]);

header("Location: result.php?exam_id=$examId&score=$score&total=$totalQuestions&percentage=$percentage&badge=$badge");
exit;
