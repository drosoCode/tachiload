import os
import json
import subprocess

runCmd = "java -jar ../app/build/libs/app-all.jar"

proc = subprocess.Popen(runCmd + " --extensions", stdout=subprocess.PIPE, shell=True)
extensions = json.loads(proc.communicate()[0])

with open("compat.json", "r") as f:
    ext_data = json.load(f)

for i in range(len(ext_data)):
    if ext_data[i][0] in extensions:
        for j in extensions[ext_data[i][0]]:
            if j["name"] == ext_data[i][1]:
                ext_data[i][3] = True

with open("compat.json", "w") as f:
    json.dump(ext_data, f)