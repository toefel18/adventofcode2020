package nl.toefel.aoc2020.day8

import java.io.File

data class Op(val name: String, val input: Long)

operator fun List<Op>.get(i: Long) = this[i.toInt()]

fun main() {
    val instr: List<Op> = File(ClassLoader.getSystemResource("day8.txt").file).readLines()
            .map { Op(it.substring(0, 3), it.substring(4).toLong()) }

    runProgram(instr).also { (_, acc) -> println(acc) }

    instr.asSequence()
            .mapIndexedNotNull { i, op -> if (op.name in listOf("nop", "jmp")) i else null }
            .map { i ->
                val altered = instr.toMutableList()
                altered[i] = Op(if (altered[i].name == "jmp") "nop" else "jmp", altered[i].input)
                runProgram(altered)
            }
            .find { (success, _) -> success }
            ?.let { println("found working program: ${it.second}") }
            ?: throw IllegalStateException("None found")
}

// returns (success to lastValueOfAccumulator)
fun runProgram(instr: List<Op>): Pair<Boolean, Long> {
    val seen = mutableSetOf<Long>()
    var acc = 0L
    var ip = 0L
    while (ip < instr.size) {
        if (ip in seen) return false to acc
        seen += ip
        val op = instr[ip]
        when (op.name) {
            "nop" -> {}
            "acc" -> acc += op.input
            "jmp" -> {
                ip += op.input
                continue
            }
            else -> throw IllegalStateException("unknown instr ${op.name}")
        }
        ip++
    }
    return true to acc
}