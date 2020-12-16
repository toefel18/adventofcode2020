package nl.toefel.aoc2020.day16

import java.io.File
import java.lang.ClassLoader.getSystemResource

data class Rule(val name: String, val ranges: List<IntRange>) {
    fun isValid(num: Int): Boolean = ranges.any { num in it }
}
typealias RuleSet = List<Rule>
fun RuleSet.validateNr(nr: Int): Boolean = this.any { it.isValid(nr) }
typealias Ticket = List<Int>
fun Ticket.invalidFields(withRules: RuleSet): List<Int> = this.filter { nr -> !withRules.validateNr(nr) }
fun String.toIntList(): List<Int> = this.split(",").map { it.toInt() }
fun String.ranges(): List<IntRange> = "(\\d+)-(\\d+)".toRegex().findAll(this).map { it.groupValues[1].toInt()..it.groupValues[2].toInt() }.toList()

fun main() {
    val input = File(getSystemResource("day16.txt").file).readLines()
    val rules :RuleSet = input.takeWhile { it.isNotEmpty() }.map { line -> Rule(line.takeWhile { it != ':' }, line.ranges()) }
    val ticket: Ticket = input.first { it.firstOrNull() in '0'..'9' }.toIntList()
    val nearbyTickets: List<Ticket> = input.reversed().takeWhile { it.firstOrNull() in '0'..'9' }.reversed().map { it.toIntList() }

    part1(nearbyTickets, rules)
    part2(ticket, nearbyTickets, rules)
}

fun part1(nearbyTickets: List<Ticket>, rules: RuleSet) {
    nearbyTickets.flatMap { it.invalidFields(rules) }.sum().also { println("$it") }
}

fun part2(ticket: Ticket, nearbyTickets: List<Ticket>, rules: RuleSet) {
    val validTickets = nearbyTickets.filter { ticket -> ticket.invalidFields(rules).isEmpty() }
    val fieldToRules: MutableMap<Int, MutableSet<Rule>> = findValidRulesPerField(validTickets, rules)
    val fieldToRule: Map<Int, Rule> = resolveSingleRulePerField(fieldToRules)

    fieldToRule.filterValues { it.name.startsWith("departure") }
            .keys
            .map { ticket[it].toLong() }
            .reduce {acc, i -> acc * i}
            .also { println(it) }
}

private fun resolveSingleRulePerField(fieldToRules: MutableMap<Int, MutableSet<Rule>>): Map<Int, Rule> {
    do {
        val (finishedKeys, finishedValues) = fieldToRules.filterValues { it.size == 1 }.let { it -> it.keys to it.values.flatten() }
        (fieldToRules.keys - finishedKeys).forEach { key -> fieldToRules[key]?.let { it.removeAll(finishedValues) } }
    } while (fieldToRules.values.any { it.size > 1 })
    return fieldToRules.map { entry -> entry.key to entry.value.first() }.toMap()
}

private fun findValidRulesPerField(validTickets: List<Ticket>, rules: RuleSet): MutableMap<Int, MutableSet<Rule>> {
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
