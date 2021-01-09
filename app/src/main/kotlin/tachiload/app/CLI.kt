package tachiload.app

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.file.Files
import java.nio.file.Paths
import tachiload.tachiyomi.source.model.FilterList
import tachiload.tachiyomi.source.model.SMangaImpl


class CLI(private val configPath: String, private val args: Array<String>) {
    private var items: List<ConfigItem> = Gson().fromJson(
        Files.newBufferedReader(Paths.get(configPath)),
        object: TypeToken<List<ConfigItem>>() {}.type
    )
    private var index: Map<String, List<ExtensionsIndex>> = Gson().fromJson(
        this::class.java.classLoader.getResource("extensions.json").readText(),
        object: TypeToken<Map<String, List<ExtensionsIndex>>>() {}.type
    )

    init {
        if (this.args[0] == "--extList" && this.args.size == 1) {
            this.extList()
        } else if(this.args[0] == "--search" && this.args.size >= 4) {
            this.search()
        } else {
            println("Error")
        }
    }

    private fun extList() {
        println(this::class.java.classLoader.getResource("extensions.json").readText())
    }

    private fun search() {
        //args: --search [name] [lang_ext1] [name_ext1] [lang_ext2] [name_ext2]
        var lst = mutableListOf<ConfigItem>()
        for (i in 2 until args.size step 2)
        {
            val ext = Helpers.loadExtension(this.index, this.args[i], this.args[i+1])
            if (ext == null) {
                println("Error")
                return
            }

            var page = 1
            var nextPage = true
            while(nextPage) {
                val value = ext.fetchSearchManga(page, args[1], FilterList()).toBlocking().first()
                println(value)
                for (m in value.mangas) {
                    lst.add(ConfigItem(this.args[i], this.args[i+1], m as SMangaImpl))
                }
                if(value.hasNextPage)
                    page++
                else
                    nextPage = false
            }
        }
        println(Gson().toJson(lst))
    }

}