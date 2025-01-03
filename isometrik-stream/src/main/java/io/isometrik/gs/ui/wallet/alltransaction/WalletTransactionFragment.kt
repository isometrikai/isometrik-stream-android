package io.isometrik.gs.ui.wallet.alltransaction

import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.isometrik.gs.ui.databinding.FragmentWalletTransactionBinding
import io.isometrik.gs.ui.utils.Constants
import io.isometrik.gs.ui.wallet.WalletTransactionsAdapter
import io.isometrik.gs.ui.wallet.transaction.Model.WalletTransactionItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * fragment for wallet transactions
 */
class WalletTransactionFragment(val mViewModel: AllTransactionsViewModel) : Fragment() {
    private var mWalletTransactionBinding: FragmentWalletTransactionBinding? = null
    private var mWalletTransactionsAdapter: WalletTransactionsAdapter? = null
    private var mWalletTransactionItemData = ArrayList<WalletTransactionItemData>()
    private var fragmentType : FragmentType? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mWalletTransactionBinding =
            FragmentWalletTransactionBinding.inflate(inflater, container, false)

        mViewModel.fetchTransaction( "")

        val linearLayoutManager = LinearLayoutManager(activity)
        mWalletTransactionBinding!!.rvWalletTransaction.layoutManager = linearLayoutManager
        mWalletTransactionsAdapter = WalletTransactionsAdapter(mWalletTransactionItemData)
        mWalletTransactionBinding!!.rvWalletTransaction.adapter = mWalletTransactionsAdapter
        return mWalletTransactionBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()
    }

    /**
     * does Initialization of basic fragment resources
     */
    private fun initialize() {
        if (arguments != null) {
             fragmentType =
                getFragmentTypeFromInt(requireArguments().getInt(Constants.FRAGMENT_TYPE))

            when (fragmentType) {
                FragmentType.ALL -> {
                    mViewModel.fetchTransaction( "")

                    mViewModel!!.allTransactionLiveData.observe(viewLifecycleOwner
                    ) { integerArrayListPair: Pair<Int, ArrayList<WalletTransactionItemData>?> ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            mWalletTransactionItemData = integerArrayListPair.second!!
                            mWalletTransactionsAdapter?.updateList(mWalletTransactionItemData)
                            mWalletTransactionBinding!!.vgNoTransactionsFound.visibility =
                                if (mWalletTransactionItemData.size > Constants.ZERO) View.GONE else View.VISIBLE
                        }
                    }
                }

                FragmentType.CREDIT -> {
                    mViewModel!!.fetchTransaction("CREDIT")

                    mViewModel!!.creditTransactionLiveData.observe(viewLifecycleOwner
                    ) { integerArrayListPair: Pair<Int, ArrayList<WalletTransactionItemData>?> ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            mWalletTransactionItemData = integerArrayListPair.second!!
                            mWalletTransactionsAdapter?.updateList(mWalletTransactionItemData)
                            mWalletTransactionBinding!!.vgNoTransactionsFound.visibility =
                                if (mWalletTransactionItemData.size > Constants.ZERO) View.GONE else View.VISIBLE
                        }
                    }
                }
                FragmentType.DEBIT -> {
                    mViewModel!!.fetchTransaction("DEBIT")

                    mViewModel!!.debitTransactionLiveData.observe(viewLifecycleOwner
                    ) { integerArrayListPair: Pair<Int, ArrayList<WalletTransactionItemData>?> ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            mWalletTransactionItemData = integerArrayListPair.second!!
                            mWalletTransactionsAdapter?.updateList(mWalletTransactionItemData)
                            mWalletTransactionBinding!!.vgNoTransactionsFound.visibility =
                                if (mWalletTransactionItemData.size > Constants.ZERO) View.GONE else View.VISIBLE
                        }
                    }
                }
                null -> {}
            }

        }
    }

}
