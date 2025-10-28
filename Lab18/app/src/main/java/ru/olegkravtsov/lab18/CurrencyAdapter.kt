package ru.olegkravtsov.lab18

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrencyAdapter(private var currencies: List<Currency>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.currencyName)
        val codeTextView: TextView = itemView.findViewById(R.id.currencyCode)
        val valueTextView: TextView = itemView.findViewById(R.id.currencyValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.nameTextView.text = currency.getDisplayName()
        holder.codeTextView.text = currency.charCode
        holder.valueTextView.text = currency.value
    }

    override fun getItemCount(): Int = currencies.size

    fun updateData(newCurrencies: List<Currency>) {
        currencies = newCurrencies
        notifyDataSetChanged()
    }
}