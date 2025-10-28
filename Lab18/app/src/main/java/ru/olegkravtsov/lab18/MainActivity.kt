package ru.olegkravtsov.lab18

import android.os.Bundle
import android.util.Xml
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private lateinit var currencyAdapter: CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupButton()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.currencyRecyclerView)
        currencyAdapter = CurrencyAdapter(emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = currencyAdapter
        }
    }

    private fun setupButton() {
        val loadButton = findViewById<android.widget.Button>(R.id.loadButton)
        val progressBar = findViewById<android.widget.ProgressBar>(R.id.progressBar)

        loadButton.setOnClickListener {
            progressBar.visibility = android.view.View.VISIBLE
            loadButton.isEnabled = false

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val currencies = loadCurrencies()
                    withContext(Dispatchers.Main) {
                        currencyAdapter.updateData(currencies)
                        progressBar.visibility = android.view.View.GONE
                        loadButton.isEnabled = true

                        // Используем ресурсы для строки
                        val successMessage = getString(R.string.loading_success, currencies.size)
                        android.widget.Toast.makeText(
                            this@MainActivity,
                            successMessage,
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = android.view.View.GONE
                        loadButton.isEnabled = true

                        // Используем ресурсы для строки ошибки
                        val errorMessage = getString(R.string.loading_error, e.message ?: "Неизвестная ошибка")
                        android.widget.Toast.makeText(
                            this@MainActivity,
                            errorMessage,
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun loadCurrencies(): List<Currency> {
        val currencies = mutableListOf<Currency>()

        // Используем ресурсы для URL и кодировки
        val url = getString(R.string.url_currency_rates)
        val encoding = getString(R.string.encoding_windows1251)

        val xml = URL(url)
            .readText(Charset.forName(encoding))

        val parser = Xml.newPullParser()
        parser.setInput(xml.reader())

        var eventType = parser.eventType
        var currentName = ""
        var currentValue = ""
        var currentNominal = 1
        var currentCharCode = ""

        var inValute = false
        var inName = false
        var inValue = false
        var inNominal = false
        var inCharCode = false

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "Valute" -> {
                            inValute = true
                            currentName = ""
                            currentValue = ""
                            currentNominal = 1
                            currentCharCode = ""
                        }
                        "Name" -> if (inValute) inName = true
                        "Value" -> if (inValute) inValue = true
                        "Nominal" -> if (inValute) inNominal = true
                        "CharCode" -> if (inValute) inCharCode = true
                    }
                }

                XmlPullParser.TEXT -> {
                    when {
                        inName -> currentName = parser.text
                        inValue -> currentValue = parser.text
                        inNominal -> currentNominal = parser.text.toIntOrNull() ?: 1
                        inCharCode -> currentCharCode = parser.text
                    }
                }

                XmlPullParser.END_TAG -> {
                    when (parser.name) {
                        "Valute" -> {
                            inValute = false
                            if (currentName.isNotEmpty() && currentValue.isNotEmpty() && currentCharCode.isNotEmpty()) {
                                currencies.add(
                                    Currency(
                                        name = currentName,
                                        value = currentValue,
                                        nominal = currentNominal,
                                        charCode = currentCharCode
                                    )
                                )
                            }
                        }
                        "Name" -> inName = false
                        "Value" -> inValue = false
                        "Nominal" -> inNominal = false
                        "CharCode" -> inCharCode = false
                    }
                }
            }
            eventType = parser.next()
        }

        // Сортируем валюты по алфавиту для удобства
        return currencies.sortedBy { it.name }
    }
}