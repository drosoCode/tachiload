package tachiload.tachiyomi.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.stream.Collectors

import java.util.ArrayList


class CustomCookieJar : CookieJar {
    private val storage: MutableList<Cookie> = ArrayList()
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        storage.addAll(cookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {

        // Remove expired Cookies
        //storage.removeIf { cookie: Cookie -> cookie.expiresAt() < System.currentTimeMillis() }

        // Only return matching Cookies
        return storage.stream().filter { cookie: Cookie ->
            cookie.matches(
                url
            )
        }.collect(Collectors.toList())
    }

    fun get(url: HttpUrl): List<Cookie> {

        // Remove expired Cookies
        //storage.removeIf { cookie: Cookie -> cookie.expiresAt() < System.currentTimeMillis() }

        // Only return matching Cookies
        return storage.stream().filter { cookie: Cookie ->
            cookie.matches(
                url
            )
        }.collect(Collectors.toList())
    }

    fun remove(url: HttpUrl, cookieNames: List<String>? = null, maxAge: Int = -1) {
        val urlString = url.toString()
    }

}