import sqlite3

conn = sqlite3.connect("app/src/main/assets/content.db")
c = conn.cursor()
c.execute("SELECT name FROM sqlite_master WHERE type='table'")
tables = [r[0] for r in c.fetchall()]
print("Tables:", tables)

for tbl in ['vocab_items', 'grammar_lessons', 'reading_articles', 'listening_scenarios', 'exercises']:
    c.execute(f"SELECT COUNT(*) FROM {tbl} WHERE id LIKE 'batch_%' OR id LIKE 'batch-%' OR id LIKE 'batch.%'")
    print(f"{tbl}: {c.fetchone()[0]}")
