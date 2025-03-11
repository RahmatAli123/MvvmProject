package com.example.mvvmauth.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmauth.AllViewModel.ProductViewModel
import com.example.mvvmauth.ProductDataModel
import com.example.mvvmauth.R
import com.example.mvvmauth.Adapter.UserAdapter

class HomeFragment : Fragment() {
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val productList = ArrayList<ProductDataModel>()
    private lateinit var viewModel: ProductViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        myRecyclerView = view.findViewById(R.id.recyclerView)
        myRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        userAdapter = UserAdapter(productList, requireContext())
        myRecyclerView.adapter = userAdapter
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModel.getProductListAll()

        viewModel.productListLiveData.observe(viewLifecycleOwner) { newList ->
            productList.clear()
            productList.addAll(newList!!)
            userAdapter.notifyDataSetChanged()
        }

        return view
    }
}
