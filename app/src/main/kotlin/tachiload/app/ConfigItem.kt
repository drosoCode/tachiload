package tachiload.app

import tachiload.tachiyomi.source.model.SMangaImpl

data class ConfigItem(
    val extension: String,
    val language: String,
    val data: SMangaImpl
)
