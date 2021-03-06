import os
import subprocess
import json
import shutil

basePath = "/app/app/src/main/kotlin/tachiload/extension"
dataPath = "../app/src/main/resources/extensions.json"
buildCmd = "cd ../ && ./gradlew build"

extensions_error = {}
proc = subprocess.Popen(buildCmd, stderr=subprocess.PIPE, shell=True)
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

with open(dataPath, "r") as f:
    ext_data = json.load(f)

for lang in extensions_error:
    i = 0
    while i < len(ext_data[lang]):
        if ext_data[lang][i]["name"] in extensions_error[lang]:
            shutil.rmtree(os.path.join(basePath, lang, ext_data[lang][i]["name"]))
            del ext_data[lang][i]
        else:
            i += 1

with open(dataPath, "w") as f:
    json.dump(ext_data, f)

os.system(buildCmd)