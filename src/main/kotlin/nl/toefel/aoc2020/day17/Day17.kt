package nl.toefel.aoc2020.day17

import java.io.File

interface Point {
    val withNeighbors: List<Point>
    val neighbors: List<Point>
}

interface IPoint3D : Point{
    val x : Int
    val y : Int
    val z : Int
}

data class Point3D(override val x: Int, override val y: Int, override val z: Int) : IPoint3D{
    override val withNeighbors: List<Point3D> by lazy {
        (x - 1..x + 1).flatMap { iX -> (y - 1..y + 1).flatMap { iY -> (z - 1..z + 1).map { iZ -> Point3D(iX, iY, iZ) } } }
    }
    override val neighbors: List<Point3D> get() = withNeighbors - this
}

data class Point4D(val point3d: Point3D, val w: Int) : IPoint3D by point3d{
    override val withNeighbors: List<Point4D> by lazy {
        (w - 1..w +1).flatMap { iW -> point3d.withNeighbors.map { Point4D(it, iW) }}
    }
    override val neighbors: List<Point4D> get() = withNeighbors - this
}

data class Cube(val active: Boolean) {
    override fun toString(): String = if (active) "#" else "."
}

class Universe<T : Point>(val map: Map<T, Cube>, val cycle: Int = 0) : Map<T, Cube> by map {
    fun nextCycle(): Universe<T> {
        return keys.flatMap { it.withNeighbors }
                .toSet()
                .associateWith { point ->
                    val active = this[point]?.active == true
                    val activeNeighbors = point.neighbors.count { this[it]?.active == true }
                    Cube((active && (activeNeighbors == 2 || activeNeighbors == 3)) || !active && activeNeighbors == 3)
                }
                .let { Universe(it as Map<T, Cube>, cycle = cycle + 1) }
    }
}

fun main() {
    val input3D = File(ClassLoader.getSystemResource("day17.txt").file).readLines()
            .flatMapIndexed { y, row -> row.mapIndexed { x, state -> Point3D(x, y, 0) to Cube(state == '#') } }
            .toMap()

    val life = generateSequence(Universe(input3D)) { it.nextCycle() }
    life.take(7).forEach { universe -> println(universe.values.count { it.active }) }


    val input4D = input3D.mapKeys { Point4D(it.key, 0) }
    val life4D = generateSequence(Universe(input4D)) { it.nextCycle() }
    life4D.take(7).forEach { universe -> println(universe.values.count { it.active }) }
}
