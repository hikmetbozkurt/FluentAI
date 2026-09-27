import sqlite3

conn = sqlite3.connect("app/src/main/assets/content.db")
c = conn.cursor()
c.execute("SELECT id, headword, cefr_level, meaning_tr FROM vocab_items ORDER BY id")
rows = c.fetchall()

levels = {}
for r in rows:
    lvl = r[2]
    levels.setdefault(lvl, []).append(r)

selected = []
for lvl in ["A2", "B1", "B2", "C1", "C2"]:
    items = levels.get(lvl, [])
    # take 10 items from each level
    # pick every Nth item to get good variety
    step = len(items) // 10
    lvl_selected = [items[i * step] for i in range(10)]
    selected.extend(lvl_selected)

print(f"Selected {len(selected)} vocab items:")
for s in selected:
    print(f"  {s[0]} ({s[2]}): {s[1]} - {s[3][:30]}")

with open("tools/curriculum_batch_001/selected_vocab.txt", "w", encoding="utf-8") as f:
    for s in selected:
        f.write(f"{s[0]}|{s[1]}|{s[2]}|{s[3]}\n")
