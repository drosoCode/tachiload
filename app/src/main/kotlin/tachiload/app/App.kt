package tachiload.app

import kotlin.system.exitProcess
import it.sauronsoftware.cron4j.Scheduler
import java.time.LocalDateTime

fun main(args: Array<String>) {
    val configPath = "./config.json"
    val downloadPath = "./downloads"

    if (args.isNotEmpty()) {
        if(args[0] == "--cron" && args.size == 2)
        {
            println("========================================== Tachiload [CRON] ==========================================")
            val s = Scheduler()
            s.schedule(args[1], Runnable() {
                println("Running update at:"+LocalDateTime.now())
                Download(configPath, downloadPath).update()
            })
            s.start()
            try {
                Thread.sleep(Long.MAX_VALUE)
            } catch (e: InterruptedException) {
            }
        }
        else
        {
            CLI(configPath, args)
        }
    } else {
        println("========================================== Tachiload ==========================================")
        Download(configPath, downloadPath).update()
    }
    exitProcess(0)
}