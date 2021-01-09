package tachiload.app

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tachiload.tachiyomi.source.SourceFactory
import tachiload.tachiyomi.source.online.HttpSource
import java.nio.file.Files
import java.nio.file.Paths

class Helpers {
    companion object {
        fun loadExtension(index: Map<String, List<ExtensionsIndex>>, lang: String, ext: String): HttpSource? {
            val extClass = index[lang]!!.find { it.name == ext }?.extClass
            val instance = this.javaClass.classLoader.loadClass("tachiload.extension.$lang.$ext.src.eu.kanade.tachiyomi.extension.$lang.$ext$extClass")
                .getDeclaredConstructor()
                .newInstance()

            when (instance) {
                is HttpSource -> return instance
                is SourceFactory -> return instance.createSources()[0] as HttpSource
            }
            return null
        }

        fun loadIndex(): Map<String, List<ExtensionsIndex>> {
            return Gson().fromJson(
                this::class.java.classLoader.getResource("extensions.json").readText(),
                object: TypeToken<Map<String, List<ExtensionsIndex>>>() {}.type
            )
        }

        fun loadItems(configPath: String): List<ConfigItem> {
            return Gson().fromJson(
                Files.newBufferedReader(Paths.get(configPath)),
                object: TypeToken<List<ConfigItem>>() {}.type
            )
        }
    }
}