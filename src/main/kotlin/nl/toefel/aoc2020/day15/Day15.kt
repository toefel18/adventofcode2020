package nl.toefel.aoc2020.day15

fun main() {
    playGame(mutableListOf(0,13,1,16,6,17), 2020)
    playGame(mutableListOf(0,13,1,16,6,17), 30000000)
}

private fun playGame(nrs: MutableList<Int>, until: Int, expect: Long? = null)  {
    val indicesLastOccurrence = HashMap<Int, Int>()
    nrs.dropLast(1).forEachIndexed{ i, nr -> indicesLastOccurrence[nr] = i }
    var currentNr = nrs.last()
    var currentPos = nrs.size - 1
    while (currentPos < until -1) {
        currentNr = indicesLastOccurrence.put(currentNr, currentPos)?.let { currentPos - it } ?: 0
        currentPos++
    }
    println("result = $currentPos = $currentNr, expected was $expect")
}