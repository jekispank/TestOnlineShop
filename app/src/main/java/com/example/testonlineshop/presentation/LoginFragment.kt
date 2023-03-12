package com.example.testonlineshop.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testonlineshop.Const
import com.example.testonlineshop.R
import com.example.testonlineshop.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            (activity?.application as SignInApplication).database.getDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btLogin: TextView = view.findViewById(R.id.bt_log_in)
        var firstName: EditText = view.findViewById(R.id.enter_first_name)
        var password: EditText = view.findViewById(R.id.password_edit)


        btLogin.setOnClickListener {

            val firstName = firstName.text.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                .filter { !it.isWhitespace() }
            val password = password.text.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                .filter { !it.isWhitespace() }

            val loginDataState = viewModel.checkUserFirstNameAndPassword(
                firstName,
                password
            )
            Log.d("LoginFragment", "$loginDataState")
            Log.d("LoginFragment", firstName)
            Log.d("LoginFragment", password)

            if (loginDataState == Const.FIRST_NAME_IS_EMPTY) {
                alertDialog("Enter your first name!")
            } else if (loginDataState == Const.PASSWORD_IS_EMPTY) {
                alertDialog("Enter your password")
            } else {
                isUserAccountExist(firstName)
                /* If user account exist in Db isUserAccountExist = true */
                val isUserExist = viewModel.checkingValue.value
                Log.d("LoginFragment", "isUserAccountExist $isUserExist")

                if (isUserExist == true) {
                    val action = LoginFragmentDirections.actionLoginFragmentToRootFragment()
                    findNavController().navigate(action)
                } else {
                    val action = LoginFragmentDirections.actionLoginFragmentToRootFragment()
                    findNavController().navigate(action)
                    Log.d("LoginFragment", "isUserAccountExist 2 option $isUserExist")
                }
            }
        }
    }

    private fun isUserAccountExist(firstName: String) {
        GlobalScope.launch (Dispatchers.IO){
            lifecycle.coroutineScope.launch {
                viewModel.checkingIsUserAccountExist(firstName)
            }
        }
    }


    /* Launch alert dialog  */
    private fun alertDialog(state: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(state)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }
}