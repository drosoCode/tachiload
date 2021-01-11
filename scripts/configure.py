#!/usr/bin/python3
from whiptail import Whiptail
import subprocess
import json
import os

runCmd = "java -jar /app/app.jar"
dataPath = "/app/config.json"

tui = Whiptail(title="Tachiload", backtitle="Tachiload configuration utility")
configData = []
if os.path.exists(dataPath):
    with open(dataPath, "r") as f:
        configData = json.load(f)
else:
    with open(dataPath, "w") as f:
        f.write("[]")


proc = subprocess.Popen(runCmd + " --extensions", stdout=subprocess.PIPE, shell=True)
save = False
extensions = json.loads(proc.communicate()[0])
filterExt = extensions.keys()
selectedExts = ""

# filterExt = ["en", "all"]


def selectExt():
    lst = []
    for i in filterExt:
        for j in extensions[i]:
            lst.append(i + "|" + j["name"])
    o = tui.checklist("Select Extensions", sorted(lst))
    if o[1] == 1:
        return ""
    else:
        l = ""
        for i in o[0]:
            e = i.split("|")
            l += ' "' + e[0] + '" "' + e[1] + '"'
        return l


def getMangaList(res):
    lst = []
    for i in res:
        lst.append(
            i["language"] + "|" + i["extension"] + " Name: " + i["data"]["title"]
        )
    return sorted(lst)


def getItemFromRes(i, res):
    a = i.split(" Name: ")
    b = a[0].split("|")
    for r in res:
        if (
            b[0] == r["language"]
            and b[1] == r["extension"]
            and a[1] == r["data"]["title"]
        ):
            return r
    return None


def selectManga(extensions, name):
    proc = subprocess.Popen(
        runCmd + ' --search "' + name + '" ' + extensions,
        stdout=subprocess.PIPE,
        shell=True,
    )
    res = json.loads(proc.communicate()[0])
    r = getMangaList(res)
    if len(r) == 0:
        tui.msgbox("No result available")
        return None

    i = tui.menu("Select Result", r)
    if i[1]:
        return None
    return getItemFromRes(i[0], res)


def addManga():
    global selectedExts, configData
    if selectedExts == "" or not tui.yesno("Select extensions ?", "no"):
        selectedExts = selectExt()
        if selectedExts == "":
            return

    name = tui.inputbox("Enter manga name")
    if name[1] or name[0] == "":
        return

    d = selectManga(selectedExts, name[0])
    if d is not None:
        if d in configData:
            tui.msgbox("Manga already tracked")
        else:
            configData.append(d)


def removeManga():
    l = getMangaList(configData)
    if len(l) == 0:
        tui.msgbox("No manga available")
        return
    out = tui.menu("Manga List", l)
    if out[1]:
        return
    delete = getItemFromRes(out[0], configData)
    if delete is not None:
        if not tui.yesno("Are you sure ?", "no"):
            configData.remove(delete)


while True:
    opt = ["Add Manga", "List Mangas", "Remove Manga", "Save And Exit", "Exit"]
    out = tui.menu("Home", opt)
    if out[1]:
        break
    p = opt.index(out[0])
    if p == 0:
        addManga()
    elif p == 1:
        l = getMangaList(configData)
        if len(l) == 0:
            tui.msgbox("No manga available")
        else:
            tui.menu("Manga List", l)
    elif p == 2:
        removeManga()
    elif p == 3:
        save = True
        break
    else:
        break

if save:
    with open(dataPath, "w") as f:
        json.dump(configData, f)
