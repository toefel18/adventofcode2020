package nl.toefel.aoc2020.day9

import java.io.File
import java.lang.ClassLoader.getSystemResource

fun <T> List<T>.combineValues(start: Int, endExclusive: Int): Sequence<Pair<T, T>> =
       (start until endExclusive).asSequence().flatMap { x ->
            (x until endExclusive).asSequence().map { y ->
                this[x] to this[y]
            }
        }

fun main() {
    val nrs = File(getSystemResource("day9.txt").file).readLines().map { it.toLong() }
    val preambleSize = 25
    val firstInvalidNumber = (preambleSize until nrs.size)
            .find { nrs.combineValues(it - preambleSize, it).none { (x, y) -> x + y == nrs[it] } }
            ?.let { nrs[it] }
    println(firstInvalidNumber)

    val accumulated = nrs.runningReduce {acc, l -> acc + l }
    val part2 = accumulated.asSequence()
            .flatMapIndexed { x, xValue -> accumulated.asSequence().mapIndexed { y, yValue -> (xValue - yValue) to y+1..x } }
            .find { (sumOfRange, _) -> sumOfRange == firstInvalidNumber!! }
            ?.let { (_, range) -> range.minOf{ nrs[it] } + range.maxOf { nrs[it] } }
    println(part2)
}