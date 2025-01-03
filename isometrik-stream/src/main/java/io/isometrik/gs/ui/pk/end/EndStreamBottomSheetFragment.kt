package io.isometrik.gs.ui.pk.end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.isometrik.gs.ui.R
import io.isometrik.gs.ui.databinding.FragmentEndStreamBottomSheetBinding

/**
 * view all details of a streamer
 * @author Sreelatha Avula
 */
class EndStreamBottomSheetFragment : BottomSheetDialogFragment() {
    var mBinding: FragmentEndStreamBottomSheetBinding? = null
    lateinit var userId: String
    lateinit var streamId: String
    private var isBroadCaster: Boolean = false
    private var isFromProfile: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEndStreamBottomSheetBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTransTheme


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding?.ivClose?.setOnClickListener {
            dismiss()
        }
        initViewModel()
    }


    /**
     * initialize view model
     */
    private fun initViewModel() {
        mBinding?.tvLeave?.setOnClickListener {
            dismiss()
            requireActivity().finish()
        }
//        mBinding?.tvLeave?.visibility = if (isFromProfile) View.GONE else View.VISIBLE
    }

    /**
     * used to update user id and stream id
     */
    fun updateStreamId(userId: String, streamId: String, isBroadCaster: Boolean, isFromProfile: Boolean) {
        this.userId = userId
        this.streamId = streamId
        this.isBroadCaster = isBroadCaster
        this.isFromProfile = isFromProfile
    }
}