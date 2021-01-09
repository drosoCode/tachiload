package tachiload.app

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val configPath = "/app/config.json"
    val downloadPath = "/app/downloads"

    if (args.isNotEmpty()) {
        CLI(configPath, args)
    } else {
        println("========================================== Tachiload ==========================================")
        Download(configPath, downloadPath).update()
    }
    exitProcess(0)
}