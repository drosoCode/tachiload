package tachiload.app

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tachiload.tachiyomi.source.model.SChapter
import tachiload.tachiyomi.source.model.SManga
import tachiload.tachiyomi.source.online.HttpSource
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths

class Download(private val configPath: String, private val downloadPath: String) {

    private var index = Helpers.loadIndex()
    private var items = Helpers.loadItems(configPath)

    private fun downloadChapter(extension: HttpSource, chapter: SChapter, title: String, number: Int) {
        println("  Downloading chapter $number")
        extension.fetchPageList(chapter).subscribe { pages ->
            for ((i, p) in pages.withIndex()) {
                extension.fetchImage(p).subscribe { value ->
                    val p = this.downloadPath+"/$title/$number/"
                    val directory = File(p)
                    if (!directory.exists()) {
                        directory.mkdirs()
                    }
                    val fos = FileOutputStream("$p$i.png")
                    fos.write(value.body!!.bytes())
                    fos.close()
                }
            }
        }
    }

    private fun downloadNewChapters(extension: HttpSource?, manga: SManga, latestChapter: Int) {
        if (extension == null)
            return
        println("  Found $latestChapter chapters")
        extension.fetchChapterList(manga).subscribe { chapters ->
            if(latestChapter < 0)
            {
                for ((i, c) in chapters.withIndex()) {
                    this.downloadChapter(extension, c, manga.title, i+1)
                }
            }
            else if (chapters.size - latestChapter > 0)
            {
                for (i in 0 until chapters.size-latestChapter) {
                    this.downloadChapter(extension, chapters[i], manga.title, latestChapter+i+1)
                }
            }
        }
    }

    private fun getLatestChapter(title: String): Int
    {
        val dir = File(this.downloadPath+"/"+title)
        return if (!dir.exists()) {
            -1
        } else {
            val lst = dir.list()
            var i = -1
            for (a in lst) {
                if (a.toInt() > i)
                    i = a.toInt()
            }
            i
        }
    }

    fun update()
    {
        for (manga in this.items)
        {
            println("Updating " + manga.data.title + " ...")
            this.downloadNewChapters(
                Helpers.loadExtension(this.index, manga.language, manga.extension),
                manga.data,
                this.getLatestChapter(manga.data.title)
            )
        }
    }
}