package nl.toefel.aoc2020.day5

import java.io.File
import kotlin.math.roundToInt

data class Seat(val row: Int, val col: Int){
    val id = row * 8 + col
}

fun binarySearch(range: IntRange, input: String): Int =
    when {
        input.isEmpty() -> range.first
        input[0] == 'F' || input[0] == 'L' -> binarySearch(range.first..(range.last - range.middle()), input.substring(1))
        input[0] == 'B' || input[0] == 'R' -> binarySearch((range.first + range.middle())..range.last, input.substring(1))
        else -> throw IllegalStateException("")
    }

fun IntRange.middle(): Int = ((this.last - this.first).toDouble() / 2.0).roundToInt()

fun main() {
    val seats = File(ClassLoader.getSystemResource("day5.txt").file).readLines()
        .map { Seat(binarySearch(0..127, it.substring(0, 7)), binarySearch(0..7, it.substring(7))) }

    println(seats.map { it.id }.max())

    val rowWithMissingSeat: List<Seat> = seats.groupBy { it.row }.values.filter { it.size in 7..7 }.first()
    val allSeatsForThatRow = (0..7).map { rowWithMissingSeat.first().copy(col = it) }
    val missingSeat = allSeatsForThatRow.minus(rowWithMissingSeat)

    println(missingSeat.first().id)
}