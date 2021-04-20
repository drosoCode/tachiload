package tachiload.tachiyomi.network

import tachiload.tachiyomi.source.online.HttpSource
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import tachiload.app.SBrowser
import com.microsoft.playwright.*
import java.nio.file.Paths

class CloudflareInterceptor : Interceptor {

    init {
        println("/////////////////////////////////////////////")
    }

    private val networkHelper = NetworkHelper()
    private lateinit var browser: BrowserContext

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        println("-------------------------------------------------")
        browser = SBrowser.getContext()

        val originalRequest = chain.request()

        val response = chain.proceed(originalRequest)

        // Check if Cloudflare anti-bot is on
        if (response.code != 503 || response.header("Server") !in SERVER_CHECK) {
            return response
        }

        try {
            response.close()
            networkHelper.cookieManager.remove(originalRequest.url, COOKIE_NAMES, 0)
            val oldCookie = networkHelper.cookieManager.get(originalRequest.url).firstOrNull { it.name == "cf_clearance" }
            resolveWithWebView(originalRequest, oldCookie)

            return chain.proceed(originalRequest)
        } catch (e: Exception) {
            // Because OkHttp's enqueue only handles IOExceptions, wrap the exception so that
            // we don't crash the entire app
            throw IOException(e)
        }
    }

    private fun resolveWithWebView(request: Request, oldCookie: Cookie?) {

        val origRequestUrl = request.url.toString()
        val headers = request.headers.toMultimap().mapValues { it.value.getOrNull(0) ?: "" }.toMutableMap()
        //headers["X-Requested-With"] = WebViewUtil.REQUESTED_WITH
        println("aaaaaaaaaaaaaaaaa")
        browser.setExtraHTTPHeaders(headers)
        println("bbbbbbbbbbbbbbbbbbbbbbbb")
        val page = browser.newPage()
        println("ccccccccccccccccccccccccc")
        page.navigate(origRequestUrl)
        println("ddddddddddddddddddddddddddd")
        val opts = Page.ScreenshotOptions()
        println("eeeeeeeeeeeeeeeeeeeeeeeee")
        page.screenshot(opts.withPath(Paths.get("test.png")))
        println("fffffffffffffffffffffffffffff")
        val cookies = browser.cookies()
        println("ggggggggggggggggggggggggggggg")
        browser.close()
        println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh")

        for(c in cookies)
        {
            println(c)
        }

        /*if( networkHelper.cookieManager.get(origRequestUrl.toHttpUrl()).firstOrNull { it.name == "cf_clearance" }.let { it != null && it != oldCookie })
        {
            // cloudflare bypassed
        }
        else
        {
            // Throw exception if we failed to bypass Cloudflare
            throw Exception("Unable to bypass cloudflare")
        }*/
    }

    companion object {
        private val SERVER_CHECK = arrayOf("cloudflare-nginx", "cloudflare")
        private val COOKIE_NAMES = listOf("cf_clearance")
    }
}
