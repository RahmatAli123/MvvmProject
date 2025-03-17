package com.example.mvvmauth
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmauth.AllViewModel.ProductDetailsViewModel
import com.example.mvvmauth.Fragment.CardFragment

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var productDetailsImageView: ImageView
    private lateinit var productDetailsName: TextView
    private lateinit var productDetailsPrice: TextView
    private lateinit var productDetailsDescription: TextView
    private lateinit var addToCartButton: Button
    private lateinit var buyNowButton: Button
    private lateinit var viewModel: ProductDetailsViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        productDetailsImageView = findViewById(R.id.productDetails_ImageView)
        productDetailsName=findViewById(R.id.productDetailsName_TextView)
        productDetailsPrice=findViewById(R.id.productDetailsPrice_TextView)
        productDetailsDescription=findViewById(R.id.productDetailsDescription_TextView)
        addToCartButton = findViewById(R.id.addToCart_Button)
        buyNowButton = findViewById(R.id.buyNow_Button)
        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]


        val productName=intent.getStringExtra("productName")
        val productPrice=intent.getStringExtra("productPrice")
        val productDescription=intent.getStringExtra("productDescription")
        val productImage=intent.getStringExtra("productImage")?.replace("[","")

        productDetailsName.text=productName
        productDetailsPrice.text=productPrice
        productDetailsDescription.text=productDescription
        Glide.with(this).load(productImage).into(productDetailsImageView)

        addToCartButton.setOnClickListener {
            viewModel.addToCartInsertData(this,productName!!,productPrice!!,productDescription!!,productImage!!)


        }

        buyNowButton.setOnClickListener {
            val intent= Intent(this,PaymentActivity::class.java)
            intent.putExtra("productTittle",productName)
            intent.putExtra("productDes",productDescription)
            intent.putExtra("productPrice",productPrice)
            intent.putExtra("productImage",productImage)
            startActivity(intent)
        }
    }

}