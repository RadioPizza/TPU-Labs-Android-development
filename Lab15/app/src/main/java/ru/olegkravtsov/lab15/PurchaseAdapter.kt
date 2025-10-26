package ru.olegkravtsov.lab15

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PurchaseAdapter(
    private var purchases: MutableList<Purchase>,
    private val onItemClick: (Purchase) -> Unit
) : RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>() {

    class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase, parent, false)
        return PurchaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val purchase = purchases[position]
        holder.tvName.text = purchase.name
        holder.tvQuantity.text = holder.itemView.context.getString(R.string.item_quantity_format, purchase.quantity)

        holder.itemView.setOnClickListener {
            onItemClick(purchase)
        }
    }

    override fun getItemCount(): Int = purchases.size

    fun addPurchase(purchase: Purchase) {
        purchases.add(purchase)
        notifyItemInserted(purchases.size - 1)
    }

    fun updatePurchase(position: Int, purchase: Purchase) {
        purchases[position] = purchase
        notifyItemChanged(position)
    }

    fun removePurchase(position: Int) {
        purchases.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getPurchasePosition(purchase: Purchase): Int {
        return purchases.indexOfFirst { it.id == purchase.id }
    }
}