import os
import subprocess
import json
import shutil

basePath = "/app/app/src/main/kotlin/tachiload/extension"

extensions_error = {}
proc = subprocess.Popen(["/app/gradlew", "build"], stderr=subprocess.PIPE)
while True:
    line = proc.stderr.readline()
    if line:
        l = line.rstrip().decode("utf-8")
        if l.find("e: " + basePath) == 0:
            print(l)
            arr = l.split("/")
            lang = arr[8]
            ext = arr[9]
            if lang not in extensions_error:
                extensions_error[lang] = []
            if ext not in extensions_error[lang]:
                extensions_error[lang].append(ext)
    else:
        break

with open(os.path.join(basePath, "extensions.json"), "r") as f:
    ext_data = json.load(f)

for lang in extensions_error:
    i = 0
    while i < len(ext_data[lang]):
        if ext_data[lang][i]["name"] in extensions_error[lang]:
            shutil.rmtree(os.path.join(basePath, lang, ext_data[lang][i]["name"]))
            del ext_data[lang][i]
        else:
            i += 1

with open(os.path.join(basePath, "extensions.json"), "w") as f:
    json.dump(ext_data, f)

os.system("/app/gradlew build")
