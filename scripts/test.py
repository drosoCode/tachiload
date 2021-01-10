import os
import subprocess
import json
import shutil

extPath = "../app/src/main/kotlin/tachiload/extension"
dataPath = "../app/src/main/resources/extensions.json"
testCmd = 'java -jar ../app/build/libs/app-all.jar --search "abc" '
buildCmd = "cd ../ && ./gradlew build"

with open(dataPath, "r") as f:
    ext_data = json.load(f)

for lang in ext_data.keys():
    i = 0
    while i < len(ext_data[lang]):
        extensions_error = {}
        proc = subprocess.Popen(
            testCmd + '"' + lang + '" "' + ext_data[lang][i]["name"] + '"',
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            shell=True,
        )

        if proc.communicate()[1]:
            # if data on stderr, remove the extension
            p = os.path.join(extPath, lang, ext_data[lang][i]["name"])
            if os.path.exists(p):
                shutil.rmtree(p)
            print(" - " + lang + "|" + ext_data[lang][i]["name"] + ": ERR")
            del ext_data[lang][i]
        else:
            print(" - " + lang + "|" + ext_data[lang][i]["name"] + ": OK")
        i += 1

with open(dataPath, "w") as f:
    json.dump(ext_data, f)

os.system(buildCmd)