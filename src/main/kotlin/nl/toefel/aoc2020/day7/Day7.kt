package nl.toefel.aoc2020.day7

import java.io.File

data class Content(val amount: Int, val bag: String)

fun main() {
    val regex = "(\\w+ \\w+) bags contain (no other bags|((\\d+) (\\w+ \\w+) bags?)(, (\\d+) (\\w+ \\w+) bags?)?(, (\\d+) (\\w+ \\w+) bags?)?(, (\\d+) (\\w+ \\w+) bags?)?).".toRegex()
    val bagRules: Map<String, List<Content>> = File(ClassLoader.getSystemResource("day7.txt").file).readLines()
            .map { regex.matchEntire(it)!!.groupValues }
            .associateBy({ g -> g[1] }, { g -> (4..13 step 3).filterNot { g[it].isEmpty() }.map { Content(g[it].toInt(), g[it + 1]) } })

    fun Map<String, List<Content>>.countBagsThatCanContain(bag: String): Set<String> =
            this.filterValues { bagContents -> bagContents.any { it.bag == bag } }.keys
                    .flatMap { countBagsThatCanContain(it) + it }
                    .toSet()

    fun Map<String, List<Content>>.countContainedBags(bag: String): Long =
            this[bag]?.map { it.amount + it.amount * countContainedBags(it.bag) }?.sum() ?: 0

    println(bagRules.countBagsThatCanContain("shiny gold").size)
    println(bagRules.countContainedBags("shiny gold"))
}

// oldscool and better performance for part 1
//private fun part1(bagRules: Map<String, List<Content>>, target : String): Set<String> {
//    val visited = mutableSetOf<String>()
//    var next = setOf(target)
//    while (next.isNotEmpty()) {
//        next = next.flatMap { target -> bagRules.filterValues { c -> c.any { it.bag == target } }.keys }
//                .filterNot { it in visited }
//                .toSet()
//        visited += next
//    }
//    return visited.minus(target)
//}

