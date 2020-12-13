package nl.toefel.aoc2020.day10

import java.io.File
import java.lang.ClassLoader.getSystemResource

fun main() {
    val jolts = File(getSystemResource("day10.txt").file).readLines()
            .map { it.toLong() }
            .sorted()
            .let { listOf(0L) + it + (it.last() + 3) }
    val (ones, threes) = jolts.windowed(2).map { (a, b) -> b - a }.filter { it == 1L || it == 3L }.partition { it == 1L }
    println(ones.size * threes.size)
    println(jolts.windowed(2).map { (a, b) -> "${b - a}"}.joinToString("").split("3+".toRegex()).map { when(it) {
        "" -> 1L
        "1" -> 1L
        "11" -> 2L
        "111" -> 4L
        "1111" -> 7L
        else -> throw RuntimeException()
    } }.reduce {acc: Long, i: Long ->acc * i })
}
