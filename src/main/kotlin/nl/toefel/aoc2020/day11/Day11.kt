package nl.toefel.aoc2020.day11

import java.io.File
import java.lang.ClassLoader.getSystemResource

val adjacentSeatDeltas = listOf(-1 to -1, -1 to 0, -1 to 1, 1 to -1, 1 to 0, 1 to 1, 0 to -1, 0 to 1)
fun List<String>.adjacentSeats(row: Int, col: Int) = adjacentSeatDeltas.mapNotNull { (rowOffset, colOffset) -> this.getOrNull(row + rowOffset)?.getOrNull(col + colOffset) }
fun List<String>.print() { this.forEach { row ->row.forEach {print(it) } ; println() }; println() }

fun main() {
    val input: List<String> = File(getSystemResource("day11-test.txt").file).readLines()
    input.print()

    val round1 = processRound(input).also { it.print() }
    val round2 = processRound(round1).also { it.print() }
    val round3 = processRound(round2).also { it.print() }

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
