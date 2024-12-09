fun main() {
    val inputText = "\" Лето , как обычно ,пролетело    незаметно...\"–грустно сказал Ваня .Он( и его друзья )сидели на берегу речки с поэтичным названием \"Стремительная \"."
    println(inputText)
    println("-".repeat(inputText.length))
    println(processText(inputText))
}

fun processText(input: String): String {
    var text = replaceQuotes(input)
    text = removeExtraSpaces(text)
    text = replaceHyphensWithDashes(text)
    text = addMissingSpaces(text)
    return text
}

fun replaceQuotes(input: String): String {
    var insideQuotes = false
    return input.map { char ->
        when (char) {
            '"' -> {
                insideQuotes = !insideQuotes
                if (insideQuotes) '«' else '»'
            }
            else -> char
        }
    }.joinToString("")
}

fun removeExtraSpaces(input: String): String {
    val regex1 = "\\s+([,.)»])".toRegex()	// 1 или более пробелов перед , или . или ) или »
    val regex2 = "([(«])\\s+".toRegex()		// 1 или более пробелов после ( или «
    return input
    	.replace(regex1, "$1")
    	.replace(regex2, "$1")
    	.replace("\\s+".toRegex(), " ")		// убираем множественные пробелы
}

fun addMissingSpaces(input: String): String {
    val regex1 = "(?<=\\p{L})([,.)])(?![\\s\\.])".toRegex()
    val regex2 = "(?<=\\S)[(]".toRegex() 
    var outputText: String
    outputText = input.replace(regex1) {
        "${it.value} " // Добавляем пробел после них
    }
    outputText = outputText.replace(regex2) {
        " ${it.value}" // Добавляем пробел перед "("
    }
    return outputText
}

fun replaceHyphensWithDashes(input: String): String {
    return input.replace("(\\s*[-–]\\s*)".toRegex(), " – ")
      .replace("\\s+".toRegex(), " ") // Избавляемся от лишних пробелов
      .trim() // Убираем пробелы по краям строки
}