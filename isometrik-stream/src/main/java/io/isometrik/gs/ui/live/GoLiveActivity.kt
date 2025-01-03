package io.isometrik.gs.ui.live

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import io.isometrik.gs.rtcengine.ar.capture.CaptureOperations
import io.isometrik.gs.rtcengine.ar.capture.ImageCaptureCallbacks
import io.isometrik.gs.ui.IsometrikStreamSdk
import io.isometrik.gs.ui.R
import io.isometrik.gs.ui.databinding.IsmActivityGoliveBinding
import io.isometrik.gs.ui.ecommerce.products.link.LinkProductsFragment
import io.isometrik.gs.ui.ecommerce.products.link.LinkedProductsCallback
import io.isometrik.gs.ui.effects.EffectsFragment
import io.isometrik.gs.ui.members.select.MultiLiveSelectMembersContract
import io.isometrik.gs.ui.restream.RestreamChannelsActionCallback
import io.isometrik.gs.ui.restream.add.AddOrEditChannelsFragment
import io.isometrik.gs.ui.restream.list.FetchChannelsFragment
import io.isometrik.gs.ui.scrollable.webrtc.MultiliveStreamsActivity
import io.isometrik.gs.ui.utils.AlertProgress
import io.isometrik.gs.ui.utils.GlideApp
import io.isometrik.gs.ui.utils.ImageFilePathUtils
import io.isometrik.gs.ui.utils.ImageUtil
import io.isometrik.gs.ui.utils.Utilities
import io.isometrik.gs.ui.utils.blur.BlurTransformation
import java.io.File
import java.io.IOException

class GoLiveActivity : AppCompatActivity(), GoLiveContract.View,
    ImageCaptureCallbacks, RestreamChannelsActionCallback, LinkedProductsCallback {
    var goLivePresenter: GoLiveContract.Presenter? = null

    private var isPublic = true
    private var multiGuestLive = true
    private var cleanUpRequested = false
    private var capturedImagePath: String? = null
    private var mediaPath: String? = null

    private var alertProgress: AlertProgress? = null
    private var alertDialog: AlertDialog? = null

    private var captureOperations: CaptureOperations? = null
    private var fetchChannelsFragment: FetchChannelsFragment? = null
    private var addOrEditChannelsFragment: AddOrEditChannelsFragment? = null
    private var effectsFragment: EffectsFragment? = null
    private var linkProductsFragment: LinkProductsFragment? = null

    private var ismActivityGoliveBinding: IsmActivityGoliveBinding? = null
    private var selectImageFromGalleryActivityLauncher: ActivityResultLauncher<Intent>? = null
    private val productIds = ArrayList<String>()

    private var rtmpIngest = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ismActivityGoliveBinding = IsmActivityGoliveBinding.inflate(
            layoutInflater
        )
        val view: View = ismActivityGoliveBinding!!.root
        setContentView(view)

        goLivePresenter = GoLivePresenter(this)
        alertProgress = AlertProgress()
        effectsFragment = EffectsFragment()

        if (IsometrikStreamSdk.getInstance().isometrik.isARFiltersEnabled) {
            ismActivityGoliveBinding!!.arCameraView.visibility = View.VISIBLE
            ismActivityGoliveBinding!!.normalCameraView.visibility = View.GONE
            ismActivityGoliveBinding!!.ivEffects.visibility = View.VISIBLE
        } else {
            ismActivityGoliveBinding!!.arCameraView.visibility = View.GONE
            ismActivityGoliveBinding!!.normalCameraView.visibility = View.VISIBLE
            ismActivityGoliveBinding!!.ivEffects.visibility = View.GONE
        }

        ismActivityGoliveBinding!!.etStreamDescription.setText(IsometrikStreamSdk.getInstance().userSession.userName)
        captureOperations = CaptureOperations(
            IsometrikStreamSdk.getInstance().isometrik,
            ismActivityGoliveBinding!!.arCameraView,
            ismActivityGoliveBinding!!.normalCameraView,
            this
        )
        IsometrikStreamSdk.getInstance().isometrik.setImageCaptureCallbacks(this)
        checkImageCapturePermissions()

        fetchChannelsFragment = FetchChannelsFragment()
        addOrEditChannelsFragment = AddOrEditChannelsFragment()

        ismActivityGoliveBinding!!.swSelfHosted.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            ismActivityGoliveBinding!!.rlRtmpIngest.visibility =
                if (b) View.VISIBLE else View.GONE
        }




        ismActivityGoliveBinding!!.rlDeviceStream.setOnClickListener { view12: View? -> onLiveFromDeviceSelected() }
        ismActivityGoliveBinding!!.rlMultiStream.setOnClickListener { view1: View? -> onSingleMultiLiveSelected() }

        ismActivityGoliveBinding!!.ivEffects.setOnClickListener { view15: View? ->
            effectsFragment!!.updateParameters(false, true)
            effectsFragment!!.show(supportFragmentManager, EffectsFragment.TAG)
        }

        ismActivityGoliveBinding!!.tvGoLive.setOnClickListener { view16: View? ->
            if (ismActivityGoliveBinding!!.etStreamDescription.text != null && ismActivityGoliveBinding!!.etStreamDescription.text.toString().trim { it <= ' ' }
                    .isNotEmpty()
            ) {
                if (ismActivityGoliveBinding!!.swEcommerce.isChecked && productIds.size < 1) {
                    Toast.makeText(
                        this@GoLiveActivity,
                        getString(R.string.ism_link_atleast_one_product),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (multiGuestLive) {
                        captureOrUploadImage()
                    } else {
                        if (ismActivityGoliveBinding!!.swAudioOnly.isChecked && ismActivityGoliveBinding!!.swRestream.isChecked) {
                            Toast.makeText(
                                this@GoLiveActivity,
                                getString(R.string.ism_restream_audio_only_not_supported),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (isPublic) {
                                Toast.makeText(
                                    this@GoLiveActivity,
                                    getString(R.string.ism_public_not_supported),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                captureOrUploadImage()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this@GoLiveActivity,
                    getString(R.string.ism_invalid_broadcast_description),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ismActivityGoliveBinding?.iHeader?.ivBack?.setOnClickListener { view17: View? ->
            onBackPressed()
        }

        ismActivityGoliveBinding!!.rlCoverImage.setOnClickListener { view17: View? ->
            if (!Utilities.checkSelfExternalStoragePermissionIsGranted(
                    this@GoLiveActivity,
                    true
                )
            ) {
                if (Utilities.shouldShowExternalPermissionStorageRational(
                        this@GoLiveActivity,
                        true
                    )
                ) {
                    val snackbar = Snackbar.make(
                        ismActivityGoliveBinding!!.rlParent,
                        R.string.ism_permission_gallery,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        getString(R.string.ism_ok)
                    ) { view1: View? -> this.requestGalleryPermission() }

                    snackbar.show()

//                    (snackbar.view.findViewById<View>(R.id.snackbar_text) as TextView).gravity =
//                        Gravity.CENTER_HORIZONTAL
                } else {
                    requestGalleryPermission()
                }
            } else {
                selectImageFromGallery()
            }
        }

        ismActivityGoliveBinding!!.ivRemoveCoverImage.setOnClickListener { view18: View? ->
            ismActivityGoliveBinding!!.ivRemoveCoverImage.visibility =
                View.GONE
            mediaPath = null
            try {
                GlideApp.with(this@GoLiveActivity)
                    .clear(ismActivityGoliveBinding!!.ivCoverImage)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
        }

        selectImageFromGalleryActivityLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    mediaPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        ImageFilePathUtils.getPathAboveN(this, data!!.data)
                    } else {
                        ImageFilePathUtils.getPath(this, data!!.data)
                    }

                    try {
                        GlideApp.with(this).load(mediaPath).centerCrop()
                            .listener(object : RequestListener<Drawable?> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any,
                                    target: Target<Drawable?>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any,
                                    target: Target<Drawable?>,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    ismActivityGoliveBinding!!.ivRemoveCoverImage.visibility =
                                        View.VISIBLE
                                    return false
                                }
                            }).into(ismActivityGoliveBinding!!.ivCoverImage)
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    this@GoLiveActivity,
                    getString(R.string.ism_gallery_cancel),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ismActivityGoliveBinding!!.swEcommerce.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                ismActivityGoliveBinding!!.incLinkProducts.rlLinkProducts.visibility =
                    View.VISIBLE
                ismActivityGoliveBinding!!.tvSelfHosted.visibility = View.GONE
                ismActivityGoliveBinding!!.swSelfHosted.visibility = View.GONE
                ismActivityGoliveBinding!!.rlRtmpIngest.visibility = View.GONE
            } else {
                ismActivityGoliveBinding!!.incLinkProducts.rlLinkProducts.visibility =
                    View.GONE
                if (multiGuestLive) {
                    ismActivityGoliveBinding!!.tvSelfHosted.visibility = View.VISIBLE
                    ismActivityGoliveBinding!!.swSelfHosted.visibility = View.VISIBLE
                    ismActivityGoliveBinding!!.rlRtmpIngest.visibility =
                        if (ismActivityGoliveBinding!!.swSelfHosted.isChecked) View.VISIBLE else View.GONE
                } else {
                    ismActivityGoliveBinding!!.tvSelfHosted.visibility = View.GONE
                    ismActivityGoliveBinding!!.swSelfHosted.visibility = View.GONE
                    ismActivityGoliveBinding!!.rlRtmpIngest.visibility = View.GONE
                }
            }
        }
        ismActivityGoliveBinding!!.incLinkProducts.rlLinkProducts.setOnClickListener { v: View? ->
            linkProductsFragment = LinkProductsFragment(this@GoLiveActivity)
            linkProductsFragment!!.show(supportFragmentManager, LinkProductsFragment.TAG)
        }

        ismActivityGoliveBinding!!.swRestream.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
//            if (isChecked) {
//                ismActivityGoliveBinding!!.incRestreamView.relRestreamView.setVisibility(View.VISIBLE)
//            } else {
//                ismActivityGoliveBinding!!.incRestreamView.relRestreamView.setVisibility(View.GONE)
//            }
        }
    }

    /**
     * [GoLiveContract.View.onImageUploadResult]
     */
    override fun onImageUploadResult(url: String) {
        hideProgressDialog()

        if (ismActivityGoliveBinding!!.etStreamDescription.text != null) {
            if (multiGuestLive) {
                if (IsometrikStreamSdk.getInstance().isometrik.isARFiltersEnabled) {
                    //To release camera surfaceview explicitly
                    ismActivityGoliveBinding!!.arCameraView.visibility = View.GONE
                    cleanupOnActivityDestroy()
                }


                showProgressDialog(getString(R.string.ism_starting_broadcast))

                goLivePresenter?.startBroadcast(
                    ismActivityGoliveBinding!!.etStreamDescription.text.toString().trim(), url,
                    arrayListOf(), isPublic, ismActivityGoliveBinding!!.swAudioOnly.isChecked, true,
                    ismActivityGoliveBinding!!.swEnableRecording.isChecked,
                    ismActivityGoliveBinding!!.swHdBroadcast.isChecked,
                    true,
                    ismActivityGoliveBinding!!.swRestream.isChecked,
                    ismActivityGoliveBinding!!.swEcommerce.isChecked,
                    if (ismActivityGoliveBinding!!.swEcommerce.isChecked) productIds else ArrayList(),
                    ismActivityGoliveBinding!!.swSelfHosted.isChecked,
                    rtmpIngest,
                    (ismActivityGoliveBinding!!.rlRtmpIngest.findViewById<View>(R.id.swPersistRtmpIngestEndpoint) as SwitchCompat).isChecked

                )
            } else {
                //Go live directly

                showProgressDialog(getString(R.string.ism_starting_broadcast))

                goLivePresenter!!.startBroadcast(
                    ismActivityGoliveBinding!!.etStreamDescription.text.toString()
                        .trim { it <= ' ' },
                    url,
                    isPublic,
                    ismActivityGoliveBinding!!.swAudioOnly.isChecked,
                    false,
                    ismActivityGoliveBinding!!.swEnableRecording.isChecked,
                    ismActivityGoliveBinding!!.swHdBroadcast.isChecked,
                    ismActivityGoliveBinding!!.swLowLatency.isChecked,
                    ismActivityGoliveBinding!!.swRestream.isChecked,
                    ismActivityGoliveBinding!!.swEcommerce.isChecked,
                    if (ismActivityGoliveBinding!!.swEcommerce.isChecked) productIds else ArrayList(),
                    false,
                    false,
                    false
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        var permissionDenied = false

        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                permissionDenied = true
                break
            }
        }
        if (permissionDenied) {
            if (requestCode == 0) {
                Toast.makeText(
                    this,
                    getString(R.string.ism_permission_start_streaming_denied),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.ism_permission_gallery_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            if (requestCode == 0) {
                openCamera()
            } else {
                selectImageFromGallery()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Check streaming permissions.
     */
    fun checkImageCapturePermissions() {
        if ((ContextCompat.checkSelfPermission(
                this@GoLiveActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED) || !Utilities.checkSelfExternalStoragePermissionIsGranted(
                this@GoLiveActivity, false
            ) || (ContextCompat.checkSelfPermission(
                this@GoLiveActivity, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    this@GoLiveActivity,
                    Manifest.permission.CAMERA
                ))

                || Utilities.shouldShowExternalPermissionStorageRational(this@GoLiveActivity, false)

                || (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@GoLiveActivity,
                    Manifest.permission.RECORD_AUDIO
                ))
            ) {
                val snackbar = Snackbar.make(
                    ismActivityGoliveBinding!!.rlParent,
                    R.string.ism_permission_start_streaming,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    getString(R.string.ism_ok)
                ) { view: View? -> this.requestCameraPermissions() }
                snackbar.show()


//                (snackbar.view.findViewById<View>(R.id.snackbar_text) as TextView).gravity =
//                    Gravity.CENTER_HORIZONTAL
            } else {
                requestCameraPermissions()
            }
        } else {
            openCamera()
        }
    }

    private fun requestCameraPermissions() {
        val permissionsRequired = ArrayList<String>()
        permissionsRequired.add(Manifest.permission.CAMERA)
        permissionsRequired.add(Manifest.permission.RECORD_AUDIO)
        permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(false))

        val p = permissionsRequired.toTypedArray<String>()

        ActivityCompat.requestPermissions(this@GoLiveActivity, p, 0)
    }

    private fun requestGalleryPermission() {
        val permissionsRequired = ArrayList<String>()


        permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(true))

        val p = permissionsRequired.toTypedArray<String>()

        ActivityCompat.requestPermissions(this@GoLiveActivity, p, 1)
    }

    private fun selectImageFromGallery() {
        selectImageFromGalleryActivityLauncher!!.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }

    override fun onBackPressed() {
        cleanupOnActivityDestroy()
        try {
            super.onBackPressed()
        } catch (ignore: Exception) {
        }
    }

    override fun onDestroy() {
        cleanupOnActivityDestroy()
        super.onDestroy()
    }

    private fun cleanupOnActivityDestroy() {
        if (!cleanUpRequested) {
            cleanUpRequested = true
            captureOperations!!.cleanup()
            if (capturedImagePath != null) {
                goLivePresenter!!.deleteImage(File(capturedImagePath))
            }
            IsometrikStreamSdk.getInstance().isometrik.setImageCaptureCallbacks(null)
            hideProgressDialog()
        }
    }

    /**
     * [GoLiveContract.View.onImageUploadError]
     */
    override fun onImageUploadError(errorMessage: String) {
        hideProgressDialog()
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun showProgressDialog(message: String) {
        alertDialog = alertProgress!!.getProgressDialog(this, message)
        if (!isFinishing && !isDestroyed) alertDialog?.show()
    }

    private fun hideProgressDialog() {
        if (alertDialog != null && alertDialog!!.isShowing) alertDialog!!.dismiss()
    }

    /**
     * [GoLiveContract.View.onBroadcastStarted]
     */
    override fun onBroadcastStarted(
        streamId: String,
        streamDescription: String,
        streamImageUrl: String,
        memberIds: ArrayList<String>,
        startTime: Long,
        userId: String,
        audioOnly: Boolean,
        ingestEndpoint: String,
        streamKey: String,
        hdBroadcast: Boolean,
        restream: Boolean,
        rtcToken: String,
        restreamEndpoints: ArrayList<String>,
        productsLinked: Boolean,
        productsCount: Int,
        rtmpIngestUrl: String
    ) {
        hideProgressDialog()
        memberIds.add(userId)

        if (IsometrikStreamSdk.getInstance().isometrik.isARFiltersEnabled) {
            //To release camera surfaceview explicitly for bug of camera hanging if gone live without multilive, due to surfaceview being not released
            ismActivityGoliveBinding!!.arCameraView.visibility = View.GONE
            cleanupOnActivityDestroy()
        }

        //Intent intent = new Intent(GoLiveActivity.this, ScrollableSingleStreamsActivity.class);
        val intent = Intent(this@GoLiveActivity, MultiliveStreamsActivity::class.java)

        intent.putExtra("streamId", streamId)
        intent.putExtra("streamDescription", streamDescription)
        intent.putExtra("streamImage", streamImageUrl)
        intent.putExtra("startTime", startTime)
        intent.putExtra("membersCount", memberIds.size)
        intent.putExtra("viewersCount", 0)
        intent.putExtra("publishersCount", 1)
        intent.putExtra("joinRequest", true)
        intent.putExtra("isAdmin", true)
        intent.putStringArrayListExtra("memberIds", memberIds)
        intent.putExtra("initiatorName", IsometrikStreamSdk.getInstance().userSession.userName)
        intent.putExtra("publishRequired", false)
        intent.putExtra("initiatorId", IsometrikStreamSdk.getInstance().userSession.userId)

        intent.putExtra(
            "initiatorIdentifier",
            IsometrikStreamSdk.getInstance().userSession.userIdentifier
        )
        intent.putExtra("initiatorImage", IsometrikStreamSdk.getInstance().userSession.userProfilePic)
        intent.putExtra("isPublic", isPublic)
        intent.putExtra("isBroadcaster", true)
        intent.putExtra("audioOnly", audioOnly)
        intent.putExtra("ingestEndpoint", ingestEndpoint)
        intent.putExtra("streamKey", streamKey)
        intent.putExtra("multiLive", false)
        intent.putExtra("hdBroadcast", hdBroadcast)
        intent.putExtra("rtcToken", rtcToken)
        intent.putExtra("restream", restream)
        intent.putStringArrayListExtra("restreamEndpoints", restreamEndpoints)
        intent.putExtra("productsLinked", productsLinked)
        intent.putExtra("productsCount", productsCount)
        intent.putExtra("isModerator", true)
        intent.putExtra("selfHosted", false)
        intent.putExtra("rtmpIngest", false)
        intent.putExtra("persistRtmpIngestEndpoint", false)
        intent.putExtra("rtmpIngestUrl", rtmpIngestUrl)

        startActivity(intent)
        finish()
    }

    /**
     * [GoLiveContract.View.onError]
     */
    override fun onError(errorMessage: String?) {
        hideProgressDialog()

        runOnUiThread {
            if (errorMessage != null) {
                Toast.makeText(this@GoLiveActivity, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@GoLiveActivity,
                    getString(R.string.ism_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openCamera() {
        captureOperations!!.openCamera(this, windowManager)
    }

    private fun requestImageCapture() {
        showProgressDialog(getString(R.string.ism_capturing_image))
        if (IsometrikStreamSdk.getInstance().isometrik.isARFiltersEnabled) {
            captureOperations!!.requestARImageCapture()
        } else {
            try {
                captureOperations!!.captureNormalPhoto(
                    ImageUtil.createImageFile(
                        System.currentTimeMillis().toString(), true,
                        this
                    )
                )
            } catch (ignore: IOException) {
                Toast.makeText(
                    this@GoLiveActivity,
                    getString(R.string.ism_image_capture_failure),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun arImageCaptured(bitmap: Bitmap?) {
        hideProgressDialog()
        if (bitmap != null) {
            Handler().post {
                capturedImagePath = ImageUtil.saveCapturedBitmap(
                    bitmap,
                    this
                )
                if (capturedImagePath != null) {
                    showProgressDialog(getString(R.string.ism_uploading_image))
                    try {
                        GlideApp.with(this).load(capturedImagePath)
                            .transform(CenterCrop()).transform(BlurTransformation())
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(
                                ismActivityGoliveBinding!!.ivCoverImagePlaceHolder
                            )
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }

                    goLivePresenter!!.requestImageUpload(capturedImagePath)
                } else {
                    Toast.makeText(
                        this@GoLiveActivity,
                        getString(R.string.ism_image_capture_failure),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                this@GoLiveActivity,
                getString(R.string.ism_image_capture_failure),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun normalImageCaptured(file: File, success: Boolean) {
        runOnUiThread {
            hideProgressDialog()
            if (success) {
                capturedImagePath = file.absolutePath
                showProgressDialog(getString(R.string.ism_uploading_image))
                try {
                    GlideApp.with(this@GoLiveActivity).load(capturedImagePath)
                        .transform(CenterCrop()).transform(BlurTransformation())
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(
                            ismActivityGoliveBinding!!.ivCoverImagePlaceHolder
                        )
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }

                goLivePresenter!!.requestImageUpload(capturedImagePath)
            } else {
                Toast.makeText(
                    this@GoLiveActivity,
                    getString(R.string.ism_image_capture_failure),
                    Toast.LENGTH_SHORT
                ).show()
                goLivePresenter!!.deleteImage(file)
            }
        }
    }

    private fun captureOrUploadImage() {
        ///////////////
        if (mediaPath != null && ismActivityGoliveBinding!!.ivRemoveCoverImage.visibility == View.VISIBLE) {
            showProgressDialog(getString(R.string.ism_uploading_image))
            try {
                GlideApp.with(this)
                    .load(mediaPath)
                    .transform(CenterCrop())
                    .transform(BlurTransformation())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ismActivityGoliveBinding!!.ivCoverImagePlaceHolder)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
            goLivePresenter!!.requestImageUpload(mediaPath)
        } else {
            requestImageCapture()
        }
        ///////////////
//        mediaPath = if(IsometrikStreamSdk.getInstance().userSession.userName.contains("Dev"))"https://res.cloudinary.com/dedibgpdw/image/upload/v1733893285/streams/soghs9dw3ihu90ux5g2v.jpg" else "https://res.cloudinary.com/dedibgpdw/image/upload/v1733893502/streams/nllle2pdycfk7tdftlgo.jpg"
//        try {
//            GlideApp.with(this)
//                .load(mediaPath)
//                .transform(CenterCrop())
//                .transform(BlurTransformation())
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(ismActivityGoliveBinding!!.ivCoverImagePlaceHolder)
//        } catch (ignore: IllegalArgumentException) {
//        } catch (ignore: NullPointerException) {
//        }
//        onImageUploadResult(mediaPath!!)
        ///////////////
    }

    private fun dismissAllDialogs() {
        if (!isFinishing && !isDestroyed) {
            try {
                if (fetchChannelsFragment!!.dialog != null && fetchChannelsFragment!!.dialog!!
                        .isShowing && !fetchChannelsFragment!!.isRemoving
                ) {
                    fetchChannelsFragment!!.dismiss()
                } else if (addOrEditChannelsFragment!!.dialog != null && addOrEditChannelsFragment!!.dialog!!
                        .isShowing && !addOrEditChannelsFragment!!.isRemoving
                ) {
                    addOrEditChannelsFragment!!.dismiss()
                }
            } catch (ignore: Exception) {
            }
        }
    }

    override fun editChannel(
        channelId: String,
        channelName: String,
        ingestUrl: String,
        enabled: Boolean,
        channelType: Int
    ) {
        addOrEditChannelsFragment = AddOrEditChannelsFragment()
        dismissAllDialogs()
        addOrEditChannelsFragment!!.updateParams(
            false,
            channelId,
            channelName,
            ingestUrl,
            enabled,
            channelType
        )
        addOrEditChannelsFragment!!.show(supportFragmentManager, AddOrEditChannelsFragment.TAG)
    }

    override fun addChannel() {
        //Added due to fragment ui was not getting refreshed
        addOrEditChannelsFragment = AddOrEditChannelsFragment()
        dismissAllDialogs()
        addOrEditChannelsFragment!!.updateParams(true, null, null, null, false, -1)
        addOrEditChannelsFragment!!.show(supportFragmentManager, AddOrEditChannelsFragment.TAG)
    }

    override fun noRestreamChannelsFound() {
        hideProgressDialog()

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(getString(R.string.ism_add_restream_channels))
        alertDialog.setPositiveButton(getString(R.string.ism_add_update)) { dialogInterface: DialogInterface?, i: Int ->
            dismissAllDialogs()
            fetchChannelsFragment!!.updateParams(this@GoLiveActivity)
            fetchChannelsFragment!!.show(supportFragmentManager, FetchChannelsFragment.TAG)
        }
        alertDialog.setCancelable(true)
        if (!isFinishing && !isDestroyed) {
            alertDialog.show()
        }
    }

    override fun onProductsSelected(productIds: ArrayList<String>) {
        this.productIds.clear()
        this.productIds.addAll(productIds)
        val size = productIds.size
        if (productIds.size == 0) {
            ismActivityGoliveBinding!!.incLinkProducts.tvContent.text =
                getString(R.string.ism_link_products_description)
        } else {
            ismActivityGoliveBinding!!.incLinkProducts.tvContent.text = if (size > 1) getString(
                R.string.ism_products_linked,
                size
            ) else getString(R.string.ism_product_linked, size)
        }
    }

    fun onSingleMultiLiveSelected(){
            ismActivityGoliveBinding!!.tvDeviceStream.setTextColor(
                ContextCompat.getColor(
                    this@GoLiveActivity,
                    R.color.ism_filters_gray
                )
            )
            ismActivityGoliveBinding!!.tvMultiStream.setTextColor(
                ContextCompat.getColor(
                    this@GoLiveActivity,
                    R.color.ism_white
                )
            )

        ismActivityGoliveBinding!!.ivLiveFromDevice.visibility = View.GONE
        ismActivityGoliveBinding!!.ivMultiStream.visibility = View.VISIBLE
        rtmpIngest = false
        ismActivityGoliveBinding!!.swPersistRtmpIngestEndpoint.visibility = View.GONE
        ismActivityGoliveBinding!!.tvPersistRtmpIngestEndpoint.visibility = View.GONE
//        ismActivityGoliveBinding!!.incRtmpView.relRtmpView.visibility = View.GONE
        ismActivityGoliveBinding!!.normalCameraView.visibility = View.VISIBLE


//            ismActivityGoliveBinding!!.rlLowLatency.visibility = View.GONE
//            ismActivityGoliveBinding!!.rlAudioOnly.visibility = View.VISIBLE
//
//            ismActivityGoliveBinding!!.tvSelfHosted.visibility = View.VISIBLE
//            ismActivityGoliveBinding!!.swSelfHosted.visibility = View.VISIBLE
//            ismActivityGoliveBinding!!.rlRtmpIngest.visibility =
//                if (ismActivityGoliveBinding!!.swSelfHosted.isChecked) View.VISIBLE else View.GONE
    }

    fun onLiveFromDeviceSelected() {
        if (multiGuestLive) {
            ismActivityGoliveBinding!!.tvDeviceStream.setTextColor(
                ContextCompat.getColor(
                    this@GoLiveActivity,
                    R.color.ism_white
                )
            )
            ismActivityGoliveBinding!!.tvMultiStream.setTextColor(
                ContextCompat.getColor(
                    this@GoLiveActivity,
                    R.color.ism_filters_gray
                )
            )

            ismActivityGoliveBinding!!.ivLiveFromDevice.visibility = View.VISIBLE
            ismActivityGoliveBinding!!.ivMultiStream.visibility = View.GONE
            rtmpIngest = true
            ismActivityGoliveBinding!!.swPersistRtmpIngestEndpoint.visibility = View.VISIBLE
            ismActivityGoliveBinding!!.tvPersistRtmpIngestEndpoint.visibility = View.VISIBLE
//            ismActivityGoliveBinding!!.incRtmpView.relRtmpView.visibility = View.VISIBLE
            ismActivityGoliveBinding!!.normalCameraView.visibility = View.GONE


//            ismActivityGoliveBinding!!.rlLowLatency.visibility = View.VISIBLE
//            ismActivityGoliveBinding!!.rlAudioOnly.visibility = View.GONE
//
//            ismActivityGoliveBinding!!.tvSelfHosted.visibility = View.GONE
//            ismActivityGoliveBinding!!.swSelfHosted.visibility = View.GONE
//            ismActivityGoliveBinding!!.rlRtmpIngest.visibility = View.GONE
        }
    }

    /**
     * [MultiLiveSelectMembersContract.View.onBroadcastStarted]
     */
    override fun onBroadcastStarted(
        streamId: String?,
        streamDescription: String?,
        streamImageUrl: String?,
        memberIds: java.util.ArrayList<String?>,
        startTime: Long,
        userId: String?,
        ingestEndpoint: String?,
        streamKey: String?,
        hdBroadcast: Boolean,
        restream: Boolean,
        rtcToken: String?,
        restreamEndpoints: java.util.ArrayList<String?>?,
        productsLinked: Boolean,
        productsCount: Int,
        rtmpIngestUrl: String?
    ) {
        hideProgressDialog()
        memberIds.add(userId)
        val intent =
            Intent(this@GoLiveActivity, MultiliveStreamsActivity::class.java)


        intent.putExtra("streamId", streamId)
        intent.putExtra("streamDescription", streamDescription)
        intent.putExtra("streamImage", streamImageUrl)
        intent.putExtra("startTime", startTime)
        intent.putExtra("membersCount", memberIds.size)
        intent.putExtra("viewersCount", 0)
        intent.putExtra("publishersCount", 1)
        intent.putExtra("joinRequest", true)
        intent.putExtra("isAdmin", true)
        intent.putStringArrayListExtra("memberIds", memberIds)
        intent.putExtra("initiatorName", IsometrikStreamSdk.getInstance().userSession.userName)
        intent.putExtra("publishRequired", false)
        intent.putExtra("initiatorId", IsometrikStreamSdk.getInstance().userSession.userId)

        intent.putExtra(
            "initiatorIdentifier",
            IsometrikStreamSdk.getInstance().userSession.userIdentifier
        )
        intent.putExtra(
            "initiatorImage",
            IsometrikStreamSdk.getInstance().userSession.userProfilePic
        )
        intent.putExtra("isPublic", isPublic)
        intent.putExtra("audioOnly", ismActivityGoliveBinding!!.swAudioOnly.isChecked)
        intent.putExtra("isBroadcaster", true)
        intent.putExtra("ingestEndpoint", ingestEndpoint)
        intent.putExtra("streamKey", streamKey)
        intent.putExtra("multiLive", true)
        intent.putExtra("hdBroadcast", hdBroadcast)
        intent.putExtra("rtcToken", rtcToken)
        intent.putExtra("restream", restream)
        intent.putStringArrayListExtra("restreamEndpoints", restreamEndpoints)
        intent.putExtra("productsLinked", productsLinked)
        intent.putExtra("productsCount", productsCount)
        intent.putExtra("isModerator", true)
        intent.putExtra("selfHosted", getIntent().getBooleanExtra("selfHosted", false))
        intent.putExtra("rtmpIngest", getIntent().getBooleanExtra("rtmpIngest", false))
        intent.putExtra(
            "persistRtmpIngestEndpoint",
            getIntent().getBooleanExtra("persistRtmpIngestEndpoint", false)
        )
        intent.putExtra("rtmpIngestUrl", rtmpIngestUrl)
        startActivity(intent)
        finish()
    }


}
