package com.example.testonlineshop.presentation

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.testonlineshop.Const.EMAIL_IS_EMPTY
import com.example.testonlineshop.Const.FIRST_NAME_IS_EMPTY
import com.example.testonlineshop.Const.INVALID_EMAIL
import com.example.testonlineshop.Const.LAST_NAME_IS_EMPTY
import com.example.testonlineshop.Const.USER_IS_EXIST
import com.example.testonlineshop.Const.USER_IS_REGISTERED
import com.example.testonlineshop.R
import com.example.testonlineshop.data.UserLoginData
import com.example.testonlineshop.databinding.FragmentSignInPageBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.Locale.filter

class SignInPageFragment : Fragment() {
    private lateinit var binding: FragmentSignInPageBinding
    private val viewModel: SignInPageViewModel by viewModels {
        SignInPageViewModelFactory(
            (activity?.application as SignInApplication).database.getDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logIn.setOnClickListener {
            val action = SignInPageFragmentDirections.actionSignInPageFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding.btSignIn.setOnClickListener {

            val firstName = binding.editFirstName.text.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                .filter { !it.isWhitespace() }
            val lastName = binding.editLastName.text.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                .filter { !it.isWhitespace() }
            val email = binding.editEmail.text.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                .filter { !it.isWhitespace() }

            /* Setting up message for Alert Dialog */
            val signInDataState = viewModel.checkUserLoginData(firstName, lastName, email)
            val state = setUpAlertDialogMessage(signInDataState)

            /* Navigate to LoginFragment if user is existed, another way put data to Db */
            if (state == "Correct data") {
                isUserExist(email)
                /* TODO( "Figure out how to return data from coroutine")*/
                val checkingValueFromDb = viewModel.checkingValue.value
                if (checkingValueFromDb == true) {
                    alertDialog(USER_IS_EXIST)
                    val action =
                        SignInPageFragmentDirections.actionSignInPageFragmentToLoginFragment()
                    findNavController().navigate(action)
                } else {
                    putIntoDb(firstName, lastName, email)
                    alertDialog(USER_IS_REGISTERED)
                }
            } else {

                /* Show message about correctness login data */
                alertDialog(state)
            }
        }
    }

    /* Launch alert dialog  */
    private fun alertDialog(state: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(state)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }

    /* Put login data to database */
    private fun putIntoDb(
        firstName: String,
        lastName: String,
        email: String
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            lifecycle.coroutineScope.launch {
                viewModel.insertUserLoginDataToDb(
                    UserLoginData(
                        0,
                        firstName,
                        lastName,
                        email
                    )
                )
            }
        }
    }

    /* Checking. Is user exist with current email */
    private fun isUserExist(email: String) {
        GlobalScope.launch(Dispatchers.IO) {
            lifecycle.coroutineScope.launch {
                viewModel.checkingIsUserExist(email)
            }
        }
    }

    private fun setUpAlertDialogMessage(signInDataState: Int): String {
        return when (signInDataState) {
            FIRST_NAME_IS_EMPTY -> "Enter your first name!"
            LAST_NAME_IS_EMPTY -> "Enter your last name!"
            EMAIL_IS_EMPTY -> "Enter your email!"
            INVALID_EMAIL -> "Enter valid email!"
            else -> "Correct data"
        }
    }
}
