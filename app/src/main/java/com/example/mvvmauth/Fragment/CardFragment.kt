package com.example.mvvmauth.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmauth.Adapter.AddToCartAdapter
import com.example.mvvmauth.AllDataModel.AddToCartDataModel
import com.example.mvvmauth.AllViewModel.FetchCartDataViewModel
import com.example.mvvmauth.DeleteCartItem
import com.example.mvvmauth.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

class CardFragment : Fragment(), DeleteCartItem {
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: AddToCartAdapter
    private lateinit var cartList: ArrayList<AddToCartDataModel>
    private lateinit var cartDataViewModel: FetchCartDataViewModel
    private val db = FirebaseDatabase.getInstance()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartList = ArrayList()
        cartAdapter = AddToCartAdapter(cartList, this)
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cartRecyclerView.adapter = cartAdapter
        cartDataViewModel = ViewModelProvider(this)[FetchCartDataViewModel::class.java]
        cartDataViewModel.fetchCartData()
        cartDataViewModel.productList.observe(viewLifecycleOwner) { cartItem ->
            cartList.clear()
            cartList.addAll(cartItem)
            cartAdapter.notifyDataSetChanged()
        }
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteItem(position: Int, data: AddToCartDataModel) {
        val user = FirebaseAuth.getInstance().currentUser
        db.getReference("products").child("cart")
            .child(user!!.uid).child(data.cartId.toString())
            .removeValue()
            .addOnSuccessListener {
                cartList.removeAt(position)
                cartAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Delete Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Delete Failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}