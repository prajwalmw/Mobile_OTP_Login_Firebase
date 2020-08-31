package com.prajwal.prajwalwaingankar_roz.Enter_Number

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.prajwal.prajwalwaingankar_roz.R
import com.prajwal.prajwalwaingankar_roz.Verification_OTP.Verification_OTPActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.edit_phone_number)
        val button: Button = findViewById(R.id.button_send)


        button.setOnClickListener(View.OnClickListener
        {
            var mobileString: String = editText.text.toString().trim()

            if(mobileString.isEmpty() || mobileString.length < 10) {
                editText.setError("Enter a valid mobile number")
                editText.requestFocus()
            }

            val intent = Intent(this, Verification_OTPActivity::class.java)
            intent.putExtra("mobile", mobileString)
            startActivity(intent)
        })

    }


}