package nl.toefel.aoc2020.day9

import java.io.File
import java.lang.ClassLoader.getSystemResource

val input = """16
10
15
5
1
11
7
19
6
12
4"""
fun main() {
    val jolts = File(getSystemResource("day10.txt").file).readLines()
            .map { it.toLong() }
            .sorted()
            .let { listOf(0L) + it + (it.last() + 3) }
//    val jolts = input2.reader().readLines().map { it.toLong() }.sorted().let { listOf(0L) + it + (it.last() + 3) }
    println(jolts)

    val (ones, threes) = jolts.windowed(2)
            .map { (a, b) -> b - a }
            .filter { it == 1L || it == 3L }
            .partition { it == 1L }
    println(ones.size * threes.size)

}

val input2 = """28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3
"""