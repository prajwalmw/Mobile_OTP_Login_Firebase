package com.prajwal.prajwalwaingankar_roz.Verification_OTP

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.arch.core.executor.TaskExecutor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.prajwal.prajwalwaingankar_roz.Home_Screen.HomeScreen_Activity
import com.prajwal.prajwalwaingankar_roz.R
import java.util.concurrent.TimeUnit

class Verification_OTPActivity : AppCompatActivity() {
    lateinit var editText_otp: EditText
    lateinit var button_signin: Button
    private lateinit var mVerificationId: String
    private lateinit var mauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification__o_t_p)

        mauth = FirebaseAuth.getInstance()
        editText_otp = findViewById(R.id.edit_OTP_verifi)
        button_signin = findViewById(R.id.sign_in)

        val mobileIntent: String = intent?.getStringExtra("mobile") ?: "123456789"

        //getting mobile number from user entered flow and passing it to verification
        phone_verification(mobileIntent)

        //if automatic didnt work then user can enter the otp
        button_signin.setOnClickListener(View.OnClickListener {
            var edit_otp: String = editText_otp.text.toString()

            if(edit_otp.isEmpty() || edit_otp.length < 6)
            {
                editText_otp.setError("Enter valid code")
                editText_otp.requestFocus()
            }

            verify_VerificationCode(edit_otp)
        })

    }

    /**This function is used to verify the mobile number
     * of the user entered.
     * @param mobile
     * @return void
     */
    private fun phone_verification(mobile: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91" + mobile,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callBacks)
    }

    private val callBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            //Getting the code sent by SMS
            val code: String? = phoneAuthCredential.smsCode

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editText_otp.setText(code)
                //verifying the code
                verify_VerificationCode(code);
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@Verification_OTPActivity, e.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)
            mVerificationId = s //String
//            mResendToken = forceResendingToken // PhoneAuthProvider.ForceResendingToken
        }
    }

    /**This function takes the otp code as parameter and verifies it.
     * @param code
     */
    private fun verify_VerificationCode(code: String) {

        //credenials created
        var credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code)

        //user signing in
        signInWithPhoneAuthCredential(credential);
    }

    private fun signInWithPhoneAuthCredential(mcredential: PhoneAuthCredential)
    {
        mauth.signInWithCredential(mcredential).addOnCompleteListener(this,
        OnCompleteListener{task: Task<AuthResult> -> if(task.isComplete)
        {
            val intent = Intent(this, HomeScreen_Activity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
        })
    }


}