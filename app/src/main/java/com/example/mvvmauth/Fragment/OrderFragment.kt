package com.example.mvvmauth.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmauth.Adapter.OrdersAdapter
import com.example.mvvmauth.AllDataModel.OrderDataModel
import com.example.mvvmauth.AllViewModel.MyOrderViewModel
import com.example.mvvmauth.R


class OrderFragment : Fragment() {
   private lateinit var myOrderRecyclerView: RecyclerView
   private lateinit var orderAdapter: OrdersAdapter
   private lateinit var orderList:ArrayList<OrderDataModel>
   private lateinit var orderViewModel: MyOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        myOrderRecyclerView = view.findViewById(R.id.myOrderRecyclerView)
        orderList = ArrayList()
        orderAdapter = OrdersAdapter(orderList)
        myOrderRecyclerView.adapter = orderAdapter
        orderViewModel = ViewModelProvider(this)[MyOrderViewModel::class.java]
        orderViewModel.fetchOrderData()
        orderViewModel.orderList.observe(viewLifecycleOwner) { orderList ->
            orderList.clear()
            orderList.addAll(orderList)
            orderAdapter.notifyDataSetChanged()

        }

        return view
    }

}