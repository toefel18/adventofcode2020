package nl.toefel.aoc2020.day17

import java.io.File
import java.lang.ClassLoader.getSystemResource
import kotlin.math.min
import kotlin.math.max

data class Point4D(val x: Int, val y: Int, val z: Int) {

}


data class Point3D(val x: Int, val y: Int, val z: Int) {
    val withNeighbors: List<Point3D> by lazy {
        (x - 1..x + 1).flatMap { iX -> (y - 1..y + 1).flatMap { iY -> (z - 1..z + 1).map { iZ -> Point3D(iX, iY, iZ) } } }
    }
    val neighbors: List<Point3D> get() = withNeighbors - this
}

data class Cube(val active: Boolean) {
    override fun toString(): String = if (active) "#" else "."
}

class Universe(val map: Map<Point3D, Cube>, val cycle: Int = 0) : Map<Point3D, Cube> by map {
    val minBound: Point3D get() = keys.reduce { acc, next -> Point3D(min(acc.x, next.x), min(acc.y, next.y), min(acc.z, next.z)) }
    val maxBound: Point3D get() = keys.reduce { acc, next -> Point3D(max(acc.x, next.x), max(acc.y, next.y), max(acc.z, next.z)) }

    fun print() {
        val (min, max) = (minBound to maxBound)
        val zSlices = (min.z..max.z).map { z ->
            (min.y..max.y).joinToString("\n") { y ->
                (min.x..max.x).joinToString("") { x ->
                    this.getOrDefault(Point3D(x, y, z), Cube(false)).toString()
                }
            }.let { "z=$z\n$it" }
        }
        println("cycle $cycle, alive = ${values.count { it.active }}")
        zSlices.map { it.split('\n') }
                .reduce { acc, slice -> acc.zip(slice).map { "${it.first.padEnd(max.x - min.x + 2)} ${it.second.padEnd(max.x - min.x + 2)}" } }
                .forEach { println(it) }
    }

    fun nextCycle(): Universe {
        return keys.flatMap { it.withNeighbors }
                .toSet()
                .associateWith { pos: Point3D ->
                    val active = this[pos]?.active == true
                    val activeNeighbors = pos.neighbors.count { this[it]?.active == true }
                    Cube((active && (activeNeighbors == 2 || activeNeighbors == 3)) || !active && activeNeighbors == 3)
                }
                .let { Universe(it, cycle = cycle + 1) }
    }
}

fun main() {
    val input = File(getSystemResource("day17.txt").file).readLines()
            .flatMapIndexed { y, row -> row.mapIndexed { x, state -> Point3D(x, y, 0) to Cube(state == '#') } }
            .toMap()

    val life = generateSequence(Universe(input)) { it.nextCycle() }
    life.take(7).forEach { it.print() }
}
