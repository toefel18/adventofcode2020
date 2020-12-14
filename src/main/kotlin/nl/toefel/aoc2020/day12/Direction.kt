package nl.toefel.aoc2020.day12

import java.lang.IllegalArgumentException

enum class Direction(val degrees: Int) {
    NORTH(0), EAST(90), SOUTH(180), WEST(270);

    fun rotateLeft(degreesToRotate: Int):Direction {
        val newDegrees = (degrees + (-degreesToRotate% -360)).let { if (it < 0) 360+it else it }
        return ofDegrees(newDegrees)
    }
    fun rotateRight(degreesToRotate: Int) : Direction {
        val newDegrees = degrees + (degreesToRotate % 360)
        return ofDegrees(newDegrees)
    }

    companion object {
        @JvmStatic
        fun ofDegrees(degrees: Int): Direction = values().find { it.degrees == degrees } ?: throw IllegalArgumentException("no direction at $degrees degrees")
    }
}