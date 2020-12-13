echo "Prepping for day $1"
touch "src/main/resources/day$1-test.txt"
touch "src/main/resources/day$1.txt"
mkdir -p "src/main/kotlin/nl/toefel/aoc2020/day$1"
cat >> "src/main/kotlin/nl/toefel/aoc2020/day$1/Day$1.kt" <<EOF
package nl.toefel.aoc2020.day$1

import java.io.File
import java.lang.ClassLoader.getSystemResource

fun main() {
    val input = File(getSystemResource("day$1.txt").file).readLines()
}
EOF