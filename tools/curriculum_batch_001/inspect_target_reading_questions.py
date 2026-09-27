import yaml
from pathlib import Path

target_ids = [
    'reading.b2.continuous-integration-evolution',
    'reading.b2.leadership-emotional-intelligence',
    'reading.b2.micro-frontend-paradigms',
    'reading.b2.cross-cultural-negotiation',
    'reading.c1.zero-trust-security-paradigms',
    'reading.c1.executive-crisis-communication',
    'reading.c1.behavioral-economics-product-choice',
    'reading.c1.distributed-consensus-systems',
    'reading.c2.epistemic-foundations-of-science',
    'reading.c2.the-mechanics-of-speculative-bubbles',
    'reading.c2.architectural-modularity-and-technical-debt',
    'reading.c2.algorithmic-governance-and-ethics'
]

for p in sorted(Path('content/reading').rglob('*.yaml')):
    if 'batches' in p.parts:
        continue
    with open(p, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)
    if not isinstance(data, list):
        continue
    for a in data:
        if a['id'] in target_ids:
            print(f"=== {a['id']} ===")
            for q in a.get('comprehension_questions', []):
                print(f"  Q: {q['question_en']} -> Correct: {q['correct_answer']}")
            words = [v['word'] for v in a.get('vocabulary_annotations', [])]
            print(f"  Vocab: {words}")
