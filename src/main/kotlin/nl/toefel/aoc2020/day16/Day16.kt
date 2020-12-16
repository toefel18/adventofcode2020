package nl.toefel.aoc2020.day16

import java.io.File
import java.lang.ClassLoader.getSystemResource
import kotlin.collections.Map.Entry
import kotlin.system.measureTimeMillis

data class Rule(val name: String, val ranges: List<IntRange>) {
    fun isValid(num: Int): Boolean = ranges.any { num in it }
}

fun List<Rule>.validateNr(nr: Int): Boolean = this.any { it.isValid(nr) }
fun List<Int>.invalidFields(withRules: List<Rule>): List<Int> = this.filter { nr -> !withRules.validateNr(nr) }
fun List<Int>.isValid(withRules: List<Rule>): Boolean = this.invalidFields(withRules).isEmpty()
fun String.toIntList(): List<Int> = this.split(",").map { it.toInt() }
fun String.ranges(): List<IntRange> = "(\\d+)-(\\d+)".toRegex().findAll(this).map { it.groupValues[1].toInt()..it.groupValues[2].toInt() }.toList()

fun main() {
    val input = File(getSystemResource("day16.txt").file).readLines()
    val rules: List<Rule> = input.takeWhile { it.isNotEmpty() }.map { line -> Rule(line.takeWhile { it != ':' }, line.ranges()) }
    val ticket: List<Int> = input.first { it.firstOrNull() in '0'..'9' }.toIntList()
    val nearbyTickets: List<List<Int>> = input.reversed().takeWhile { it.firstOrNull() in '0'..'9' }.reversed().map { it.toIntList() }

    part1(nearbyTickets, rules)
    measureTimeMillis { part2Mutable(ticket, nearbyTickets, rules) }.also { println("mutable part2 took $it millis") }
    measureTimeMillis { part2Immutable(ticket, nearbyTickets, rules)}.also { println("immutable part2 took $it millis") }
}

fun part1(nearbyTickets: List<List<Int>>, rules: List<Rule>) {
    nearbyTickets.flatMap { it.invalidFields(rules) }.sum().also { println("$it") }
}

fun part2Immutable(ticket: List<Int>, nearbyTickets: List<List<Int>>, rules: List<Rule>) {
    solveToSingleRulePerField(validRulesPerField(nearbyTickets.filter { it.isValid(rules) }, rules))
            .filterValues { it.name.startsWith("departure") }
            .keys
            .map { ticket[it].toLong() }
            .reduce { acc, i -> acc * i }
            .also { println(it) }
}

private fun validRulesPerField(validTickets: List<List<Int>>, rules: List<Rule>): Map<Int, Set<Rule>> = validTickets
            .map { nrs -> nrs.withIndex().associateBy({ it.index }, { rules.filter { rule -> rule.isValid(it.value) }.toSet() }) }
            .reduce {acc, map -> acc.mapValues { entry -> entry.value.intersect(map[entry.key]!!) } }

private fun solveToSingleRulePerField(fieldToRules: Map<Int, Set<Rule>>): Map<Int, Rule> = fieldToRules
            .entries
            .sortedBy { it.value.size }
            .fold(mapOf()) { acc: Map<Int, Rule>, entry: Entry<Int, Set<Rule>> -> acc + (entry.key to (entry.value - acc.values).first()) }

// code with mutable collections, which should be more performant
fun part2Mutable(ticket: List<Int>, nearbyTickets: List<List<Int>>, rules: List<Rule>) {
    val validTickets = nearbyTickets.filter { ticket -> ticket.invalidFields(rules).isEmpty() }
    val fieldToRules: MutableMap<Int, MutableSet<Rule>> = findValidRulesPerField(validTickets, rules)
    val fieldToRule: Map<Int, Rule> = resolveSingleRulePerField(fieldToRules)

    fieldToRule.filterValues { it.name.startsWith("departure") }
            .keys
            .map { ticket[it].toLong() }
            .reduce { acc, i -> acc * i }
            .also { println(it) }
}

private fun resolveSingleRulePerField(fieldToRules: MutableMap<Int, MutableSet<Rule>>): Map<Int, Rule> {
    do {
        val (finishedKeys, finishedValues) = fieldToRules.filterValues { it.size == 1 }.let { it -> it.keys to it.values.flatten() }
        (fieldToRules.keys - finishedKeys).forEach { key -> fieldToRules[key]?.let { it.removeAll(finishedValues) } }
    } while (fieldToRules.values.any { it.size > 1 })
    return fieldToRules.map { entry -> entry.key to entry.value.first() }.toMap()
}

private fun findValidRulesPerField(validTickets: List<List<Int>>, rules: List<Rule>): MutableMap<Int, MutableSet<Rule>> {
    val fieldToRules = mutableMapOf<Int, MutableSet<Rule>>()
    validTickets.forEach { ticket ->
        ticket.forEachIndexed { fieldId, fieldValue ->
            val matchingRules = rules.filter { it.isValid(fieldValue) }.toMutableSet()
            fieldToRules[fieldId] = fieldToRules[fieldId]?.let { rules: MutableSet<Rule> -> matchingRules.intersect(rules).toMutableSet() }
                    ?: matchingRules
        }
    }
    return fieldToRules
}