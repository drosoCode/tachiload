import os
import json

extensions = []

basePath = "../app/src/main/kotlin/tachiload/extension"

for lang in sorted(os.listdir(basePath)):
    p = os.path.join(basePath, lang)
    if os.path.isdir(p):
        for ext in sorted(os.listdir(p)):
            extensions.append([lang, ext, False, False, False])

with open("compat.json", "w") as f:
    json.dump(extensions, f)