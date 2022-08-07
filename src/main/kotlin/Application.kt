import domain.api.LocalExecutor
import domain.layout.PackedLayout
import domain.ql.ExpressionParser
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import java.io.FileNotFoundException
import kotlin.system.exitProcess

suspend fun serve() = withContext(Dispatchers.IO) {
    val executor = LocalExecutor()
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", 9002)
    val socket = serverSocket.accept()
    val receiveChannel = socket.openReadChannel()
    val sendChannel = socket.openWriteChannel(autoFlush = true)
    val layout = PackedLayout()
    println("Started a server.")
    while (true) {
        val answer = receiveChannel.readUTF8Line().orEmpty()
        if (answer.isEmpty()) delay(50)
        else if (answer != "EXIT") {
            try {
                launch(CoroutineName("executing $answer")) {
                    executor.execute(ExpressionParser(answer).parseTerminate(), sendChannel, layout)
                }
            } catch (ex: FileNotFoundException) {
                println("Server closed a connection: $ex")
                socket.close()
                selectorManager.close()
                exitProcess(0)
            }
        } else {
            println("Server closed a connection")
            socket.close()
            selectorManager.close()
            exitProcess(0)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    runBlocking {
        launch(newSingleThreadContext("ServerThread")) {
            serve()
        }
        val selectorManager = ActorSelectorManager(Dispatchers.IO)
        val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 9002)

        val receiveChannel = socket.openReadChannel()
        val sendChannel = socket.openWriteChannel(autoFlush = true)

        launch(Dispatchers.IO) {
            while (true) {
                val answer = receiveChannel.readUTF8Line()
                if (answer != null) {
                    println(answer)
                } else {
                    println("Server closed a connection")
                    socket.close()
                    selectorManager.close()
                    exitProcess(0)
                }
            }
        }
        while (true) {
            delay(100)
            print("> ")
            val myMessage = readln()
            if (myMessage.lowercase() == "exit") exitProcess(0)
            sendChannel.writeStringUtf8("$myMessage\n")
        }
    }
}

