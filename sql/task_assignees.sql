-- タスクの担当者（複数）を保存するテーブル
-- 1つのタスクに対して、担当者の数だけ行を登録する
CREATE TABLE task_assignees (
    task_id INT NOT NULL,   -- tasks.task_id
    user_id INT NOT NULL,   -- users.user_id
    PRIMARY KEY (task_id, user_id)
);

-- 既存タスクの担当者（tasks.assignee_id）を移しておく（1回だけ実行）
INSERT INTO task_assignees (task_id, user_id)
SELECT task_id, assignee_id FROM tasks WHERE assignee_id > 0;
