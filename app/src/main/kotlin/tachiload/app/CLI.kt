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
        if (this.args[0] == "--extensions" && this.args.size == 1) {
            print(this.extList())
        } else if(this.args[0] == "--search" && this.args.size >= 4) {
            print(this.search())
        } else {
            print("Error")
        }
    }

    private fun extList(): String {
        return this::class.java.classLoader.getResource("extensions.json").readText()
    }

    private fun search(): String {
        //args: --search [name] [lang_ext1] [name_ext1] [lang_ext2] [name_ext2]
        var lst = mutableListOf<ConfigItem>()
        for (i in 2 until args.size step 2)
        {
            val ext = Helpers.loadExtension(this.index, this.args[i], this.args[i+1]) ?: return "Error"

            var page = 1
            var nextPage = true
            while(nextPage) {
                val value = ext.fetchSearchManga(page, args[1], FilterList()).toBlocking().first()
                for (m in value.mangas) {
                    lst.add(ConfigItem(this.args[i+1], this.args[i], m as SMangaImpl))
                }
                if(value.hasNextPage)
                    page++
                else
                    nextPage = false
            }
        }
        return Gson().toJson(lst)
    }

}