package nl.toefel.aoc2020.day2

import java.io.File

data class PasswordWithPolicy(val min: Int, val max: Int, val char: Char, val pwd: String) {
    val validPwdPart1 : Boolean = pwd.filter { it == char }.length in (min .. max)
    val validPwdPart2 : Boolean = (pwd[min-1] == char) xor (pwd[max -1] == char)
}

fun main() {
    val input = "(\\d+)-(\\d+) ([a-z]): (.+)"
            .toRegex()
            .findAll(File(ClassLoader.getSystemResource("day2.txt").file).readText())
            .map { with(it.groupValues) {PasswordWithPolicy(this[1].toInt(), this[2].toInt(), this[3][0], this[4]) }}
            .toList()

    println(input.filter { it.validPwdPart1 }.size)
    println(input.filter { it.validPwdPart2 }.size)
}

