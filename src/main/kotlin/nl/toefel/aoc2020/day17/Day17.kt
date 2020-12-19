package nl.toefel.aoc2020.day17

import java.io.File

interface Point {
    val withNeighbors: List<Point>
    val neighbors: List<Point>
}

interface IPoint3D : Point {
    val x: Int
    val y: Int
    val z: Int
}

data class Point3D(override val x: Int, override val y: Int, override val z: Int) : IPoint3D {
    override val withNeighbors: List<Point3D> by lazy { (x - 1..x + 1).flatMap { iX -> (y - 1..y + 1).flatMap { iY -> (z - 1..z + 1).map { iZ -> Point3D(iX, iY, iZ) } } } }
    override val neighbors: List<Point3D> get() = withNeighbors - this
}

data class Point4D(val point3d: Point3D, val w: Int) : IPoint3D by point3d {
    override val withNeighbors: List<Point4D> by lazy { (w - 1..w + 1).flatMap { iW -> point3d.withNeighbors.map { Point4D(it, iW) } } }
    override val neighbors: List<Point4D> get() = withNeighbors - this
}

fun <T : Point> Set<T>.cycle(): Set<T> = this
        .flatMap { it.withNeighbors }
        .filterTo(HashSet()) { point ->
            val active = point in this
            val activeNeighbors = point.neighbors.count { it in this }
            (active && (activeNeighbors == 2 || activeNeighbors == 3)) || !active && activeNeighbors == 3
        } as Set<T>

fun main() {
    val input3D = File(ClassLoader.getSystemResource("day17.txt").file).readLines()
            .flatMapIndexed { y, row -> row.mapIndexedNotNull { x, state -> if (state == '#') Point3D(x, y, 0) else null } }
            .toSet()

    val life = generateSequence(input3D) { it.cycle() }
    life.take(7).forEachIndexed { index, points ->  println("cycle $index ${points.size}") }

    val input4D = input3D.map { Point4D(it, 0) }.toSet()
    val life4D = generateSequence(input4D) { it.cycle() }
    life4D.take(7).forEachIndexed { index, points ->  println("cycle $index ${points.size}") }
}
