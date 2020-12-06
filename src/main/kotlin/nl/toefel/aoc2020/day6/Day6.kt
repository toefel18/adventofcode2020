package nl.toefel.aoc2020.day6

import java.io.File

fun main() {
    val groups: List<List<Set<Char>>> = File(ClassLoader.getSystemResource("day6.txt").file).readText().split("\n\n")
        .map { group -> group.split("\n").map { line -> line.toSet() } }

    println(groups.sumBy { it.reduce { acc, set -> acc + set }.size })
    println(groups.sumBy { it.reduce { acc, person -> acc.intersect(person) }.size })
}