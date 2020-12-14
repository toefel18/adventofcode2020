package nl.toefel.aoc2020.day14

import java.io.File
import java.lang.ClassLoader.getSystemResource

fun main() {
    val input = File(getSystemResource("day14.txt").file).readLines()
    lateinit var mask : String
    val mem = mutableMapOf<Long, Long>()

    input.forEach { line ->
        if (line.startsWith("mask")) {
            mask = line.substring(7)
        } else {
            val addr = line.drop(4).takeWhile { it in '0'..'9' }.toLong()
            val value = line.dropWhile { it != '=' }.drop(2).toLong()
            mem[addr] = value.applyValueMask(mask)
        }
    }
    println(mem.values.sum());

    mem.clear()
    input.forEach { line ->
        if (line.startsWith("mask")) {
            mask = line.substring(7)
        } else {
            val addr = line.drop(4).takeWhile { it in '0'..'9' }.toLong()
            val value = line.dropWhile { it != '=' }.drop(2).toLong()
            addr.applyFloatingAddressMask(mask).forEach {
                mem[it] = value
            }
        }
    }
    println(mem.values.sum());
}

fun Long.applyValueMask(mask: String): Long = this.toString(2)
        .padStart(mask.length, '0')
        .zip(mask)
        .map { (bit, mask) -> if (mask == 'X') { bit } else mask }
        .let { String(it.toCharArray()).toLong(2) }


fun Long.applyFloatingAddressMask(mask: String): List<Long> {
    val pattern = this.toString(2)
            .padStart(mask.length, '0')
            .zip(mask)
            .map { (bit, mask) -> if (mask == '0') { bit } else { mask } }
            .let { String(it.toCharArray()) }

    var results = listOf(pattern)
    while (results.first().contains('X')) {
        results = results.flatMap { listOf(it.replaceFirst('X', '1'), it.replaceFirst('X', '0')) }
    }
    return results.map { it.toLong(2) }
}

