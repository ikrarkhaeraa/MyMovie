package com.example.mymovie.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.mymovie.R
import com.example.mymovie.databinding.FragmentHomeBinding
import com.example.mymovie.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val model: LoginViewModel by activityViewModels()
    private lateinit var email: String
    private lateinit var password: String
    private var isEmailValid = false
    private var isPasswordValid = false

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailedittext.addTextChangedListener(emailTextWatcher)
        binding.passwordedittext.addTextChangedListener(passwordTextWatcher)
        binding.buttonMasuk.isEnabled = false

        binding.buttonMasuk.setOnClickListener {
            binding.buttonMasuk.visibility = INVISIBLE
            showLoading(true)
            model.setUserLoginState()
            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                findNavController().navigate(R.id.action_loginFragment_to_HomeFragment)
            }
        }
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not used in this case
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateEmail()
            updateSubmitButtonState()
        }

        override fun afterTextChanged(s: Editable?) {
            // Not used in this case
        }
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not used in this case
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validatePassword()
            updateSubmitButtonState()
        }

        override fun afterTextChanged(s: Editable?) {
            // Not used in this case
        }
    }

    private fun validateEmail() {
        email = binding.emailedittext.text.toString().trim()

        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textFieldEmail.error = getString(R.string.emailNotValid)
            isEmailValid = false
        } else {
            binding.textFieldEmail.error = null
            isEmailValid = true
        }
    }

    private fun validatePassword() {
        password = binding.passwordedittext.text.toString()

        if (!password.isEmpty() && password.length < 8) {
            binding.textFieldPassword.error = getString(R.string.passwordNotValid)
            isPasswordValid = false
        } else {
            binding.textFieldPassword.error = null
            isPasswordValid = true
        }
    }

    private fun updateSubmitButtonState() {
        val isBothFieldsValid = isEmailValid && isPasswordValid
        val isBothFieldsNotEmpty = binding.emailedittext.text?.isNotEmpty() == true && binding.passwordedittext.text?.isNotEmpty() == true
        binding.buttonMasuk.isEnabled = isBothFieldsValid && isBothFieldsNotEmpty
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = VISIBLE
        } else {
            binding.progressBar.visibility = GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

}