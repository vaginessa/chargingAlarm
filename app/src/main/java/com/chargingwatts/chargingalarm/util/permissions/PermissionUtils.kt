package com.chargingwatts.chargingalarm.util.permissions

import AppConstants
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

/**
 * Created by Abhishek Luthra on 4/8/2018.
 * chargingwatts.in
 * er.abhishek.luthra@gmail.com
 */

class PermissionUtils @Inject constructor(private val mActivity: FragmentActivity, private val mFragment: Fragment? = null) {


    fun verifyPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                            mActivity,
                            permission
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    //--------------------------ASK PERMISSSIONS ON ACTIVITY-------------------------------------------//
    /*
    use this method to ask for permissions while you are on an mActivity so that you get callback
    on onPermissionsResult of the mActivity
    */
    fun askPermissions(requestCode: Int,
                       showRationale: Boolean,
                       permissionDialogParams: PermissionDialogParams,
                       vararg permissions: String
    ) {
        if (useRuntimePermissions()) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                                mActivity,
                                permission
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    mActivity,
                                    permission) && showRationale
                    ) {
                        defaultShowRationale(permission, requestCode, permissionDialogParams)
                    } else {
                        if (mFragment != null) {
                            mFragment.requestPermissions(arrayOf(permission), requestCode)
                        } else {
                            ActivityCompat.requestPermissions(
                                    mActivity, arrayOf(permission), requestCode
                            )
                        }
                    }
                }
            }
        }
    }

    //--------------------show dialog explaining why a particular permission is necessary----------//

    fun defaultShowRationale(permission: String,
                             requestCode: Int,
                             permissionDialogParams: PermissionDialogParams) {
        val alertBuilder = AlertDialog.Builder(mActivity)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(permissionDialogParams.title)
        alertBuilder.setMessage(permissionDialogParams.message)
        alertBuilder.setPositiveButton(
                permissionDialogParams.positiveBtnText,
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (mFragment != null) {
                            mFragment.requestPermissions(arrayOf(permission), requestCode)
                        } else {
                            ActivityCompat.requestPermissions(
                                    mActivity, arrayOf(permission), requestCode
                            )
                        }
                    }
                })
//        alertBuilder.setNegativeButton(
//            permissionDialogParams.negativeBtnText,
//            object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//
//                }
//            })
        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    //-------------------GO TO APPLICATION SETTINGS-----------------------------------------------//
    fun openApplicationSettings() {
        val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        val uri: Uri = Uri.fromParts("package", mActivity.packageName, null);
        intent.setData(uri)
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, AppConstants.START_SETTINGS_ACTIVITY_REQUEST_CODE)
        } else {
            mActivity.startActivityForResult(intent, AppConstants.START_SETTINGS_ACTIVITY_REQUEST_CODE)
        }
    }

    //------------------------CHECK IF RUNTIME PERMISSIONS ARE NEEDED-----------------------------//
    fun useRuntimePermissions(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
    }
}

data class PermissionDialogParams(
        var title: String = "Permission Necessary",
        var message: String = "To use this feature , you need to allow Barm to the specified Permission",
        var positiveBtnText: String = "Okay"
//    var negativeBtnText: String = "Deny"
)

