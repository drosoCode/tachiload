package tachiload.app

fun main(args: Array<String>) {
    val configPath = "/app/config.json"
    val downloadPath = "/app/downloads"

    if (args.isNotEmpty()) {
        CLI(configPath, args)
    } else {
        println("========================================== Tachiload ==========================================")
        Download(configPath, downloadPath).update()
    }
}