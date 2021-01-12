package tachiload.app

import kotlin.system.exitProcess
import it.sauronsoftware.cron4j.Scheduler
import java.time.LocalDateTime
import kotlinx.cli.*


fun main(args: Array<String>) {

    if(args.isNotEmpty() && (args[0] == "--extensions" || args[0] == "--search" || args[0] == "--download"))
    {
        CLI("./config.json", "./downloads", args)
    }
    else
    {
        val parser = ArgParser("Tachiload")
        val cron by parser.option(ArgType.String, shortName = "c", description = "Cron Expression")
        val webhook by parser.option(ArgType.String, shortName = "wh", description = "Webhook URL (discord) for notification")
        val configPath by parser.option(ArgType.String, shortName = "cfg", description = "Path to configuration file").default("./config.json")
        val downloadPath by parser.option(ArgType.String, shortName = "dl", description = "Path to download directory").default("./downloads")
        parser.parse(args)

        if(cron != null)
        {
            println("========================================== Tachiload [CRON] ==========================================")
            val s = Scheduler()
            s.schedule(cron, Runnable() {
                println("Running update at:"+LocalDateTime.now())
                Download(configPath, downloadPath, webhook).update()
            })
            s.start()
            try {
                Thread.sleep(Long.MAX_VALUE)
            } catch (e: InterruptedException) {
            }
        }
        else
        {
            println("========================================== Tachiload ==========================================")
            Download(configPath, downloadPath, webhook).update()
        }
    }
    exitProcess(0)
}
