package com.example.mvvmauth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmauth.AllViewModel.PaymentViewModel
import com.example.mvvmauth.Fragment.OrderFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import java.util.UUID

class PaymentActivity : AppCompatActivity(),PaymentResultWithDataListener {
    private var db=FirebaseDatabase.getInstance()
    private lateinit var paymentViewModel: PaymentViewModel
    private val random=UUID.randomUUID().toString()
    private val hashMap=HashMap<String,Any>()
    private lateinit var productImageView: ImageView
    private lateinit var productTittleTV: TextView
    private lateinit var productDesTV: TextView
    private lateinit var productPriceTV: TextView
    private lateinit var productPriceTV1: TextView
    private lateinit var productPriceTV2: TextView
    private lateinit var proceedBtn: AppCompatButton
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        productImageView=findViewById(R.id.cartImageView)
        productTittleTV=findViewById(R.id.productTittleTV)
        productDesTV=findViewById(R.id.productDesTV)
        productPriceTV=findViewById(R.id.productPriceTV)
        productPriceTV1=findViewById(R.id.productPriceTV1)
        productPriceTV2=findViewById(R.id.productPriceTV2)

        proceedBtn=findViewById(R.id.proceedBtn)
        paymentViewModel= ViewModelProvider(this)[PaymentViewModel::class.java]

        val productTittle=intent.getStringExtra("productTittle")
        val productDes=intent.getStringExtra("productDes")
        val productPrice=intent.getStringExtra("productPrice")
        val productImage=intent.getStringExtra("productImage")

        hashMap["orderId"]=random
        hashMap["productTittle"]=productTittle.toString()
        hashMap["productDes"]=productDes.toString()
        hashMap["productPrice"]=productPrice.toString()
        hashMap["productImage"]=productImage.toString()

        proceedBtn.setOnClickListener {
            paymentViewModel.payment(productPrice.toString(),this)
        }


        productTittleTV.text=productTittle
        productDesTV.text=productDes
        productPriceTV.text=productPrice
        productPriceTV1.text=productPrice
        productPriceTV2.text=productPrice
        Glide.with(this).load(productImage).into(productImageView)

    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val uuid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        db.getReference("orders").child(uuid).child(random).setValue(hashMap)
            .addOnSuccessListener {
                // OrderFragment को अपडेट करने के लिए ViewModel से डेटा दोबारा लाना होगा
                val fragment = supportFragmentManager.findFragmentByTag("OrderFragment") as? OrderFragment
                fragment?.orderViewModel?.fetchOrderData()

                Toast.makeText(this, "myOrder Successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "myOrder Failed", Toast.LENGTH_SHORT).show()
            }


    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()

    }
}