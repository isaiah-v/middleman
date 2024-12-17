package org.ivcode.middleman.tools.utils

import java.io.InputStream

/**
 * Executes a command and redirects the output to the console.
 */
fun List<String>.exec() {
    println(this.joinToString(" "))

    val process = ProcessBuilder(*(this.toTypedArray()))
        .inheritIO()
        .start()

    val tread = Thread {
        if(process.isAlive) {
            process.destroyForcibly()
        }
    }
    try {
        Runtime.getRuntime().addShutdownHook(tread)

        val code = process.waitFor()

        if(code != 0) {
            // error stream is already redirected to the console
            throw RuntimeException("error")
        }
    } catch (e: Throwable) {
        process.destroyForcibly()
        throw RuntimeException(e)
    } finally {
        Runtime.getRuntime().removeShutdownHook(tread)
    }
}

/**
 * Executes a that isn't expected to exit, and will execute a block of code when it does.
 */
fun List<String>.exec(onExit: ()->Unit) {
    Runtime.getRuntime().addShutdownHook(Thread(onExit))
    exec()

    throw RuntimeException("Unexpected Exit")
}

/**
 * Executes a command and returns the output.
 */
fun <T> List<String>.exec(parser: CmdParser<T>): T {
    val process = ProcessBuilder(*(this.toTypedArray()))
        .start()

    val tread = Thread {
        if(process.isAlive) {
            process.destroyForcibly()
        }
    }
    try {
        Runtime.getRuntime().addShutdownHook(tread)

        val code = process.waitFor()

        val value = parser.parse(process.inputStream)

        if (code != 0) {
            val error = StringCmdParser().parse(process.errorStream)
            throw RuntimeException(error)
        }
        return value
    } catch (e: Throwable) {
        process.destroyForcibly()
        throw RuntimeException(e)
    } finally {
        Runtime.getRuntime().removeShutdownHook(tread)
    }
}

/**
 * Parses the output of a command.
 */
interface CmdParser<T> {
    fun parse(inputStream: InputStream): T
}

/**
 * Parses the output of a command as a string.
 */
class StringCmdParser : CmdParser<String> {
    override fun parse(inputStream: InputStream): String {
        inputStream.bufferedReader().use {
            return it.readText()
        }
    }
}