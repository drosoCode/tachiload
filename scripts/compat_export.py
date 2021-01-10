import os
import json
import subprocess

extensions = []

repoPath = "/tmp/tachiyomi-extensions"
extPath = "../app/src/main/kotlin/tachiload/extension"


langs = os.listdir(extPath)
with open("compat.json", "r") as f:
    ext_data = json.load(f)

for i in range(len(ext_data)):
    if ext_data[i][0] in langs and ext_data[i][1] in os.listdir(
        os.path.join(extPath, ext_data[i][0])
    ):
        ext_data[i][4] = True


compat_data = "# Tachiload Compatibility List\n"
compat_data += "## Tested with this version: [tachiyomi-extensions](https://github.com/tachiyomiorg/tachiyomi-extensions/tree/"
rev = (
    subprocess.Popen(
        "cd " + repoPath + " && git rev-parse HEAD", stdout=subprocess.PIPE, shell=True
    )
    .communicate()[0]
    .decode("utf-8")
)
compat_data += rev[: len(rev) - 1] + ")\n"

compat_data += "|Language | Name | Depandancies | Compilation | Tests|\n"
compat_data += "|---------|------|--------------|-------------|------|\n"

for e in ext_data:
    compat_data += (
        "| "
        + e[0]
        + " | "
        + e[1]
        + " | "
        + ("✔️" if e[2] else "❌")
        + " | "
        + ("✔️" if e[2] and e[3] else "❌")
        + " | "
        + ("✔️" if e[2] and e[3] and e[4] else "❌")
        + " |\n"
    )

with open("../COMPATIBILITY.md", "w") as f:
    f.write(compat_data)