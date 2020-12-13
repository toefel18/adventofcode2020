package nl.toefel.aoc2020.day11

import java.io.File
import java.lang.ClassLoader.getSystemResource

data class Point(val x: Int, val y: Int) {
    operator fun plus (other: Point) : Point = Point(x + other.x, y + other.y)
}

val adjacentSeatDeltas = listOf(Point(-1, -1), Point(-1, 0), Point(-1, 1), Point(1, -1), Point(1, 0), Point(1, 1), Point(0, -1), Point(0, 1))

fun List<String>.adjacentSeats(row: Int, col: Int): List<Char> = adjacentSeatDeltas.mapNotNull { (colOffset, rowOffset) -> this.getOrNull(row + rowOffset)?.getOrNull(col + colOffset) }
fun List<String>.visibleSeats(row: Int, col: Int): List<Char> = adjacentSeatDeltas.mapNotNull { direction: Point ->
    generateSequence(direction) { it + direction}
        .find { vec -> this.getOrNull(row + vec.y)?.getOrNull(col + vec.x)?.let { it =='L' || it == '#' } ?: true }
        ?.let { vec -> this.getOrNull(row + vec.y)?.getOrNull(col + vec.x) }

}
fun List<String>.print() { this.forEach { row ->row.forEach {print(it) } ; println() }; println() }

fun main() {
    val input: List<String> = File(getSystemResource("day11.txt").file).readLines()
    input.print()

    loop(input) { current -> processRound(current)}
    loop(input) { current -> processRoundV2(current)}
}

private fun loop(input: List<String>, process: (List<String>) -> List<String>) {
    var previous: List<String>
    var current: List<String> = input
    do {
        previous = current
        current = process(current)
    } while (previous != current)
    println(current.sumOf { seats -> seats.count { it == '#' } })
}

fun processRound(seatPlan: List<String>): List<String> {
    return seatPlan.mapIndexed{row, seats -> seats.mapIndexed { col, seat ->
        when(seat) {
            '.' -> '.'
            'L' -> if (seatPlan.adjacentSeats(row, col).count { it == '#' } == 0) '#' else 'L'
            '#' -> if (seatPlan.adjacentSeats(row, col).count { it == '#' } >= 4) 'L' else '#'
            else -> throw Exception("unknown symbol $seat")
        }
    }.joinToString("")}
}

fun processRoundV2(seatPlan: List<String>): List<String> {
    return seatPlan.mapIndexed{row, seats -> seats.mapIndexed { col, seat ->
        when(seat) {
            '.' -> '.'
            'L' -> if (seatPlan.visibleSeats(row, col).count { it == '#' } == 0) '#' else 'L'
            '#' -> if (seatPlan.visibleSeats(row, col).count { it == '#' } >= 5) 'L' else '#'
            else -> throw Exception("unknown symbol $seat")
        }
    }.joinToString("")}
}