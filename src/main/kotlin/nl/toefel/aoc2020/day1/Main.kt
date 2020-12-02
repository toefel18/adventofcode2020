package nl.toefel.aoc2020.day1

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("day1.txt").file).readLines().map { it.toLong() }
    val tuplesAddingUpTo2020 =
        input.flatMap { first -> input.mapNotNull { second -> if (first + second == 2020L) (first to second) else null } }

    println("part 1 = ${tuplesAddingUpTo2020.map { it.first * it.second }.first()}")

    val options: Sequence<Triple<Int, Int, Int>> =
        input.indices.asSequence().flatMap { firstIdx ->
            input.indices.asSequence().flatMap { secondIdx ->
                input.indices.asSequence().map { thirdIdx -> Triple(firstIdx, secondIdx, thirdIdx) }
            }
        }

    val result = options
        .find { input[it.first] + input[it.second] + input[it.third] == 2020L }
        ?.let { input[it.first] * input[it.second] * input[it.third] }

    println("part 2 = $result")
}

