package nl.toefel.aoc2020.day13

import java.io.File
import java.lang.ClassLoader.getSystemResource

data class Bus(val id: Long, val tDelta: Long)

fun main() {
    val input = File(getSystemResource("day13.txt").file).readLines()
    val depart = input.first().toLong()
    val busIds = input[1].split(",").mapNotNull { it.toLongOrNull() }

    val busWaitTimes = busIds.map { id -> id to (id - (depart % id)) }.sortedBy { it.second }
    println(busWaitTimes.first().let { (id, minutesWait) -> id * minutesWait })

    val busIdWithTime = input[1].split(",")
        .withIndex()
        .filter { it.value != "x" }
        .map { Bus(it.value.toLong(), it.index.toLong()) }

    val ids = busIdWithTime.map { it.id }.toLongArray()
    val modulos = busIdWithTime.map { (it.id- it.tDelta) }.toLongArray()

    println(findMinX(ids, modulos, ids.size))

}
fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

// Tried myself using gcd/lcm first, but that failed
// found hint online about Chinese Remainder Theorem. I understand the application a bit, not not the algorithm fully
// copied from: https://www.geeksforgeeks.org/chinese-remainder-theorem-set-2-implementation/

fun inv(a: Long, m: Long): Long {
    var a = a
    var m = m
    val m0 = m
    var t: Long
    var q: Long
    var x0 = 0L
    var x1 = 1L
    if (m == 1L) return 0

    // Apply extended Euclid Algorithm
    while (a > 1) {
        // q is quotient
        q = a / m
        t = m

        // m is remainder now, process
        // same as euclid's algo
        m = a % m
        a = t
        t = x0
        x0 = x1 - q * x0
        x1 = t
    }

    // Make x1 positive
    if (x1 < 0) x1 += m0
    return x1
}

// k is size of num[] and rem[].
// Returns the smallest number
// x such that:
// x % num[0] = rem[0],
// x % num[1] = rem[1],
// ..................
// x % num[k-2] = rem[k-1]
// Assumption: Numbers in num[] are pairwise
// coprime (gcd for every pair is 1)
fun findMinX(num: LongArray, rem: LongArray, k: Int): Long {
    // Compute product of all numbers
    var prod = 1L
    for (i in 0 until k) prod *= num[i]

    // Initialize result
    var result = 0L

    // Apply above formula
    for (i in 0 until k) {
        val pp = prod / num[i]
        result += rem[i] * inv(pp, num[i]) * pp
    }
    return result % prod
}