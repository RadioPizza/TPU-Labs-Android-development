package ru.olegkravtsov.lab15

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class PurchaseDialogFragment : DialogFragment() {

    interface PurchaseDialogListener {
        fun onPurchaseAdded(purchase: Purchase)
        fun onPurchaseUpdated(purchase: Purchase)
    }

    companion object {
        private const val ARG_PURCHASE = "purchase"

        fun newInstance(purchase: Purchase? = null): PurchaseDialogFragment {
            val args = Bundle().apply {
                putSerializable(ARG_PURCHASE, purchase)
            }
            return PurchaseDialogFragment().apply {
                arguments = args
            }
        }
    }

    private var listener: PurchaseDialogListener? = null
    private var existingPurchase: Purchase? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        existingPurchase = arguments?.getSerializable(ARG_PURCHASE, Purchase::class.java)

        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_purchase, null)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etQuantity = view.findViewById<EditText>(R.id.etQuantity)

        // Если передан существующий товар - заполняем поля
        existingPurchase?.let {
            etName.setText(it.name)
            etQuantity.setText(it.quantity)
        }

        val title = if (existingPurchase == null) {
            R.string.dialog_title_add
        } else {
            R.string.dialog_title_edit
        }

        builder.setView(view)
            .setTitle(title)
            .setPositiveButton(R.string.dialog_button_ok) { dialog, which ->
                val name = etName.text.toString().trim()
                val quantity = etQuantity.text.toString().trim()

                if (name.isNotEmpty() && quantity.isNotEmpty()) {
                    if (existingPurchase == null) {
                        // Добавление нового товара
                        listener?.onPurchaseAdded(Purchase(name = name, quantity = quantity))
                    } else {
                        // Обновление существующего товара
                        val updatedPurchase = existingPurchase!!.copy(
                            name = name,
                            quantity = quantity
                        )
                        listener?.onPurchaseUpdated(updatedPurchase)
                    }
                }
            }
            .setNegativeButton(R.string.dialog_button_cancel) { dialog, which ->
                dialog.dismiss()
            }

        return builder.create()
    }

    fun setListener(listener: PurchaseDialogListener) {
        this.listener = listener
    }
}