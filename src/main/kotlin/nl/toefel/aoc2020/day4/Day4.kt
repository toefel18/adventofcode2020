package nl.toefel.aoc2020.day4

import java.io.File



fun main() {
    val requiredKeys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    val passportFieldRegex = "(byr|iyr|eyr|hgt|hcl|ecl|pid|cid):([#a-z0-9]+)".toRegex()

    val passports = File(ClassLoader.getSystemResource("day4.txt").file)
        .readText()
        .split("\n\n")
        .map { passportBlock -> passportFieldRegex.findAll(passportBlock).map { it.groupValues[1] to it.groupValues[2] }.toMap() }
        .toList()

    println(passports.filter { passport -> requiredKeys.all { passport.containsKey(it) } }.size)

    val hcl = "#[0-9a-f]{6}".toRegex()
    val pid = "[0-9]{9}".toRegex()

    val validatedPasswords = passports
        .filter { passport -> passport["byr"]?.toIntOrNull()?.let { it in 1920..2002 } ?: false }
        .filter { passport -> passport["iyr"]?.toIntOrNull()?.let { it in 2010..2020 } ?: false }
        .filter { passport -> passport["eyr"]?.toIntOrNull()?.let { it in 2020..2030 } ?: false }
        .filter { passport -> passport["hgt"]?.let { validateHeight(it) } ?: false }
        .filter { passport -> passport["hcl"]?.let { hcl.matches(it) } ?: false }
        .filter { passport -> passport["ecl"]?.let { it in listOf("amb","blu", "brn", "gry", "grn", "hzl", "oth") } ?: false }
        .filter { passport -> passport["pid"]?.let { pid.matches(it) } ?: false }

    println(validatedPasswords.size)
}

val hgt = "([0-9]+)(cm|in)".toRegex()
fun validateHeight(heightEntry: String): Boolean {
    return hgt.matchEntire(heightEntry)?.let {
        val (amount, format) = it.destructured
        return when (format) {
            "in" -> (amount.toLong() in 59..76)
            "cm" -> (amount.toLong() in 150..193)
            else -> false
        }
    } ?: false
}

