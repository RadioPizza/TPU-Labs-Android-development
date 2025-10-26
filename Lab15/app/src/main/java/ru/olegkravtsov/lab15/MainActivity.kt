package ru.olegkravtsov.lab15

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), PurchaseDialogFragment.PurchaseDialogListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var adapter: PurchaseAdapter

    private val purchases = mutableListOf<Purchase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecyclerView()
        setupSwipeToDelete()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        fabAdd = findViewById(R.id.fabAdd)

        fabAdd.setOnClickListener {
            showAddPurchaseDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = PurchaseAdapter(
            purchases = purchases,
            onItemClick = { purchase ->
                showEditPurchaseDialog(purchase)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                adapter.removePurchase(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showAddPurchaseDialog() {
        val dialog = PurchaseDialogFragment.newInstance()
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "add_purchase")
    }

    private fun showEditPurchaseDialog(purchase: Purchase) {
        val dialog = PurchaseDialogFragment.newInstance(purchase)
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "edit_purchase")
    }

    override fun onPurchaseAdded(purchase: Purchase) {
        adapter.addPurchase(purchase)
    }

    override fun onPurchaseUpdated(purchase: Purchase) {
        val position = adapter.getPurchasePosition(purchase)
        if (position != -1) {
            adapter.updatePurchase(position, purchase)
        }
    }
}