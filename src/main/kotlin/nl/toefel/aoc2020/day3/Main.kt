package nl.toefel.aoc2020.day3

import java.io.File


fun main() {
    val map = File(ClassLoader.getSystemResource("day3.txt").file).readLines()
    println(treesOnSlope(map, 3, 1))
    println(
            listOf(
                    treesOnSlope(map, 1, 1),
                    treesOnSlope(map, 3, 1),
                    treesOnSlope(map, 5, 1),
                    treesOnSlope(map, 7, 1),
                    treesOnSlope(map, 1, 2)
            ).reduce { x, y -> x * y }
    )
}

fun treesOnSlope(map: List<String>, dx: Int, dy: Int): Long {
    var y = 0
    var x = 0
    var trees = 0L

    do {
        trees += if (map[y].let { it[x % it.length] == '#' }) 1 else 0
        y += dy
        x += dx
    } while (y < map.size)

    return trees
}

