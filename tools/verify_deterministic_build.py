import sqlite3
import sys
import tempfile
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent.parent))
from tools.build_content_db import ContentDatabaseBuilder

def verify():
    with tempfile.TemporaryDirectory() as td:
        p1 = Path(td) / "db1.db"
        p2 = Path(td) / "db2.db"
        b1 = ContentDatabaseBuilder(content_dir=Path("content"), schema_dir=Path("content/schema"), output_path=p1)
        b2 = ContentDatabaseBuilder(content_dir=Path("content"), schema_dir=Path("content/schema"), output_path=p2)
        assert b1.build(), "db1 build failed"
        assert b2.build(), "db2 build failed"

        c1 = sqlite3.connect(p1)
        c2 = sqlite3.connect(p2)

        tables = [r[0] for r in c1.execute("SELECT name FROM sqlite_master WHERE type='table' AND name != 'sqlite_sequence'").fetchall()]
        all_matched = True
        for tbl in sorted(tables):
            if tbl == "content_metadata":
                rows1 = c1.execute(f"SELECT * FROM {tbl} WHERE key != 'built_at' ORDER BY 1").fetchall()
                rows2 = c2.execute(f"SELECT * FROM {tbl} WHERE key != 'built_at' ORDER BY 1").fetchall()
            else:
                rows1 = c1.execute(f"SELECT * FROM {tbl} ORDER BY 1").fetchall()
                rows2 = c2.execute(f"SELECT * FROM {tbl} ORDER BY 1").fetchall()

            if rows1 != rows2:
                print(f"MISMATCH in table {tbl}: {len(rows1)} vs {len(rows2)}")
                all_matched = False
            else:
                print(f"Table {tbl}: {len(rows1)} rows MATCH 100%")

        c1.close()
        c2.close()

        if all_matched:
            print("DETERMINISTIC REBUILD: 100% IDENTICAL ACROSS ALL CURRICULUM TABLES")
        else:
            raise RuntimeError("Build is not deterministic")

if __name__ == "__main__":
    verify()
