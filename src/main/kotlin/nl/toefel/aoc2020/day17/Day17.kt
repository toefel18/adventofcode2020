package nl.toefel.aoc2020.day17

import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty
import kotlin.system.measureTimeMillis

interface Point<T> {
    val withNeighbors: List<T>
    val neighbors: List<T>
}

data class Point3D(val x: Int, val y: Int, val z: Int) : Point<Point3D> {
    override val withNeighbors: List<Point3D> by CachingDelegate {
        (x - 1..x + 1).flatMap { iX ->
            (y - 1..y + 1).flatMap { iY ->
                (z - 1..z + 1).map { iZ -> Point3D(iX, iY, iZ) }
            }
        }
    }
    override val neighbors: List<Point3D> get() = withNeighbors - this
}

data class Point4D(val x: Int, val y: Int, val z: Int, val w: Int) : Point<Point4D> {
    override val withNeighbors: List<Point4D> by CachingDelegate {
        (w - 1..w + 1).flatMap { iW ->
            (x - 1..x + 1).flatMap { iX ->
                (y - 1..y + 1).flatMap { iY ->
                    (z - 1..z + 1).map { iZ -> Point4D(iX, iY, iZ, iW) }
                }
            }
        }
    }
    override val neighbors: List<Point4D> get() = withNeighbors - this
}

fun <T: Point<T>> Set<T>.cycle(): Set<T> = this
        .flatMap { it.withNeighbors }
        .filterTo(HashSet()) { point ->
            val active = point in this
            val activeNeighbors = point.neighbors.count { it in this }
            (active && (activeNeighbors == 2 || activeNeighbors == 3)) || !active && activeNeighbors == 3
        }

fun main() {
    measureTimeMillis {
        val input3D = File(ClassLoader.getSystemResource("day17.txt").file).readLines()
                .flatMapIndexed { y, row -> row.mapIndexedNotNull { x, state -> if (state == '#') Point3D(x, y, 0) else null } }
                .toSet()

        val life = generateSequence(input3D) { it.cycle() }
        life.take(7).forEachIndexed { index, points -> println("cycle $index ${points.size}") }

        val life4D = generateSequence(input3D.map { Point4D(it.x, it.y, it.z, 0) }.toSet()) { it.cycle() }
        life4D.take(7).forEachIndexed { index, points -> println("cycle $index ${points.size}") }
    }.also { println("$it millis") }
}

class CachingDelegate<T>(val initializer: () -> List<T>) {
    operator fun getValue(thisRef: T, property: KProperty<*>): List<T> {
        return map.computeIfAbsent(thisRef!!) { _ -> initializer() } as List<T>
    }

    companion object {
        val map: ConcurrentHashMap<Any, List<*>> = ConcurrentHashMap()
    }
}