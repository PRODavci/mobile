import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.mireascanner.MainActivity
import com.mireascanner.common.permissions.PermissionsSharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NotificationsPermissionHelper(private val context: Context) {

    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val permissionsSharedPreferencesManager = PermissionsSharedPreferencesManager()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkAndRequestPermission(activity: MainActivity) {
        Log.d("Permtag", "here")
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                val isRationaleShowLastDialog =
                    permissionsSharedPreferencesManager.getLocationPermissionFlag(context)
                if (isRationaleShowLastDialog) {
                    showLastPermissionDialog()
                } else {
                    showPermissionDialog(activity)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionDialog(activity: MainActivity) {
        AlertDialog.Builder(context)
            .setTitle("Notification Permission")
            .setMessage("This app requires permission to send notifications.")
            .setPositiveButton("Allow") { _, _ ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton("Deny") { dialog, _ ->
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
        AlertDialog.Builder(context)
            .setTitle("Notification Permission")
            .setMessage("This app requires permission to send notifications. Open Settings?")
            .setPositiveButton("Allow") { _, _ ->
                (context as Activity).startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + context.packageName)
                    )
                )
            }
            .setNegativeButton("Deny") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }


    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
}