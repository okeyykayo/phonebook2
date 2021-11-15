package com.addressbook.android.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.addressbook.android.R
import com.addressbook.android.databinding.ActivityEditRemoveAddressBookBinding
import com.addressbook.android.main.respository.AddressBookRepository
import com.addressbook.android.main.respository.AddressViewFactory
import com.addressbook.android.main.viewModel.AddressViewModel
import com.addressbook.android.roomDatabase.db.AddressBook
import com.addressbook.android.roomDatabase.db.AddressBookDatabase
import com.addressbook.android.util.Constant
import com.addressbook.android.util.Globals
import com.addressbook.android.util.UtilsValidation


class EditRemoveAddressBookActivity : AppCompatActivity() {

    internal var globals: Globals? = null

    private var isUpdate = false
    private var extra: Bundle? = null
    private var mAddressBook: AddressBook? = null

    lateinit var binding: ActivityEditRemoveAddressBookBinding
    lateinit var viewModel: AddressViewModel
    lateinit var addressBookDatabase: AddressBookDatabase
    lateinit var addressBookRepository: AddressBookRepository
    lateinit var factory: AddressViewFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRemoveAddressBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        globals = applicationContext as Globals
        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbarTitle.text = getString(R.string.lbl_detail)
        binding.toolbar.toolbarLeft.visibility = View.VISIBLE
        binding.toolbar.toolbarRight.visibility = View.VISIBLE
        binding.toolbar.toolbarLeft.text = getString(R.string.lbl_address_book)
        binding.toolbar.toolbarRight.text = getString(R.string.action_logout)

        addressBookDatabase = AddressBookDatabase(this)
        addressBookRepository = AddressBookRepository(addressBookDatabase)
        factory = AddressViewFactory(addressBookRepository)
        viewModel = ViewModelProvider(this,factory)[AddressViewModel::class.java]

        extra = intent.extras
        if (extra != null && extra!!.containsKey(Constant.Key_editAddressBook)) {
            isUpdate = true
            mAddressBook = extra!!.getSerializable(Constant.Key_editAddressBook) as AddressBook
        }

        // set the text in button based on adding or editing addressbook
        if (isUpdate) {
            // set the value from intent bundle
            binding.edtName.setText(mAddressBook?.name)
            binding.edtEmail.setText(mAddressBook?.email)
            binding.edtContactNo.setText(mAddressBook?.contact_number)
            mAddressBook?.isactive?.let { binding.switchActive.isChecked=it }

            binding.btnSaveUpdate.text = getString(R.string.action_update)
        } else {
            binding.btnSaveUpdate.text = getString(R.string.action_save)
        }

        binding.toolbar.toolbarLeft.setOnClickListener(clickListener)
        binding.toolbar.toolbarRight.setOnClickListener(clickListener)
        binding.btnSaveUpdate.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener { view ->

        when (view.id) {

            R.id.toolbar_left -> {
                globals?.hideKeyboard(getContextActivity())
                onBackPressed()
            }

            R.id.toolbar_right -> {
                globals?.hideKeyboard(getContextActivity())
                globals?.setUserDetails(null)
                globals?.logoutProcess(getContext())
            }

            R.id.btn_save_update -> {
                globals?.hideKeyboard(getContextActivity())
                if (isUpdate) {
                    updateAddressBook()
                } else {
                    insertAddressBook()
                }
            }
        }
    }

    private fun insertAddressBook() {

        if (isValid()) {
            val addressBook = AddressBook()
            addressBook.name = binding.edtName.text.toString().trim()
            addressBook.email = binding.edtEmail.text.toString().trim()
            addressBook.contact_number = binding.edtContactNo.text.toString().trim()
            addressBook.isactive = binding.switchActive.isChecked

            viewModel.insertAddressBook(addressBook).also { finish() }

        }
    }


    private fun updateAddressBook() {
        if (isValid()) {
            val addressBook = AddressBook()
            addressBook.name = binding.edtName.text.toString().trim()
            addressBook.email = binding.edtEmail.text.toString().trim()
            addressBook.contact_number = binding.edtContactNo.text.toString().trim()
            addressBook.isactive = binding.switchActive.isChecked
            addressBook.id = mAddressBook?.id

            viewModel.updateAddressBook(addressBook).also { finish() }
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    fun isValid(): Boolean {
        if (UtilsValidation.validateEmptyEditText(binding.edtName)) {
            globals?.showToast(getContext(), getString(R.string.toast_err_name))
            requestFocus(binding.edtName)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(binding.edtEmail)) {
            globals?.showToast(getContext(), getString(R.string.toast_err_email))
            requestFocus(binding.edtEmail)
            return false
        }
        if (UtilsValidation.validateEmail(binding.edtEmail)) {
            globals?.showToast(getContext(), getString(R.string.toast_err_enter_valid_email))
            requestFocus(binding.edtEmail)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(binding.edtContactNo)) {
            globals?.showToast(getContext(), getString(R.string.toast_err_contact_no))
            requestFocus(binding.edtContactNo)
            return false
        }
        if (UtilsValidation.validatePhoneNumber(binding.edtContactNo)) {
            globals?.showToast(getContext(), getString(R.string.toast_err_enter_valid_contact_number))
            requestFocus(binding.edtContactNo)
            return false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun getContext(): Context {
        return this@EditRemoveAddressBookActivity
    }

    private fun getContextActivity(): Activity {
        return this@EditRemoveAddressBookActivity
    }
}
