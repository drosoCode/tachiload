package tachiload.app

import com.microsoft.playwright.*

object SBrowser {
    var pw:Playwright
    var bw:Browser

    init {
        println("Initializing Browser ...")
        this.pw = Playwright.create()
        this.bw = this.pw.firefox().launch()
    }

    fun getContext(): BrowserContext {
        return this.bw.newContext()
    }

    fun close() {
        this.bw.close()
        this.pw.close()
    }
}