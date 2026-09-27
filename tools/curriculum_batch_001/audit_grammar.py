import yaml
from pathlib import Path

grammar_dir = Path('content/grammar')
lessons = []
titles = {}
total_examples = 0
total_traps = 0
cefr_counts = {}

for y in sorted(grammar_dir.rglob('*.yaml')):
    if 'batches' in y.parts:
        continue
    with open(y, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)
    if not isinstance(data, list):
        continue
    for g in data:
        gid = g['id']
        cefr = g['cefr_level']
        cefr_counts[cefr] = cefr_counts.get(cefr, 0) + 1
        t = g['title'].strip().lower()
        if t in titles:
            print(f"DUPLICATE TITLE: {g['title']} ({gid} vs {titles[t]})")
        titles[t] = gid
        ex_cnt = len(g.get('examples', []))
        trap_cnt = len(g.get('turkish_traps', []))
        total_examples += ex_cnt
        total_traps += trap_cnt
        if ex_cnt == 0:
            print(f"NO EXAMPLES: {gid}")
        if trap_cnt == 0:
            print(f"NO TRAPS: {gid}")
        lessons.append(gid)

print(f"Total grammar lessons: {len(lessons)}")
print(f"CEFR distribution: {cefr_counts}")
print(f"Total illustrative examples: {total_examples}")
print(f"Total Turkish learner traps: {total_traps}")
