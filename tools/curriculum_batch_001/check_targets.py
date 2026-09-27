import yaml
from pathlib import Path

target_ids = [
    'reading.b2.leadership-emotional-intelligence',
    'reading.b2.micro-frontend-paradigms',
    'reading.b2.cross-cultural-negotiation',
    'reading.c1.executive-crisis-communication',
    'reading.c1.behavioral-economics-product-choice',
    'reading.c2.architectural-modularity-and-technical-debt',
    'reading.c2.algorithmic-governance-and-ethics',
    'reading.c2.monetary-policy-and-macro-imbalances'
]

for y in sorted(Path('content/reading').rglob('*.yaml')):
    if 'batches' in y.parts:
        continue
    with open(y, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)
    for a in data:
        if a['id'] in target_ids:
            words = sum(len(p['content_en'].split()) for p in a['paragraphs'])
            print(f"{a['id']} ({a['cefr_level']}): {words} words, {len(a['paragraphs'])} paras")
