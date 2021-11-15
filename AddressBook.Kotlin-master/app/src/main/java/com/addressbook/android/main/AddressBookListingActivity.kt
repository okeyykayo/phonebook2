package com.addressbook.android.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addressbook.android.R
import com.addressbook.android.databinding.ActivityAddressBookListingBinding
import com.addressbook.android.main.respository.AddressBookRepository
import com.addressbook.android.main.respository.AddressViewFactory
import com.addressbook.android.main.viewModel.AddressViewModel
import com.addressbook.android.roomDatabase.db.AddressBook
import com.addressbook.android.roomDatabase.db.AddressBookDatabase
import com.addressbook.android.util.Constant
import com.addressbook.android.util.Globals
import java.io.Serializable
import java.util.*

class AddressBookListingActivity : AppCompatActivity(), AddressBookAdapter.OnAddressViewActionListener {


    private var addressbookList: List<AddressBook> = ArrayList()
    private var adapterAddressBookList: AddressBookAdapter? = null
    private var globals: Globals? = null
    private var ADD_EDIT_ADDRESS_REQ_CODE = 1010
    private lateinit var binding: ActivityAddressBookListingBinding

    private lateinit var viewModel: AddressViewModel
    lateinit var addressBookDatabase: AddressBookDatabase
    lateinit var addressBookRepository: AddressBookRepository
    lateinit var factory: AddressViewFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBookListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setToolbar()
        setUpList()
    }

    private fun setToolbar() {

        globals = applicationContext as Globals
        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbarTitle.text = getString(R.string.lbl_address_book)
        binding.toolbar.toolbarLeft.visibility = View.VISIBLE
        binding.toolbar.toolbarRight.visibility = View.VISIBLE
        binding.toolbar.toolbarLeft.text = getString(R.string.action_logout)
        binding.toolbar.toolbarRight.text = getString(R.string.action_add)

        binding.toolbar.toolbarRight.setOnClickListener(clickListener)
        binding.toolbar.toolbarLeft.setOnClickListener(clickListener)

    }


    private fun setUpList() {

        addressBookDatabase = AddressBookDatabase(this)
        addressBookRepository = AddressBookRepository(addressBookDatabase)
        factory = AddressViewFactory(addressBookRepository)
        viewModel = ViewModelProvider(this,factory)[AddressViewModel::class.java]

        viewModel.getAllAddress().observe(this, androidx.lifecycle.Observer {
            addressbookList = it
            setAdapter()

        })
    }

    private fun setAdapter() {
        if (addressbookList.isNotEmpty()) {
            if (adapterAddressBookList == null) {
                adapterAddressBookList = AddressBookAdapter(this)
            }
            adapterAddressBookList!!.setData(addressbookList)

            binding.rvAddressList.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(this@AddressBookListingActivity, RecyclerView.VERTICAL))
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                isFocusable = true
                adapter = adapterAddressBookList
            }
        }
        handleEmptyList()
    }



    private fun getContext(): Context {
        return this@AddressBookListingActivity
    }

    private fun handleEmptyList() {

        if (addressbookList.isEmpty()) {
            binding.tvNoList.visibility = View.VISIBLE
            binding.rvAddressList.visibility = View.GONE
        } else {
            binding.tvNoList.visibility = View.GONE
            binding.rvAddressList.visibility = View.VISIBLE
        }

    }

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.toolbar_right -> {
                saveAddressBook()
            }

            R.id.toolbar_left -> {
                globals?.setUserDetails(null)
                globals?.logoutProcess(getContext())
            }
        }
    }

    private fun saveAddressBook() {
        val addBook = Intent(this, EditRemoveAddressBookActivity::class.java)
        startActivityForResult(addBook, ADD_EDIT_ADDRESS_REQ_CODE)
    }

    private fun editAddressBook(addressBook: AddressBook) {

        val editBook = Intent(getContext(), EditRemoveAddressBookActivity::class.java)
        editBook.putExtra(Constant.Key_editAddressBook, addressBook as Serializable)
        startActivityForResult(editBook, ADD_EDIT_ADDRESS_REQ_CODE)

    }

    override fun onAddressDeleted(addressBook: AddressBook) {
        val dialog = AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog)
        dialog.setTitle("Delete Address!!")
                .setMessage("Are you sure you want to delete this address?")
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deleteProduct(addressBook)
                }.setNegativeButton(R.string.cancel, null).create().show()
    }

    override fun onAddressEdited(addressBook: AddressBook) {
        editAddressBook(addressBook)
    }
}
