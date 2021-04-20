package tachiload.tachiyomi.network

import tachiload.tachiyomi.network.CustomCookieJar
import tachiload.tachiyomi.network.UserAgentInterceptor

import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.io.File
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class NetworkHelper() {

    private val cacheDirPath: String = ".cache";

    private val cacheDir = File(cacheDirPath, "network_cache")

    private val cacheSize = 5L * 1024 * 1024 // 5 MiB

    val cookieManager = CustomCookieJar()

    val client by lazy {
        val builder = OkHttpClient.Builder()
            .cookieJar(cookieManager)
            .cache(Cache(cacheDir, cacheSize))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(UserAgentInterceptor())

        /*if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
            builder.addInterceptor(httpLoggingInterceptor)
        }*/

        if (true) { // preferences.enableDoh()
            builder.dns(
                DnsOverHttps.Builder().client(builder.build())
                    .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
                    .bootstrapDnsHosts(
                        listOf(
                            InetAddress.getByName("162.159.36.1"),
                            InetAddress.getByName("162.159.46.1"),
                            InetAddress.getByName("1.1.1.1"),
                            InetAddress.getByName("1.0.0.1"),
                            InetAddress.getByName("162.159.132.53"),
                            InetAddress.getByName("2606:4700:4700::1111"),
                            InetAddress.getByName("2606:4700:4700::1001"),
                            InetAddress.getByName("2606:4700:4700::0064"),
                            InetAddress.getByName("2606:4700:4700::6400")
                        )
                    )
                    .build()
            )
        }

        builder.build()
    }

    val cloudflareClient by lazy {
        client.newBuilder()
            .addInterceptor(CloudflareInterceptor())
            .build()
        client
    }
}
