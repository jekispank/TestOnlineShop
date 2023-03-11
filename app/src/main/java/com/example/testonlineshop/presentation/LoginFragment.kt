package com.example.testonlineshop.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.testonlineshop.Const
import com.example.testonlineshop.databinding.FragmentLoginBinding

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
    ): View? {
        val binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btLogIn.setOnClickListener {

            val firstName = binding.enterFirstName.text.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                .filter { !it.isWhitespace() }
            val password = binding.passwordEdit.text.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                .filter { !it.isWhitespace() }

            val loginDataState = viewModel.checkUserFirstNameAndPassword(firstName, password)

            if (loginDataState == Const.FIRST_NAME_IS_EMPTY) alertDialog("Enter your first name!")
            else {
                if (loginDataState == Const.PASSWORD_IS_EMPTY) alertDialog("Enter your password")
                else {
                    /* If user account exist in Db isUserAccountExist = true */
                    val isUserAccountExist = viewModel.checkingValue.value

                    if (isUserAccountExist == true) {
                        val action = LoginFragmentDirections.actionLoginFragmentToRootFragment()
                        findNavController().navigate(action)
                    }
                    else {
                        val action = LoginFragmentDirections.actionLoginFragmentToSignInPageFragment()
                        findNavController().navigate(action)
                    }
                }
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
}