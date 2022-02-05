package com.imix.assessmentapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.imix.assessmentapp.activities.MyApp.Companion.context
import com.imix.assessmentapp.R

object Utils {


    fun changeStatusBarColor(statusBarColor: Int, myActivityReference: Activity) {
        val window = myActivityReference.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = statusBarColor
    }


    fun goToNextScreenWithFinish(cls: Class<*>?, ct: Context) {
        val intent = Intent(ct, cls)
        ct.startActivity(intent)
        (ct as Activity).finish()
    }


    fun goToNextScreenWithoutFinish(cls: Class<*>?, ct: Context) {
        val intent = Intent(ct, cls)
        ct.startActivity(intent)
    }


    fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    fun notInternetDialog(c: Context?): AlertDialog.Builder {
        val builder: AlertDialog.Builder = AlertDialog.Builder(c!!)
        builder.setTitle("No Internet Connection")
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit")
        builder.setPositiveButton(
            "Ok"
        ) { dialog, _ -> dialog.cancel() }
        return builder
    }


    fun navigateAndClearBackStack(cls: Class<*>?, ct: Context) {
        val intent = Intent(ct, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        ct.startActivity(intent)
        (ct as Activity).finish()
    }

    fun showSnackBar(view: View, msg: String?) {
        //findViewById(android.R.id.content)
        Snackbar.make(view, msg!!, Snackbar.LENGTH_SHORT)
            .show()
    }


    fun setVisibility(visible: View, gone: View) {
        visible.visibility = View.VISIBLE
        gone.visibility = View.GONE
    }

    fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun shareApp(ct: Context?) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                ct!!.resources.getString(R.string.app_name)
            )
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                """
                  ${shareMessage}https://play.google.com/store/apps/details?id=${context.packageName}
                  
                  """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            ct.startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("DEPRECATION")
    fun getSpannedText(text: String): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            Html.fromHtml(text);
        }
    }

    fun showMaterialDialog(context: Context, title: String?, message: String?) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(context.resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()
    }


    fun showShortToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun setImageWithCoil(
        imgUrl: String?,
        ivImage: ShapeableImageView,
    ) {
        ivImage!!.load(imgUrl) {
            target(
                onStart = { placeholder ->
                    // Handle the placeholder drawable.
                },
                onSuccess = { result ->
                    ivImage.load(result)
                    // Handle the successful result.
                },
                onError = { error ->
                    ivImage.load(R.drawable.ic_place_holder)

                    // Handle the error drawable.
                }
            )

        }
    }


}