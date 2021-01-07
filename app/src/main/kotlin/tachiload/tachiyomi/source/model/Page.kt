package tachiload.tachiyomi.source.model

import java.net.URI

class Page(
    val index: Int,
    val url: String = "",
    var imageUrl: String? = null,
    var uri: URI? = null
)
