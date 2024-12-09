class Abonent(val name: String, val number: String) {
    val callLogs: MutableList<String> = mutableListOf()
}

class Station {
    val abonents: MutableList<Abonent> = mutableListOf()

    fun addAbonent(abonent: Abonent) {
        abonents.add(abonent)
    }

    fun call(from: String, to: String) {
        val fromAbonent = abonents.find { it.name == from }
        val toAbonent = abonents.find { it.name == to }

        if (fromAbonent != null && toAbonent != null) {
            fromAbonent.callLogs.add("Исходящий к ${toAbonent.name}")
            toAbonent.callLogs.add("Входящий от ${fromAbonent.name}")
        }
    }

    fun showStat() {
        for (abonent in abonents) {
            println("Журнал звонков абонента ${abonent.name}:")
            abonent.callLogs.forEach { println("    $it") }
            println()
        }
    }
}

fun main() {
    val station = Station()

    station.addAbonent(Abonent("Иван", "001"))
    station.addAbonent(Abonent("Ольга", "002"))
    station.addAbonent(Abonent("Сергей", "003"))

    station.call("Иван", "Ольга")
    station.call("Ольга", "Сергей")
    station.call("Сергей", "Иван")
    station.call("Иван", "Сергей")
    station.call("Ольга", "Иван")

    station.showStat()
}
