import db.execute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ql.QLExpressionParser
import java.io.FileNotFoundException
import kotlin.system.exitProcess

fun main() {
    runBlocking {
        val receiveChannel = Channel<String>()
        launch(Dispatchers.IO) {
            while (true) {
                val answer = receiveChannel.receive()
                if (answer != "EXIT") {
                    try {
                        execute(QLExpressionParser(answer).parseTerminate())
                    } catch (ex: FileNotFoundException) {
                        println("Server closed a connection: $ex")
                        receiveChannel.close()
                        exitProcess(0)
                    }
                } else {
                    println("Server closed a connection")
                    receiveChannel.close()
                    exitProcess(0)
                }
            }
        }

        println("> ")
        var line = readlnOrNull()
        while (!line.isNullOrBlank()) {
            receiveChannel.send(line)
            print("> ")
            line = readln()
        }
        receiveChannel.send("EXIT")
    }
}

