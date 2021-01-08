package tachiload.app

fun main(args: Array<String>) {
    println("========================================== Tachiload ==========================================")

    val configPath = "/app/config.json"
    val downloadPath = "/app/downloads"

    val downloader = Download(configPath, downloadPath)
    downloader.update()
}