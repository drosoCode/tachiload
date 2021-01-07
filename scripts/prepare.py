import os
import json
import io
import shutil

extensions = {}


def parseGradle(path: str) -> dict:
    data = {"libVersion": None, "extVersionCode": None, "extClass": None}
    with open(path, "r") as f:
        for l in f.readlines():
            for d in data.keys():
                if l.find(d) != -1:
                    data[d] = l[l.index("=") + 2 :].strip("\n").strip("'")
    return data


def findParenthesisEnd(startPos: int, data: str) -> int:
    nbOpen = 0
    for i in range(startPos + 1, len(data)):
        if data[i] == "(":
            nbOpen += 1
        elif data[i] == ")":
            if nbOpen == 0:
                return i + 1
            else:
                nbOpen -= 1


def fixKotlin(lang: str, ext: str):
    path = os.path.join(
        lang, ext, "src", "eu", "kanade", "tachiyomi", "extension", lang, ext
    )
    if not os.path.exists(path):
        shutil.rmtree(os.path.join(lang, ext))
        return

    for f in os.listdir(path):
        if f[f.rfind(".") + 1 :] == "kt":
            curFile = os.path.join(path, f)
            with open(curFile, "r") as f:
                # if extension imports android libs, discard it
                print(curFile)
                data = f.read()
                if (
                    data.find("import android") != -1
                    or data.find("import com.squareup.duktape") != -1
                ):
                    shutil.rmtree(path)
                    return
                # fix package name
                data = data.replace(
                    "package eu.kanade.tachiyomi.extension",
                    "package tachiload.extension."
                    + lang
                    + "."
                    + ext
                    + ".src.eu.kanade.tachiyomi.extension",
                )
                # fix imports
                data = data.replace("import eu.kanade", "import tachiload")
                # fix old okhttp versions
                data = data.replace("response.request().url()", "response.request.url")
                data = data.replace("response.body()", "response.body")
                data = data.replace(
                    "originalRequest.url().toString()", "originalRequest.url.toString()"
                )
                data = data.replace(
                    "chain.request().url().toString()", "chain.request().url.toString()"
                )

                data = data.replace(".body()!!.", ".body!!.")
                data = data.replace(".body()?.", ".body?.")
                data = data.replace("response.code()", "response.code")
                data = data.replace("genreResponse.code()", "genreResponse.code")
                data = data.replace(
                    ".receivedResponseAtMillis()",
                    ".receivedResponseAtMillis",
                )

                # replace HttpUrl.parse() by ().toHttpUrlOrNull()
                if data.find("HttpUrl.parse") != -1:
                    data = data.replace(
                        "import",
                        "import okhttp3.HttpUrl.Companion.toHttpUrlOrNull\nimport",
                        1,
                    )
                i = data.find("HttpUrl.parse")
                while i != -1:
                    data = data.replace("HttpUrl.parse", "", 1)
                    j = findParenthesisEnd(i, data)
                    data = data[:j] + ".toHttpUrlOrNull()" + data[j:]
                    i = data.find("HttpUrl.parse")

                # replace MediaType.parse() by ()..toMediaType()
                if data.find("MediaType.parse") != -1:
                    data = data.replace(
                        "import",
                        "import okhttp3.MediaType.Companion.toMediaType\nimport",
                        1,
                    )
                i = data.find("MediaType.parse")
                while i != -1:
                    data = data.replace("MediaType.parse", "", 1)
                    j = findParenthesisEnd(i, data)
                    data = data[:j] + ".toMediaType()" + data[j:]
                    i = data.find("MediaType.parse")

            # persist data
            with open(curFile, "w") as f:
                f.write(data)

    # create a dict with all extensions data
    if lang not in extensions:
        extensions[lang] = []
    dat = parseGradle(os.path.join(lang, ext, "build.gradle"))
    dat.update({"name": ext})
    extensions[lang].append(dat)


# process
for lang in os.listdir("./"):
    if os.path.isdir(lang):
        for ext in os.listdir(lang):
            if os.path.isdir(os.path.join(lang, ext)):
                fixKotlin(lang, ext)

# save file with list of extensions
with open("extensions.json", "w") as f:
    json.dump(extensions, f)