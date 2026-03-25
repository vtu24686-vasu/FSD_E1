<?php
require_once 'config/database.php';
require_once 'includes/auth.php';
requireLogin();

// Global exam settings
$QUESTIONS_PER_EXAM = 10;
$TIME_PER_QUESTION = 25; // seconds per question

$examSlug = $_GET['exam'] ?? '';
if (empty($examSlug)) {
    header('Location: dashboard.php');
    exit;
}

$pdo = getDBConnection();
$stmt = $pdo->prepare("SELECT * FROM exams WHERE slug = ?");
$stmt->execute([$examSlug]);
$exam = $stmt->fetch();

if (!$exam) {
    header('Location: dashboard.php');
    exit;
}

// Fetch only the configured number of questions for this exam
$stmt = $pdo->prepare("SELECT * FROM questions WHERE exam_id = ? ORDER BY id LIMIT {$QUESTIONS_PER_EXAM}");
$stmt->execute([$exam['id']]);
$questions = $stmt->fetchAll();

if (count($questions) < 1) {
    die("No questions available for this exam. Please contact admin.");
}

// Total exam time = questions * time per question
$totalTime = count($questions) * $TIME_PER_QUESTION; // seconds
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?= htmlspecialchars($exam['name']) ?> Exam - Online Exam System</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <header class="header">
        <h1><?= htmlspecialchars($exam['name']) ?> Exam</h1>
        <nav class="header-nav">
            <span class="timer" id="timer"><?= gmdate("i:s", $totalTime) ?></span>
        </nav>
    </header>

    <div class="container">
        <div class="exam-header">
            <span>Question <span id="current-q">1</span> of <?= count($questions) ?></span>
            <span class="timer" id="timer-display"><?= gmdate("i:s", $totalTime) ?></span>
        </div>

        <div class="question-nav" id="question-nav">
            <?php for ($i = 1; $i <= count($questions); $i++): ?>
                <button type="button" class="question-nav-btn <?= $i === 1 ? 'active' : '' ?>" data-q="<?= $i ?>"><?= $i ?></button>
            <?php endfor; ?>
        </div>

        <form id="exam-form" action="submit.php" method="POST">
            <input type="hidden" name="exam_id" value="<?= $exam['id'] ?>">
            <input type="hidden" name="exam_slug" value="<?= htmlspecialchars($exam['slug']) ?>">
            
            <?php foreach ($questions as $i => $q): ?>
                <div class="question-card" data-question="<?= $i + 1 ?>" <?= $i > 0 ? 'style="display:none"' : '' ?>>
                    <div class="question-number">Question <?= $i + 1 ?></div>
                    <div class="question-text"><?= htmlspecialchars($q['question_text']) ?></div>
                    <ul class="options-list">
                        <?php foreach (['a' => 'A', 'b' => 'B', 'c' => 'C', 'd' => 'D'] as $key => $label): ?>
                            <li class="option-item" data-value="<?= $key ?>">
                                <span class="option-radio"></span>
                                <span><?= htmlspecialchars($q['option_' . $key]) ?></span>
                                <input type="radio" name="q_<?= $q['id'] ?>" value="<?= $key ?>" style="display:none">
                            </li>
                        <?php endforeach; ?>
                    </ul>
                </div>
            <?php endforeach; ?>

            <div style="margin-top: 2rem; display: flex; gap: 1rem; justify-content: space-between;">
                <button type="button" class="btn btn-secondary" id="prev-btn" style="visibility: hidden;">← Previous</button>
                <button type="button" class="btn btn-secondary" id="next-btn">Next →</button>
                <button type="submit" class="btn btn-primary" id="submit-btn" style="display: none;">Submit Exam</button>
            </div>
        </form>
    </div>

    <script>
        const totalTime = <?= $totalTime ?>;
        const totalQuestions = <?= count($questions) ?>;
        let timeLeft = totalTime;
        let currentQuestion = 1;

        const timerEl = document.getElementById('timer-display');
        const currentQEl = document.getElementById('current-q');
        const questionCards = document.querySelectorAll('.question-card');
        const prevBtn = document.getElementById('prev-btn');
        const nextBtn = document.getElementById('next-btn');
        const submitBtn = document.getElementById('submit-btn');

        // Timer
        const timerInterval = setInterval(() => {
            timeLeft--;
            const mins = Math.floor(timeLeft / 60);
            const secs = timeLeft % 60;
            timerEl.textContent = `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
            timerEl.className = 'timer' + (timeLeft <= 60 ? ' danger' : timeLeft <= 180 ? ' warning' : '');
            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                document.getElementById('exam-form').submit();
            }
        }, 1000);

        // Option selection
        document.querySelectorAll('.option-item').forEach(item => {
            item.addEventListener('click', function() {
                const card = this.closest('.question-card');
                card.querySelectorAll('.option-item').forEach(o => o.classList.remove('selected'));
                this.classList.add('selected');
                this.querySelector('input[type="radio"]').checked = true;
            });
        });

        // Navigation
        function showQuestion(n) {
            questionCards.forEach((card, i) => {
                card.style.display = (i + 1 === n) ? 'block' : 'none';
            });
            currentQuestion = n;
            currentQEl.textContent = n;
            prevBtn.style.visibility = n === 1 ? 'hidden' : 'visible';
            nextBtn.style.display = n === totalQuestions ? 'none' : 'inline-block';
            submitBtn.style.display = n === totalQuestions ? 'inline-block' : 'none';
            document.querySelectorAll('.question-nav-btn').forEach((btn, i) => {
                btn.classList.toggle('active', i + 1 === n);
                const hasAnswer = cardHasAnswer(i + 1);
                btn.classList.toggle('answered', hasAnswer);
            });
        }

        function cardHasAnswer(qNum) {
            const card = questionCards[qNum - 1];
            return card && card.querySelector('input[type="radio"]:checked');
        }

        document.querySelectorAll('.question-nav-btn').forEach(btn => {
            btn.addEventListener('click', () => showQuestion(parseInt(btn.dataset.q)));
        });

        prevBtn.addEventListener('click', () => showQuestion(currentQuestion - 1));
        nextBtn.addEventListener('click', () => showQuestion(currentQuestion + 1));

        document.getElementById('exam-form').addEventListener('change', () => {
            document.querySelectorAll('.question-nav-btn').forEach((btn, i) => {
                btn.classList.toggle('answered', cardHasAnswer(i + 1));
            });
        });
    </script>
</body>
</html>
