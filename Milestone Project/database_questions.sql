-- Insert Questions for All Exams
-- Run this AFTER database.sql

USE online_exam_db;

-- Python Questions (exam_id = 1)
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(1, 'What keyword is used to define a function in Python?', 'func', 'def', 'function', 'define', 'B'),
(1, 'Which of the following is the correct way to create a list in Python?', 'list = ()', 'list = []', 'list = {}', 'list = <>', 'B'),
(1, 'What is the output of print(2 ** 3)?', '6', '8', '9', '5', 'B'),
(1, 'Which symbol is used for single-line comments in Python?', '//', '#', '/*', '--', 'B'),
(1, 'What does the len() function do?', 'Returns the type of object', 'Returns the length of an object', 'Returns the last element', 'Returns the first element', 'B'),
(1, 'Which data type is used to store text in Python?', 'txt', 'string', 'str', 'text', 'C'),
(1, 'What is the correct way to check if a key exists in a dictionary?', 'key in dict', 'dict.has(key)', 'dict.contains(key)', 'key exists dict', 'A'),
(1, 'Which loop is used to iterate over a sequence in Python?', 'repeat', 'foreach', 'for', 'while', 'C'),
(1, 'What does the range(5) function return?', '0, 1, 2, 3, 4', '1, 2, 3, 4, 5', '0, 1, 2, 3, 4, 5', '1, 2, 3, 4', 'A'),
(1, 'Which method is used to add an item to the end of a list?', 'append()', 'add()', 'insert()', 'push()', 'A');

-- Java Questions (exam_id = 2)
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(2, 'What is the correct extension for a Java source file?', '.jav', '.java', '.js', '.class', 'B'),
(2, 'Which keyword is used to create a class in Java?', 'class', 'struct', 'create', 'new', 'A'),
(2, 'What is the default value of a boolean variable in Java?', 'true', 'false', '0', 'null', 'B'),
(2, 'Which method is the entry point of a Java program?', 'start()', 'main()', 'run()', 'execute()', 'B'),
(2, 'What does the "public" keyword mean in Java?', 'Private access', 'Public access from anywhere', 'Protected access', 'Default access', 'B'),
(2, 'Which data type is used to store whole numbers in Java?', 'float', 'double', 'int', 'decimal', 'C'),
(2, 'What is the correct way to create an object in Java?', 'Object obj = new Object()', 'Object obj = Object()', 'new Object obj', 'Object obj', 'A'),
(2, 'Which loop executes at least once?', 'for', 'while', 'do-while', 'foreach', 'C'),
(2, 'What does System.out.println() do?', 'Reads input', 'Prints output to console', 'Creates a file', 'Opens a window', 'B'),
(2, 'Which keyword is used for inheritance in Java?', 'inherits', 'extends', 'implements', 'super', 'B');

-- SQL Questions (exam_id = 3)
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(3, 'What does SQL stand for?', 'Structured Query Language', 'Simple Query Language', 'Standard Query Language', 'Structured Question Language', 'A'),
(3, 'Which keyword is used to retrieve data from a database?', 'GET', 'FETCH', 'SELECT', 'RETRIEVE', 'C'),
(3, 'Which clause is used to filter records in SQL?', 'FILTER', 'WHERE', 'CONDITION', 'IF', 'B'),
(3, 'Which keyword is used to sort the result set?', 'SORT', 'ORDER BY', 'ARRANGE', 'SORT BY', 'B'),
(3, 'What does the INSERT statement do?', 'Updates records', 'Deletes records', 'Adds new records', 'Retrieves records', 'C'),
(3, 'Which function returns the number of rows?', 'COUNT()', 'SUM()', 'TOTAL()', 'NUMBER()', 'A'),
(3, 'What does the UPDATE statement do?', 'Creates tables', 'Modifies existing records', 'Deletes records', 'Adds records', 'B'),
(3, 'Which keyword is used to combine rows from two tables?', 'MERGE', 'JOIN', 'COMBINE', 'UNION', 'B'),
(3, 'What does DELETE FROM table_name do?', 'Deletes the table', 'Removes all rows', 'Deletes one row', 'Clears structure', 'B'),
(3, 'Which data type stores whole numbers?', 'VARCHAR', 'INT', 'TEXT', 'CHAR', 'B');

-- HTML Questions (exam_id = 4)
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(4, 'What does HTML stand for?', 'Hyper Text Markup Language', 'High Tech Markup Language', 'Home Tool Markup Language', 'Hyperlink Text Markup Language', 'A'),
(4, 'Which tag is used to create a heading?', '<head>', '<h1>', '<heading>', '<title>', 'B'),
(4, 'Which attribute is used to add a link in HTML?', 'src', 'href', 'link', 'url', 'B'),
(4, 'Which tag creates a line break?', '<br>', '<lb>', '<break>', '<newline>', 'A'),
(4, 'Which tag is used for the largest heading?', '<h6>', '<h1>', '<head>', '<heading>', 'B'),
(4, 'Which tag defines a paragraph?', '<para>', '<p>', '<paragraph>', '<text>', 'B'),
(4, 'Which tag is used to create an unordered list?', '<ol>', '<list>', '<ul>', '<li>', 'C'),
(4, 'Which attribute specifies an image source?', 'link', 'src', 'img', 'source', 'B'),
(4, 'Which tag creates a table row?', '<tr>', '<row>', '<td>', '<table>', 'A'),
(4, 'What is the correct HTML5 doctype?', '<!DOCTYPE html>', '<!DOCTYPE HTML5>', '<html>', '<!html>', 'A');

-- CSS Questions (exam_id = 5)
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(5, 'What does CSS stand for?', 'Computer Style Sheets', 'Cascading Style Sheets', 'Creative Style Sheets', 'Colorful Style Sheets', 'B'),
(5, 'Which property is used to change the text color?', 'font-color', 'text-color', 'color', 'fgcolor', 'C'),
(5, 'Which property is used to change the background color?', 'bgcolor', 'background-color', 'color', 'backcolor', 'B'),
(5, 'How do you select an element with id "demo"?', '#demo', '.demo', 'demo', '*demo', 'A'),
(5, 'How do you select elements with class "intro"?', 'intro', '#intro', '.intro', '*intro', 'C'),
(5, 'Which property controls the text size?', 'text-size', 'font-size', 'size', 'text-style', 'B'),
(5, 'Which property is used to add space between elements?', 'spacing', 'margin', 'padding', 'gap', 'B'),
(5, 'What is the default value of the position property?', 'fixed', 'absolute', 'static', 'relative', 'C'),
(5, 'Which value makes an element invisible but still takes space?', 'display: none', 'visibility: hidden', 'opacity: 0', 'hidden: true', 'B'),
(5, 'Which property is used to make text bold?', 'font-weight', 'bold', 'text-weight', 'weight', 'A');

-- JavaScript Questions (exam_id = 6)
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(6, 'Which keyword is used to declare a variable in JavaScript?', 'var', 'variable', 'declare', 'dim', 'A'),
(6, 'What will typeof null return?', '"undefined"', '"null"', '"object"', '"empty"', 'C'),
(6, 'Which operator is used for strict equality?', '=', '==', '===', 'equals', 'C'),
(6, 'What does console.log() do?', 'Creates a log file', 'Outputs to browser console', 'Logs errors only', 'Saves data', 'B'),
(6, 'Which method adds an element to the end of an array?', 'push()', 'append()', 'add()', 'insert()', 'A'),
(6, 'What is the correct way to write a function?', 'function myFunc()', 'func myFunc()', 'function = myFunc()', 'myFunc function()', 'A'),
(6, 'Which loop iterates over object properties?', 'for', 'for-in', 'while', 'do-while', 'B'),
(6, 'What does document.getElementById() return?', 'Array of elements', 'First matching element', 'Single element by ID', 'All elements', 'C'),
(6, 'Which keyword is used to create a constant?', 'constant', 'const', 'let', 'var', 'B'),
(6, 'What does the + operator do with strings?', 'Adds numbers', 'Concatenates strings', 'Multiplies', 'Divides', 'B');

-- Machine Learning Questions (exam_id = 7)
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(7, 'What is Machine Learning?', 'A type of hardware', 'Systems that learn from data', 'A programming language', 'A database system', 'B'),
(7, 'Which type of learning uses labeled data?', 'Unsupervised', 'Supervised', 'Reinforcement', 'Semi-supervised', 'B'),
(7, 'What is a "model" in ML?', 'A type of data', 'A mathematical representation learned from data', 'A programming framework', 'A database', 'B'),
(7, 'What does "training" mean in ML?', 'Teaching users', 'Learning patterns from data', 'Testing software', 'Deploying applications', 'B'),
(7, 'Which algorithm is used for classification?', 'K-Means', 'Linear Regression', 'Decision Tree', 'PCA', 'C'),
(7, 'What is overfitting?', 'Model too simple', 'Model memorizes training data too well', 'Model too fast', 'Model too slow', 'B'),
(7, 'What is a "feature" in ML?', 'A bug', 'An input variable used for prediction', 'A function', 'A result', 'B'),
(7, 'Which library is popular for ML in Python?', 'React', 'scikit-learn', 'jQuery', 'Express', 'B'),
(7, 'What does "regression" predict?', 'Categories', 'Continuous values', 'Labels', 'Images', 'B'),
(7, 'What is a "dataset"?', 'A single value', 'Collection of data for training/testing', 'A variable', 'A function', 'B');

-- Gen AI Questions (exam_id = 8)
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES
(8, 'What does "Gen AI" stand for?', 'General Artificial Intelligence', 'Generative Artificial Intelligence', 'Genetic AI', 'Generic AI', 'B'),
(8, 'What can Generative AI create?', 'Only text', 'Text, images, code, and more', 'Only images', 'Only code', 'B'),
(8, 'Which is an example of Gen AI?', 'Excel', 'ChatGPT', 'Photoshop', 'Word', 'B'),
(8, 'What is a "prompt" in Gen AI?', 'A result', 'Input instruction given to the model', 'A bug', 'A type of model', 'B'),
(8, 'What does "LLM" stand for?', 'Large Logic Model', 'Large Language Model', 'Long Language Model', 'Layered Learning Model', 'B'),
(8, 'Which technique helps Gen AI understand context?', 'Random selection', 'Transformer architecture', 'Simple loops', 'Basic if-else', 'B'),
(8, 'What is "hallucination" in Gen AI?', 'A feature', 'When AI generates incorrect but plausible content', 'A type of model', 'A training method', 'B'),
(8, 'What does "fine-tuning" mean?', 'Fixing bugs', 'Training a model on specific data', 'Deleting data', 'Slowing down', 'B'),
(8, 'Which company created ChatGPT?', 'Google', 'OpenAI', 'Microsoft', 'Amazon', 'B'),
(8, 'What is "token" in Gen AI?', 'A password', 'A unit of text (word or subword)', 'A key', 'A file', 'B');
