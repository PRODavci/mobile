import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mireascanner.R
import com.mireascanner.common.permissions.PermissionsSharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsPermissionHelper(private val context: Context) {

    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val permissionsSharedPreferencesManager = PermissionsSharedPreferencesManager()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkAndRequestPermission(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                val isRationaleShowLastDialog =
                    permissionsSharedPreferencesManager.getLocationPermissionFlag(context)
                withContext(Dispatchers.Main) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        )
                    ) {
                        showPermissionDialog(activity)
                    } else if (isRationaleShowLastDialog) {
                        showLastPermissionDialog()
                    } else {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                            PERMISSION_REQUEST_CODE
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionDialog(activity: Activity) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.allow_notifications))
            .setMessage(context.getString(R.string.allow_notifications_desc))
            .setPositiveButton(context.getString(R.string.allow)) { _, _ ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton(context.getString(R.string.deny)) { dialog, _ ->
                coroutineScope.launch(Dispatchers.IO) {
                    permissionsSharedPreferencesManager.putLocationPermissionFlag(context, true)
                }
                dialog.dismiss()
            }
            .create()
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showLastPermissionDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.allow_notifications))
            .setMessage(context.getString(R.string.allow_notifications_desc_last))
            .setPositiveButton(context.getString(R.string.open)) { _, _ ->
                (context as Activity).startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + context.packageName)
                    )
                )
            }
            .setNegativeButton(context.getString(R.string.deny)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun onCleared() {
        coroutineScope.cancel()
    }


    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
}