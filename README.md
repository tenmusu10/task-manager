# 進捗管理表（task-manager）

JSP / Servlet で作成したプロジェクト・タスクの進捗管理Webアプリケーション。

- 環境：Eclipse + Tomcat（Jakarta Servlet）
- DB接続：JNDI `java:comp/env/jdbc/TaskBoardDB`

## フォルダ構成

| パス | 内容 |
|---|---|
| `src/main/java/servlet/` | Servlet・Bean・ユーティリティ |
| `src/main/webapp/jsp/` | 画面（JSP） |
| `src/main/webapp/WEB-INF/web.xml` | Servlet の URL 割り当て |

※ `css/style.css` は未登録。
