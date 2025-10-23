package ru.olegkravtsov.lab12

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.olegkravtsov.lab12.databinding.ItemProductBinding

class ProductAdapter(
    private val products: List<Product>,
    private val rootView: View
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val cartItems = mutableSetOf<Long>()

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product, isInCart: Boolean, onCartClick: (Product) -> Unit) {
            binding.productImage.setImageResource(product.imageResId)
            binding.productName.text = product.name
            binding.productPrice.text = product.price

            val iconRes = if (isInCart) {
                R.drawable.shopping_cart
            } else {
                R.drawable.shopping_cart_add
            }
            binding.cartButton.setImageResource(iconRes)

            binding.cartButton.setOnClickListener {
                onCartClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val isInCart = cartItems.contains(product.id)
        holder.bind(product, isInCart) { clickedProduct ->
            handleCartClick(clickedProduct, holder)
        }
    }

    override fun getItemCount(): Int = products.size

    private fun handleCartClick(product: Product, holder: ProductViewHolder) {
        val wasInCart = cartItems.contains(product.id)

        if (wasInCart) {
            cartItems.remove(product.id)
            showSnackbar(
                rootView.context.getString(R.string.removed, product.name),
                ContextCompat.getColor(rootView.context, R.color.snackbar_error)
            )
        } else {
            cartItems.add(product.id)
            showSnackbar(
                rootView.context.getString(R.string.added, product.name),
                ContextCompat.getColor(rootView.context, R.color.snackbar_success)
            )
        }

        notifyItemChanged(holder.bindingAdapterPosition)
    }

    private fun showSnackbar(message: String, backgroundColor: Int) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)

        // Используем методы из лабораторной работы №10 для настройки цвета
        snackbar.setBackgroundTint(backgroundColor)

        // Устанавливаем цвет текста для лучшей читаемости
        val textColor = if (backgroundColor == ContextCompat.getColor(rootView.context, R.color.snackbar_success)) {
            ContextCompat.getColor(rootView.context, R.color.snackbar_success_text)
        } else {
            ContextCompat.getColor(rootView.context, R.color.snackbar_error_text)
        }
        snackbar.setTextColor(textColor)

        snackbar.show()
    }
}