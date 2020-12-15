package nl.toefel.aoc2020.day12

import nl.toefel.aoc2020.day12.Direction.N
import java.io.File
import java.lang.ClassLoader.getSystemResource
import kotlin.math.absoluteValue

enum class Direction(val degrees: Int, val delta: Point) {
    N(0, Point(0, -1)),
    E(90, Point(1, 0)),
    S(180, Point(0, 1)),
    W(270, Point(-1, 0));

    fun rotateLeft(degreesToRotate: Int): Direction =
            ofDegrees((degrees + (-degreesToRotate % -360)).let { if (it < 0) 360 + it else it } % 360)

    fun rotateRight(degreesToRotate: Int): Direction =
            ofDegrees((degrees + degreesToRotate) % 360)

    companion object {
        fun ofDegrees(degrees: Int): Direction = values().find { it.degrees == degrees }
                ?: throw IllegalArgumentException("no direction at $degrees degrees")
    }
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
    operator fun times(unit: Int): Point = Point(x * unit, y * unit)
    fun move(units: Int, direction: Direction): Point = this + (direction.delta * units)
    fun rotateClockwise(degrees: Int): Point = (0 until degrees/90).fold(this) {acc, _ -> Point(-acc.y, acc.x) }
    fun rotateCounterClockwise(degrees: Int): Point = (0 until degrees/90).fold(this) {acc, _ -> Point(acc.y, -acc.x) }
}

fun main() {
    val input = File(getSystemResource("day12.txt").file).readLines().map { it[0].toString() to it.substring(1).toInt() }
    var pos = Point(0, 0)
    var dir = Direction.E

    input.forEach { (action, units) ->
        when (action) {
            in "NESW" -> pos = pos.move(units, Direction.valueOf(action))
            "L" -> dir = dir.rotateLeft(units)
            "R" -> dir = dir.rotateRight(units)
            "F" -> pos = pos.move(units, dir)
        }
    }

    println(pos.let { it.x + it.y })

    var waypoint = Point(0,0).move(10, Direction.E).move(1, N)
    pos = Point(0, 0)
    input.forEach { (action, units) ->
        when (action) {
            in "NESW" -> waypoint = waypoint.move(units, Direction.valueOf(action))
            "L" -> waypoint = waypoint.rotateCounterClockwise(units)
            "R" -> waypoint = waypoint.rotateClockwise(units)
            "F" -> pos += waypoint.times(units)
        }
    }
    println(pos.let { it.x.absoluteValue + it.y.absoluteValue })
}
