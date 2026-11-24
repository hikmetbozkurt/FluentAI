#!/usr/bin/env python3
"""
Generate flat-editorial-v1 matte-gouache WebP covers (1536x1024) for all Reading articles
missing an entry in app/src/main/assets/reading/reading-images.manifest.json, then update the manifest.
"""

import glob
import hashlib
import json
import math
import random
from pathlib import Path
from typing import Dict, List, Tuple

import numpy as np
import yaml
from PIL import Image, ImageDraw, ImageFilter

ROOT = Path(__file__).resolve().parents[2]
ASSETS_READING = ROOT / "app" / "src" / "main" / "assets" / "reading"
MANIFEST_PATH = ASSETS_READING / "reading-images.manifest.json"

W, H = 1536, 1024
SW, SH = W * 2, H * 2  # 2x supersample for crisp vector antialiasing

PALETTE = {
    "paper": (245, 241, 232),
    "linen": (239, 233, 220),
    "sand": (232, 201, 160),
    "clay": (222, 178, 152),
    "terracotta": (163, 69, 42),
    "terracotta_soft": (196, 114, 84),
    "navy": (30, 42, 58),
    "slate": (78, 92, 112),
    "sage": (63, 106, 74),
    "sage_soft": (146, 178, 150),
    "cream": (253, 250, 244),
}


def sx(x: float) -> int:
    return int(round(x * SW))


def sy(y: float) -> int:
    return int(round(y * SH))


def draw_paper_base(rng: random.Random) -> Image.Image:
    base = Image.new("RGB", (SW, SH), PALETTE["paper"])
    draw = ImageDraw.Draw(base)

    # Soft editorial background wash shapes
    wash_tone = rng.choice([PALETTE["linen"], (242, 235, 221), (240, 236, 228)])
    cx = rng.uniform(0.25, 0.75)
    cy = rng.uniform(0.25, 0.72)
    r = rng.uniform(0.32, 0.44)
    draw.ellipse([sx(cx - r), sx(cy - r * 0.7), sx(cx + r), sx(cy + r * 0.7)], fill=wash_tone)

    # Ground table or architectural horizon plane
    horizon = rng.uniform(0.68, 0.76)
    ground_color = rng.choice([PALETTE["linen"], PALETTE["sand"], (235, 224, 204)])
    draw.rectangle([0, sy(horizon), SW, SH], fill=ground_color)
    draw.line([(sx(0.08), sy(horizon)), (sx(0.92), sy(horizon))], fill=PALETTE["slate"], width=4)
    return base


def draw_botanical_stem(draw: ImageDraw.Draw, bx: float, by: float, tilt: float, scale: float, leaf_col: Tuple[int, int, int]) -> None:
    pts = [(sx(bx), sy(by)), (sx(bx + tilt * 0.04), sy(by - 0.14 * scale)), (sx(bx + tilt * 0.08), sy(by - 0.28 * scale))]
    draw.line(pts, fill=PALETTE["navy"], width=6)
    for k, (fx, fy, dx, dy) in enumerate([
        (0.03, 0.10, -0.07, -0.05),
        (0.05, 0.17, 0.07, -0.04),
        (0.07, 0.25, -0.05, -0.06),
        (0.08, 0.29, 0.04, -0.07),
    ]):
        px = bx + tilt * fx
        py = by - fy * scale
        ex = px + dx * scale
        ey = py + dy * scale
        bbox = [min(sx(px), sx(ex)), min(sy(py), sy(ey)), max(sx(px), sx(ex)), max(sy(py), sy(ey))]
        draw.ellipse(bbox, fill=leaf_col)


def draw_finance_motif(draw: ImageDraw.Draw, rng: random.Random) -> None:
    # Quiet architectural ledger slab + balance arch + ascending blocks + botanical vessel
    horizon = 0.72
    draw.rounded_rectangle([sx(0.16), sy(0.24), sx(0.56), sy(horizon)], radius=24, fill=PALETTE["cream"], outline=PALETTE["navy"], width=6)
    # Ledger line bars (abstract, zero text)
    for idx in range(4):
        y = 0.33 + idx * 0.085
        w = 0.24 + (idx % 2) * 0.05
        col = PALETTE["terracotta"] if idx == 1 else PALETTE["slate"]
        draw.rounded_rectangle([sx(0.21), sy(y), sx(0.21 + w), sy(y + 0.018)], radius=8, fill=col)

    # Ascending bar chart prism overlapping right of ledger
    bars = [0.22, 0.34, 0.45, 0.56]
    cols = [PALETTE["sand"], PALETTE["sage_soft"], PALETTE["terracotta_soft"], PALETTE["terracotta"]]
    for i, (bh, col) in enumerate(zip(bars, cols)):
        x0 = 0.48 + i * 0.08
        draw.rounded_rectangle([sx(x0), sy(horizon - bh), sx(x0 + 0.055), sy(horizon)], radius=12, fill=col, outline=PALETTE["navy"], width=5)

    # Ceramic planter & stems on far left
    draw.rounded_rectangle([sx(0.09), sy(horizon - 0.14), sx(0.20), sy(horizon)], radius=18, fill=PALETTE["sand"], outline=PALETTE["navy"], width=5)
    draw_botanical_stem(draw, 0.145, horizon - 0.14, rng.uniform(-0.8, 0.8), 0.75, PALETTE["sage"])


def draw_engineering_motif(draw: ImageDraw.Draw, rng: random.Random) -> None:
    horizon = 0.74
    # Drafting desk lamp
    draw.rounded_rectangle([sx(0.14), sy(horizon - 0.05), sx(0.28), sy(horizon)], radius=14, fill=PALETTE["navy"])
    draw.line([(sx(0.20), sy(horizon - 0.05)), (sx(0.17), sy(0.32)), (sx(0.27), sy(0.24))], fill=PALETTE["navy"], width=12)
    draw.chord([sx(0.20), sy(0.18), sx(0.38), sy(0.42)], start=175, end=355, fill=PALETTE["terracotta"], outline=PALETTE["navy"], width=6)

    # Modular stacked architecture blocks / cards
    cards = [
        (0.38, 0.34, 0.24, 0.18, PALETTE["cream"]),
        (0.47, 0.45, 0.26, 0.19, PALETTE["sand"]),
        (0.60, 0.26, 0.23, 0.22, PALETTE["sage_soft"]),
    ]
    for x, y, w, h, col in cards:
        draw.rounded_rectangle([sx(x), sy(y), sx(x + w), sy(y + h)], radius=20, fill=col, outline=PALETTE["navy"], width=6)
        # Abstract structural slots (zero text)
        draw.rounded_rectangle([sx(x + 0.03), sy(y + 0.045), sx(x + w * 0.55), sy(y + 0.068)], radius=8, fill=PALETTE["navy"])
        draw.rounded_rectangle([sx(x + 0.03), sy(y + 0.092), sx(x + w * 0.78), sy(y + 0.11)], radius=8, fill=PALETTE["terracotta_soft"])

    # Connecting conduit lines
    draw.line([(sx(0.50), sy(0.52)), (sx(0.50), sy(horizon))], fill=PALETTE["navy"], width=6)
    draw.ellipse([sx(0.72), sy(horizon - 0.11), sx(0.84), sy(horizon)], fill=PALETTE["clay"], outline=PALETTE["navy"], width=5)


def draw_leadership_motif(draw: ImageDraw.Draw, rng: random.Random) -> None:
    horizon = 0.73
    # Architectural arch + intersecting human-scale equilibrium discs (team harmony)
    draw.chord([sx(0.28), sy(0.16), sx(0.72), sy(0.82)], start=180, end=360, fill=PALETTE["sand"])
    circles = [
        (0.32, 0.46, 0.11, PALETTE["terracotta"]),
        (0.50, 0.40, 0.12, PALETTE["sage"]),
        (0.67, 0.48, 0.105, PALETTE["navy"]),
    ]
    for cx, cy, r, col in circles:
        draw.ellipse([sx(cx - r), sy(cy - r * 1.35), sx(cx + r), sy(cy + r * 1.35)], fill=col, outline=PALETTE["cream"], width=8)
    # Foreground notebook & mug on shared desk
    draw.rounded_rectangle([sx(0.22), sy(horizon - 0.06), sx(0.76), sy(horizon + 0.04)], radius=18, fill=PALETTE["cream"], outline=PALETTE["navy"], width=6)
    draw_botanical_stem(draw, 0.84, horizon, -0.5, 0.9, PALETTE["sage"])


def draw_strategy_motif(draw: ImageDraw.Draw, rng: random.Random) -> None:
    horizon = 0.74
    # Concentric horizon sun + terraced stepping blocks + compass triangle
    draw.ellipse([sx(0.56), sy(0.18), sx(0.82), sy(0.57)], fill=PALETTE["sand"])
    draw.ellipse([sx(0.61), sy(0.25), sx(0.77), sy(0.49)], fill=PALETTE["terracotta"])

    steps = [
        (0.16, 0.56, 0.16, horizon - 0.56, PALETTE["navy"]),
        (0.32, 0.44, 0.16, horizon - 0.44, PALETTE["sage"]),
        (0.48, 0.32, 0.16, horizon - 0.32, PALETTE["clay"]),
    ]
    for x, y, w, h, col in steps:
        draw.rounded_rectangle([sx(x), sy(y), sx(x + w), sy(y + h)], radius=14, fill=col, outline=PALETTE["navy"], width=6)
    draw.line([(sx(0.24), sy(0.48)), (sx(0.56), sy(0.24))], fill=PALETTE["terracotta"], width=8)
    draw.ellipse([sx(0.545), sy(0.22), sx(0.575), sy(0.265)], fill=PALETTE["terracotta"])


def draw_technology_motif(draw: ImageDraw.Draw, rng: random.Random) -> None:
    horizon = 0.74
    # Geometric node constellation + secure vessel/key shield + desk plant
    nodes = [(0.26, 0.44), (0.44, 0.28), (0.64, 0.33), (0.76, 0.50), (0.50, 0.56)]
    for i in range(len(nodes)):
        x1, y1 = nodes[i]
        x2, y2 = nodes[(i + 1) % len(nodes)]
        draw.line([(sx(x1), sy(y1)), (sx(x2), sy(y2))], fill=PALETTE["slate"], width=6)
    draw.line([(sx(0.44), sy(0.28)), (sx(0.50), sy(0.56))], fill=PALETTE["slate"], width=6)

    for idx, (nx, ny) in enumerate(nodes):
        col = [PALETTE["terracotta"], PALETTE["navy"], PALETTE["sage"], PALETTE["sand"], PALETTE["clay"]][idx % 5]
        r = 0.055 if idx % 2 == 0 else 0.042
        draw.ellipse([sx(nx - r), sy(ny - r * 1.4), sx(nx + r), sy(ny + r * 1.4)], fill=col, outline=PALETTE["cream"], width=7)

    # Foreground server prism / hardware pedestal
    draw.rounded_rectangle([sx(0.36), sy(horizon - 0.16), sx(0.64), sy(horizon)], radius=18, fill=PALETTE["navy"], outline=PALETTE["navy"], width=5)
    draw.ellipse([sx(0.48), sy(horizon - 0.10), sx(0.52), sy(horizon - 0.045)], fill=PALETTE["terracotta"])


def draw_communication_motif(draw: ImageDraw.Draw, rng: random.Random) -> None:
    horizon = 0.72
    # Open minimalist notebook / stationery + fountain nib + coffee mug + plant
    draw.rounded_rectangle([sx(0.22), sy(0.28), sx(0.52), sy(horizon - 0.04)], radius=18, fill=PALETTE["cream"], outline=PALETTE["navy"], width=6)
    draw.rounded_rectangle([sx(0.49), sy(0.28), sx(0.76), sy(horizon - 0.04)], radius=18, fill=PALETTE["linen"], outline=PALETTE["navy"], width=6)
    draw.line([(sx(0.505), sy(0.28)), (sx(0.505), sy(horizon - 0.04))], fill=PALETTE["navy"], width=8)
    for k in range(5):
        y = 0.36 + k * 0.055
        draw.line([(sx(0.27), sy(y)), (sx(0.46), sy(y))], fill=PALETTE["slate"], width=5)
        if k < 3:
            draw.line([(sx(0.55), sy(y)), (sx(0.70), sy(y))], fill=PALETTE["terracotta_soft"], width=5)

    # Terracotta cup on right
    draw.rounded_rectangle([sx(0.79), sy(horizon - 0.15), sx(0.89), sy(horizon)], radius=20, fill=PALETTE["terracotta"], outline=PALETTE["navy"], width=6)
    draw.arc([sx(0.86), sy(horizon - 0.12), sx(0.92), sy(horizon - 0.04)], start=-90, end=90, fill=PALETTE["navy"], width=6)
    draw_botanical_stem(draw, 0.14, horizon, 0.6, 0.9, PALETTE["sage"])


def draw_nature_science_motif(draw: ImageDraw.Draw, rng: random.Random) -> None:
    horizon = 0.72
    # Rolling hillside strata or organic microscope orbs for open-source nature/farm/science stories
    draw.chord([sx(0.05), sy(0.46), sx(0.68), sy(0.96)], start=180, end=360, fill=PALETTE["sage_soft"])
    draw.chord([sx(0.40), sy(0.40), sx(0.96), sy(0.96)], start=180, end=360, fill=PALETTE["sand"])
    draw.ellipse([sx(0.42), sy(0.20), sx(0.58), sy(0.44)], fill=PALETTE["terracotta"], outline=PALETTE["navy"], width=6)
    draw.ellipse([sx(0.32), sy(0.34), sx(0.43), sy(0.50)], fill=PALETTE["navy"], outline=PALETTE["cream"], width=6)
    draw.ellipse([sx(0.60), sy(0.28), sx(0.70), sy(0.43)], fill=PALETTE["sage"], outline=PALETTE["cream"], width=6)
    draw_botanical_stem(draw, 0.22, horizon, -0.5, 0.95, PALETTE["sage"])
    draw_botanical_stem(draw, 0.78, horizon, 0.5, 0.85, PALETTE["terracotta_soft"])


def add_subtle_paper_grain(img: Image.Image, seed_int: int) -> Image.Image:
    arr = np.asarray(img, dtype=np.int16)
    np_rng = np.random.default_rng(seed_int)
    noise = np_rng.integers(-5, 6, size=(arr.shape[0], arr.shape[1], 1), dtype=np.int16)
    arr = np.clip(arr + noise, 0, 255).astype(np.uint8)
    return Image.fromarray(arr, mode="RGB")


def render_cover(article_id: str, category: str, title: str, out_path: Path) -> None:
    seed_int = int(hashlib.sha256(article_id.encode("utf-8")).hexdigest()[:8], 16)
    rng = random.Random(seed_int)

    canvas = draw_paper_base(rng)
    draw = ImageDraw.Draw(canvas)

    title_lower = title.lower()
    if any(k in title_lower for k in ("fungus", "animal", "farms", "chernobyl", "earth")):
        draw_nature_science_motif(draw, rng)
    elif category == "finance_and_economics" or "retirement" in title_lower or "healthcare" in title_lower:
        draw_finance_motif(draw, rng)
    elif category == "engineering_culture":
        draw_engineering_motif(draw, rng)
    elif category == "leadership_and_management" or "ceo" in title_lower or "sister" in title_lower:
        draw_leadership_motif(draw, rng)
    elif category == "business_strategy":
        draw_strategy_motif(draw, rng)
    elif category == "technology":
        draw_technology_motif(draw, rng)
    else:
        draw_communication_motif(draw, rng)

    # Small editorial corner balance accent dot varied per seed
    dot_x = rng.uniform(0.10, 0.14) if rng.random() > 0.5 else rng.uniform(0.86, 0.90)
    dot_y = rng.uniform(0.14, 0.22)
    draw.ellipse([sx(dot_x), sy(dot_y), sx(dot_x + 0.025), sy(dot_y + 0.037)], fill=PALETTE["terracotta"])

    # Crisp 2x downsample + organic matte-paper grain
    downsampled = canvas.resize((W, H), Image.Resampling.LANCZOS)
    textured = add_subtle_paper_grain(downsampled, seed_int)
    out_path.parent.mkdir(parents=True, exist_ok=True)
    textured.save(out_path, format="WEBP", quality=84, method=6)


def main() -> None:
    manifest = json.loads(MANIFEST_PATH.read_text(encoding="utf-8"))
    covers: Dict[str, str] = dict(manifest.get("covers", {}))

    articles: List[dict] = []
    for f in sorted(glob.glob(str(ROOT / "content" / "reading" / "**" / "*.yaml"), recursive=True)):
        data = yaml.safe_load(open(f, encoding="utf-8"))
        items = data if isinstance(data, list) else data.get("items", [])
        articles.extend(items)

    generated = 0
    for art in articles:
        aid = art["id"]
        rel_name = f"cover-{aid}.webp"
        out_path = ASSETS_READING / rel_name
        if aid not in covers or not out_path.is_file():
            render_cover(aid, art["category"], art["title"], out_path)
            covers[aid] = rel_name
            generated += 1

    # Verify every cover in manifest resolves on disk
    missing = [k for k, v in covers.items() if not (ASSETS_READING / v).is_file()]
    assert not missing, f"Missing image files: {missing}"

    manifest["covers"] = dict(sorted(covers.items()))
    MANIFEST_PATH.write_text(json.dumps(manifest, indent=2) + "\n", encoding="utf-8")
    print(f"Generated {generated} WebP covers. Manifest now covers {len(manifest['covers'])}/{len(articles)} reading articles.")


if __name__ == "__main__":
    main()
