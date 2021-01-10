import os
import json

extPath = "../app/src/main/kotlin/tachiload/extension"

langs = os.listdir(extPath)
with open("compat.json", "r") as f:
    ext_data = json.load(f)

for i in range(len(ext_data)):
    if ext_data[i][0] in langs and ext_data[i][1] in os.listdir(
        os.path.join(extPath, ext_data[i][0])
    ):
        ext_data[i][2] = True

with open("compat.json", "w") as f:
    json.dump(ext_data, f)