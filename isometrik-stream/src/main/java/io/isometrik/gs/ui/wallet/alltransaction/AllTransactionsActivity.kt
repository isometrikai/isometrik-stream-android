package io.isometrik.gs.ui.wallet.alltransaction

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import io.isometrik.gs.ui.R
import io.isometrik.gs.ui.databinding.ActivityAllTransactionsBinding
import io.isometrik.gs.ui.utils.Constants


/**
 * activity to show all the transactions details
 */
class AllTransactionsActivity : AppCompatActivity() {

    private lateinit var mAllTransactionFragment: WalletTransactionFragment
    private lateinit var mAllCreditFragment: WalletTransactionFragment
    private lateinit var mAllDebitFragment: WalletTransactionFragment

    private var mWalletBinding: ActivityAllTransactionsBinding? = null
    private lateinit var mViewModel: AllTransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(
            AllTransactionsViewModel::class.java
        )

        mAllTransactionFragment = WalletTransactionFragment(mViewModel)
        mAllCreditFragment = WalletTransactionFragment(mViewModel)
        mAllDebitFragment = WalletTransactionFragment(mViewModel)

        val currency = intent.getStringExtra(CURRENCY)
        mViewModel.updateCurrency(currency)
        mWalletBinding = ActivityAllTransactionsBinding.inflate(
            layoutInflater
        )
        setContentView(mWalletBinding!!.root)
        initializeView()
        subScribeToEmptyTransactions()
        dataAndSetUpUI
    }

    /**
     * Initialising the View using Data Binding.
     */
    private fun initializeView() {
        mWalletBinding!!.tvCategoryName.text = intent.getStringExtra(TITLE)
        mWalletBinding!!.ivBack.setOnClickListener { v: View? -> onBackPressed() }

    }


    /**
     * initialize the fragment manager.
     */
    private fun initialize() {
        val fragmentManager = supportFragmentManager
        val adapter = EcomWalletPagerAdapter(fragmentManager)
        adapter.addFragment(mAllTransactionFragment, getString(R.string.allTransactions))
        adapter.addFragment(mAllCreditFragment, getString(R.string.credits))
        adapter.addFragment(mAllDebitFragment, getString(R.string.debits))
        mWalletBinding!!.vpWallet.adapter = adapter
        mWalletBinding!!.tabEcomWallet.setupWithViewPager(mWalletBinding!!.vpWallet)

        mWalletBinding!!.vpWallet.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Not used in this case
            }

            override fun onPageSelected(position: Int) {
                // Update the last selected index
                mViewModel.fragmentSelected(getFragmentTypeFromInt(position + 1))
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not used in this case
            }
        })
    }

    /**
     * subscribe to empty transaction data
     */
    private fun subScribeToEmptyTransactions() {
        mViewModel!!.emptyTransactions.observe(
            this
        ) { aBoolean: Boolean? ->
            initialize()
        }
    }


    private val dataAndSetUpUI: Unit
        /**
         * get data and set up UI
         */
        get() {
            val allBundle = Bundle()
            allBundle.putInt(
                Constants.FRAGMENT_TYPE,
                FragmentType.ALL.value
            )
            mAllTransactionFragment.arguments = allBundle

            val creditBundle = Bundle()
            creditBundle.putInt(
                Constants.FRAGMENT_TYPE,
                FragmentType.CREDIT.value
            )
            mAllCreditFragment.arguments = creditBundle

            val debitBundle = Bundle()
            debitBundle.putInt(
                Constants.FRAGMENT_TYPE,
                FragmentType.DEBIT.value
            )
            mAllDebitFragment.arguments = debitBundle

//            mWalletBinding!!.progress.visibility = View.GONE
            initialize()
        }

    val isLoading: Boolean
        get() = mViewModel!!.progressVisible.value!!



    override fun onDestroy() {
        super.onDestroy()
        mWalletBinding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    companion object {
        const val CURRENCY: String = "currency"
        const val TITLE: String = "title"
    }
}
