/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package tachiload.app

import tachiload.tachiyomi.source.model.FilterList
//import tachiload.extension.en.mangasee.src.eu.kanade.tachiyomi.extension.en.mangasee.Mangasee
import tachiload.extension.all.webtoons.src.eu.kanade.tachiyomi.extension.all.webtoons.WebtoonsFactory
import tachiload.tachiyomi.source.online.HttpSource

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main(args: Array<String>) {
    println(App().greeting)
    //val ms = Mangasee()

    val ms = WebtoonsFactory().createSources()[0] as HttpSource


    println("popular ============================================")
    ms.fetchPopularManga(1).subscribe { value ->
        for (m in value.mangas) {
            println(m.title)
        }
    }

    println("latest updates ============================================")
    ms.fetchLatestUpdates(1).subscribe { value ->
        for (m in value.mangas) {
            println(m.title)
        }
    }

    println("search ============================================")
    ms.fetchSearchManga(1, "hero academia", FilterList()).subscribe { value ->
            for (m in value.mangas) {
                println(m.title)
            }
    }

    println("fetch chapters ============================================")
    ms.fetchSearchManga(1, "hero academia", FilterList()).subscribe { value ->
        ms.fetchChapterList(value.mangas[0]).subscribe { chapters ->
            println(chapters)
            println("fetch pages ============================================")
            ms.fetchPageList(chapters[0]).subscribe { pages ->
                println(pages)
                println("one page: ============================================")
                println(pages[0].index)
                println(pages[0].imageUrl)
            }
        }
    }

}