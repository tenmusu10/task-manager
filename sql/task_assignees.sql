-- 既存の task_board_db に「担当者の複数選択」を追加するSQL
-- （init_db.sql から作り直す場合は不要。1回だけ実行する）
USE task_board_db;

-- タスク担当者テーブル（1つのタスクに担当者の数だけ行を登録する）
CREATE TABLE task_assignees (
    task_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (task_id, user_id),
    CONSTRAINT fk_task_assignees_task FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
    CONSTRAINT fk_task_assignees_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 既存タスクの担当者（tasks.assignee_id）を移す
INSERT INTO task_assignees (task_id, user_id)
SELECT task_id, assignee_id FROM tasks WHERE assignee_id IS NOT NULL;

-- タスクの論理削除（status = '削除'）用。fk_tasks_status があるため statuses に必要
INSERT IGNORE INTO statuses (status_code, status_name, sort_order) VALUES ('削除', '削除', 9);
