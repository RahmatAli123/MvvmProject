package com.example.mvvmauth.AllViewModel

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.razorpay.Checkout
import org.json.JSONException
import org.json.JSONObject

class PaymentViewModel: ViewModel() {
    fun payment(price: String?, activity: Activity) {
        val co = Checkout()
        co.setKeyID("rzp_test_7QRDUFvP75uLJS")

        // Price check karna zaroori hai
        if (price.isNullOrEmpty()) {
            Toast.makeText(activity, "Invalid Price", Toast.LENGTH_SHORT).show()
            return
        }

        val cleanedPrice = price.replace("₹", "").trim()  // ₹ hatao aur trim karo

        val amount = try {
            (cleanedPrice.toDouble() * 100).toInt()  // Convert to paisa
        } catch (e: NumberFormatException) {
            Toast.makeText(activity, "Invalid price format", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val options = JSONObject()
            options.put("name", "Razorpay Corp")
            options.put("description", "Demoing Charges")
            options.put("image", "http://example.com/image/rzp.jpg")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", amount)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            val prefill = JSONObject()
            prefill.put("email", "gaurav.kumar@example.com")
            prefill.put("contact", "9876543210")

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: JSONException) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

}