package org.techtown.petfinder

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import org.techtown.petfinder.databinding.ActivitySignInBinding


class SignInActivity : BaseActivity() {

    lateinit var binding: ActivitySignInBinding
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            val intent = Intent(this, BottomNavMainActivity::class.java).apply {
                flags = flags or Intent.FLAG_ACTIVITY_NO_ANIMATION
            }
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
            return
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        binding.signUpButton.setOnClickListener() {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val email = binding.emailTextInput.editText!!.text.toString()
        val password = binding.passwordTextInput.editText!!.text.toString()

        if (email.isBlank()) {
            binding.emailTextInput.error = "이메일 주소를 입력해 주세요."
            return

        } else {
            binding.emailTextInput.error = null
            binding.emailTextInput.isErrorEnabled = false
        }

        if (password.isBlank()) {
            binding.passwordTextInput.error = "패스워드를 입력해 주세요."
            return

        } else {
            binding.passwordTextInput.error = null
            binding.passwordTextInput.isErrorEnabled = false
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            it.exception?.let {
                it.printStackTrace()
                // 계정이 없거나, 패스워드가 틀림

            } ?: run {
                val intent = Intent(this, BottomNavMainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun setValues() {

    }
}