from pathlib import Path

paths = [
    Path("tools/curriculum_batch_001/generate_listening_a2.py"),
    Path("content/listening/a2/listening_a2.yaml"),
]

for p in paths:
    if p.exists():
        txt = p.read_text(encoding="utf-8")
        txt = txt.replace("vocab.support", "vocab.support-v")
        p.write_text(txt, encoding="utf-8")
        print(f"Fixed {p}")
