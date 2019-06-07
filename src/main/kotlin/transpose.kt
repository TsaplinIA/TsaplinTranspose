import java.io.File
import java.lang.Exception
import java.lang.Math.max
import java.util.*

class Transpose(var file: String?, var ofile: String?, var a: Int?, var t: Boolean, var r: Boolean) {
    var text = ""
    private var lines: List<List<String>>
    var tLines = mutableListOf(mutableListOf<String>())

    init {
        if (file == null) {
            var temp = false
            val scan = Scanner(System.`in`)
            while (!temp) {
                val newLine = scan.nextLine()
                temp = newLine == ""
                if (!temp) text += newLine + "\n"
            }
        } else {
            text = File(file).readText()
        }

        lines = text
            .split("\n")
            .map { it
                .trim()
                .replace(Regex(""" +"""), " ")
                .split(" ")
                .toMutableList()
                .filter { it.isNotEmpty() }
            }
            .toMutableList()
            .filter { it.isNotEmpty() }

        if (a != null) {
            for (line in lines) {
                val tempLine = mutableListOf<String>()
                for (word in line) {
                    var tempWord = word
                    if (word.length > a!! && t)
                        tempWord = tempWord.substring(0, a!!)
                    val sCount = max(a!! - tempWord.length, 0)
                    val parR = if (r) 1 else 0
                    tempWord = " ".repeat(sCount * parR) + tempWord + " ".repeat(sCount * (1 - parR))
                    tempLine.add(tempWord)
                }
                tLines.add(tempLine)
            }
            lines = tLines.filter { it.isNotEmpty() }
        }

        val maxIndex = lines.maxBy { it.size }!!.size - 1
        val sLines = mutableListOf(mutableListOf<String>())
        for (i in 0..maxIndex) {
            val tempList = mutableListOf<String>()
            for (line in lines)
                if (line.lastIndex >= i) tempList.add(line[i])
            sLines.add(tempList)
        }
        lines = sLines.filter { it.isNotEmpty() }
        text = lines.joinToString("\n") { it.joinToString(" ") }
        if (ofile != null) File(ofile).writeText(text) else println(text)
    }

}

fun readCom(line: String): Map<String, String?> {
    val result: MutableMap<String, String?> = listOf("-r", "-a", "-t", "-o", "file")
        .map { it -> it to null }
        .toMap()
        .toMutableMap()
    var firstLine = line.replace(Regex(""" +"""), " ")
    val tRange = Regex("transpose").find(firstLine)?.range
    if (tRange != 0..8) throw Exception("Incorrect command")
    else firstLine = firstLine.removeRange(tRange)
    val parList = Regex("""\[.+?\]""")
        .findAll(firstLine.trim())
        .map { it.value.replace("[", "").replace("]", "") }
        .map { it.split(" ").toMutableList() }
        .toMutableList()
    for (el in parList) el.add("true")
    val tempParList = parList.toMutableList()
    for ((first, second) in result)
        for (el in parList)
            if (first == el[0] && first != "file") {
                result[first] = el[1]
                tempParList.remove(el)
            }
    if (tempParList.size > 0) result["file"] = tempParList[0][0]
    if (result["-a"] != null && !Regex("""[1-9]\d*""").matches(result["-a"]!!)) throw Exception("Incorrect parameter")
    if ((result["-r"] != null || result["-t"] != null) && result["-a"] == null) result["-a"] = "10"
    return result.toMap()
}

fun main(args: Array<String>) {
    val a = readCom(Scanner(System.`in`).nextLine())
    Transpose(a["file"], a["-o"], a["-a"]?.toInt(), a["-t"] != null, a["-r"] != null)
}