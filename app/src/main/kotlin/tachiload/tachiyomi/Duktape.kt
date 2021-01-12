package tachiload.tachiyomi

import tachiload.app.SBrowser
import com.microsoft.playwright.*

class Duktape(val c:BrowserContext) {
    val p:Page = c.newPage()

    companion object {
        fun create(): Duktape {
            return Duktape(SBrowser.getContext())
        }
    }

    fun evaluate(script: String): String {
        return this.p.evaluate(script).toString()
    }

    fun close() {
        this.p.close()
        this.c.close()
    }

}