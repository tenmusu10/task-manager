-- データベースの初期化と作成（文字化け防止のUTF8MB4指定）
DROP DATABASE IF EXISTS task_board_db;
CREATE DATABASE task_board_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE task_board_db;

-- 1. ユーザーマスタ
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. ステータスマスタ
CREATE TABLE statuses (
    status_code VARCHAR(20) PRIMARY KEY,
    status_name VARCHAR(20) NOT NULL,
    sort_order INT NOT NULL
);

-- 3. プロジェクトテーブル
CREATE TABLE projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    description TEXT NULL,
    deadline DATE NULL,
    status VARCHAR(20) NOT NULL DEFAULT '進行中',
    created_by INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_projects_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- 4. タスクテーブル
CREATE TABLE tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    task_name VARCHAR(100) NOT NULL,
    description TEXT NULL,
    status VARCHAR(20) NOT NULL DEFAULT '未着手',
    assignee_id INT NULL,
    deadline DATE NULL,
    created_by INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_status FOREIGN KEY (status) REFERENCES statuses(status_code),
    CONSTRAINT fk_tasks_assignee FOREIGN KEY (assignee_id) REFERENCES users(user_id) ON DELETE SET NULL,
    CONSTRAINT fk_tasks_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- 5. タスク担当者テーブル（★追加：1つのタスクに複数の担当者を登録するため）
--    tasks.assignee_id には先頭の担当者も引き続き入れる
CREATE TABLE task_assignees (
    task_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (task_id, user_id),
    CONSTRAINT fk_task_assignees_task FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
    CONSTRAINT fk_task_assignees_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==========================================
-- 以下、テスト用の初期ダミーデータ
-- ==========================================

INSERT INTO statuses (status_code, status_name, sort_order) VALUES
('未着手', '未着手', 1),
('進行中', '進行中', 2),
('完了', '完了', 3),
('削除', '削除', 9); -- ★追加：タスクの論理削除（status = '削除'）に使う。fk_tasks_status があるため必要

INSERT INTO users (login_id, password, user_name) VALUES
('sugita', 'pass123', '杉田'),
('li', 'pass123', '李'),
('ishibashi', 'pass123', '石橋'),
('shinohara', 'pass123', '篠原'),
('sakai', 'pass123', '境'),
('sato', 'pass123', '佐藤');

INSERT INTO projects (project_name, description, deadline, created_by) VALUES
('グループワーク：タスクボード開発', 'JSP/ServletとMySQLを用いたTrello風アプリの開発プロジェクト', '2026-10-21', 1);

INSERT INTO tasks (project_id, task_name, description, status, assignee_id, deadline, created_by) VALUES
(1, 'DB設計とSQL作成', '4つのテーブルを作成しダミーデータを入れる', '完了', 2, '2026-10-14', 1),
(1, '共通Beanと定数クラスの作成', 'AppConstと各Beanを作成してWARで配る', '進行中', 1, '2026-10-14', 1),
(1, 'タスク一覧表示Servletの実装', 'DBからタスクを取得してJSPへフォワードする', '未着手', 3, '2026-10-16', 1),
(1, 'タスク新規登録Servletの実装', 'フォームの値を受け取りINSERTする', '未着手', 4, '2026-10-16', 1),
(1, 'ログイン画面・ボード画面のJSP作成', 'まずは白黒のHTMLで枠組みを作る', '進行中', 5, '2026-10-16', 1),
(1, '入力フォーム作成とCSSデザイン', '登録フォームの作成とTrello風の3列レイアウト調整', '未着手', 6, '2026-10-18', 1);

-- ★追加：各タスクの担当者（assignee_id）を task_assignees にも登録
INSERT INTO task_assignees (task_id, user_id)
SELECT task_id, assignee_id FROM tasks WHERE assignee_id IS NOT NULL;