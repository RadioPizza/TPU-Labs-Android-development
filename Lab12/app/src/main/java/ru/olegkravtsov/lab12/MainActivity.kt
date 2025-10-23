package ru.olegkravtsov.lab12

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import ru.olegkravtsov.lab12.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val products = listOf(
            Product(1, getString(R.string.apple_juice), getString(R.string.apple_juice_price), R.drawable.apple_juice),
            Product(2, getString(R.string.broccoli), getString(R.string.broccoli_price), R.drawable.broccoli),
            Product(3, getString(R.string.carrot), getString(R.string.carrot_price), R.drawable.carrot),
            Product(4, getString(R.string.cheese), getString(R.string.cheese_price), R.drawable.cheese),
            Product(5, getString(R.string.chicken_drumstick), getString(R.string.chicken_drumstick_price), R.drawable.chicken_drumstick),
            Product(6, getString(R.string.crab), getString(R.string.crab_price), R.drawable.crab),
            Product(7, getString(R.string.donut), getString(R.string.donut_price), R.drawable.donut),
            Product(8, getString(R.string.egg), getString(R.string.egg_price), R.drawable.egg),
            Product(9, getString(R.string.fish), getString(R.string.fish_price), R.drawable.fish),
            Product(10, getString(R.string.french_fries_basket), getString(R.string.french_fries_basket_price), R.drawable.french_fries_basket),
            Product(11, getString(R.string.ice_cream), getString(R.string.ice_cream_price), R.drawable.ice_cream),
            Product(12, getString(R.string.milk), getString(R.string.milk_price), R.drawable.milk),
            Product(13, getString(R.string.mushroom), getString(R.string.mushroom_price), R.drawable.mushroom),
            Product(14, getString(R.string.noodles), getString(R.string.noodles_price), R.drawable.noodles),
            Product(15, getString(R.string.oatmeal), getString(R.string.oatmeal_price), R.drawable.oatmeal),
            Product(16, getString(R.string.pancakes), getString(R.string.pancakes_price), R.drawable.pancakes),
            Product(17, getString(R.string.peanut_butter), getString(R.string.peanut_butter_price), R.drawable.peanut_butter),
            Product(18, getString(R.string.pizza), getString(R.string.pizza_price), R.drawable.pizza),
            Product(19, getString(R.string.sliced_bread), getString(R.string.sliced_bread_price), R.drawable.sliced_bread),
            Product(20, getString(R.string.sushi_rolls), getString(R.string.sushi_rolls_price), R.drawable.sushi_rolls)
        )

        val adapter = ProductAdapter(products, binding.root)
        binding.productsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.productsRecyclerView.adapter = adapter
    }
}