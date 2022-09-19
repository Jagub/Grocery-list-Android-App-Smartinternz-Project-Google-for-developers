package com.example.grocerylistapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylistapp.adapter.GroceryAdapter
import com.example.grocerylistapp.database.GroceryDatabase
import com.example.grocerylistapp.database.GroceryRepository
import com.example.grocerylistapp.database.entity.GroceryItems
import com.example.grocerylistapp.ui.GroceryViewModel
import com.example.grocerylistapp.ui.GroceryViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), GroceryAdapter.GroceryItemClickInterface {

    lateinit var rvItem: RecyclerView
    lateinit var fabAdd: FloatingActionButton
    lateinit var list: List<GroceryItems>
    lateinit var groceryAdapter: GroceryAdapter
    lateinit var groceryViewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvItem = findViewById(R.id.rv_items)
        fabAdd = findViewById(R.id.fab_add)
        list = ArrayList<GroceryItems>()
        groceryAdapter = GroceryAdapter(list, this)
        rvItem.layoutManager = LinearLayoutManager(this)
        rvItem.adapter = groceryAdapter
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        groceryViewModel = ViewModelProvider(this, factory).get(GroceryViewModel::class.java)
        groceryViewModel.allGroceryItems().observe(this, Observer {
            groceryAdapter.list = it
            groceryAdapter.notifyDataSetChanged()
        })

        fabAdd.setOnClickListener {
            openDialoge()
        }
    }

    private fun openDialoge() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_dialog)
        val cancelBtn = dialog.findViewById<Button>(R.id.btn_cancel)
        val addBtn = dialog.findViewById<Button>(R.id.btn_add )
        val itemEdt = dialog.findViewById<EditText>(R.id.editItemName )
        val itemPriceEdt = dialog.findViewById<EditText>(R.id.editItemPrice )
        val itemQuantityEdt = dialog.findViewById<EditText>(R.id.editItemQuantity )
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        addBtn.setOnClickListener {
            val itemName: String = itemEdt.text.toString()
            val itemPrice: String = itemPriceEdt.text.toString()
            val itemQuantity: String = itemQuantityEdt.text.toString()
            val qty: Int = itemQuantity.toInt()
            val pr: Int = itemPrice.toInt()
            if (itemName.isNotEmpty() && itemPrice.isNotEmpty() && itemQuantity.isNotEmpty()) {
                val items = GroceryItems(itemName, qty, pr)
                groceryViewModel.insert(items)
                Toast.makeText(applicationContext, " Item Inserted .. ", Toast.LENGTH_SHORT).show()
                groceryAdapter.notifyDataSetChanged()
                dialog.dismiss()
            } else {
                Toast.makeText(
                    applicationContext,
                    " Please enter all the data .. ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        dialog.show()
    }

    override fun onItemClick(groceryItems: GroceryItems) {
        groceryViewModel.delete(groceryItems)
        groceryAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext, "Item Deleted..", Toast.LENGTH_SHORT).show()
    }
}