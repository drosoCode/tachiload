import os
import json
import subprocess

extensions = []

repoPath = "/tmp/tachiyomi-extensions"
basePath = "../app/src/main/kotlin/tachiload/extension"

with open("compat.json", "r") as f:
    ext_data = json.load(f)

ext_ok = []
for lang in os.listdir(basePath):
    p = os.path.join(basePath, lang)
    if os.path.isdir(p):
        for ext in os.listdir(p):
            ext_ok.append([lang, ext])


compat_data = "# Tachiload Compatibility List\n"
compat_data += "## Tested with: [tachiyomi-extensions](https://github.com/tachiyomiorg/tachiyomi-extensions/tree/"
rev = (
    subprocess.Popen(
        "cd " + repoPath + " && git rev-parse HEAD", stdout=subprocess.PIPE, shell=True
    )
    .communicate()[0]
    .decode("utf-8")
)
compat_data += rev[: len(rev) - 1] + ")\n"

compat_data += "|Language | Name | Compilable | Tested|\n"
compat_data += "|--------|------|------------|-------|\n"

for e in ext_data:
    if e in ext_ok:
        compat_data += "| " + e[0] + " | " + e[1] + " | YES | UNTESTED |\n"
    else:
        compat_data += "| " + e[0] + " | " + e[1] + " | NO | UNTESTED |\n"

with open("COMPATIBILITY.md", "w") as f:
    f.write(compat_data)