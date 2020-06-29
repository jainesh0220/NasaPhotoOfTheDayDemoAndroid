package com.example.nasaphotooftheday

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nasaphotooftheday.apimanager.ApiResponseCallback
import com.example.nasaphotooftheday.apimanager.RestApiManager
import com.example.nasaphotooftheday.apimanager.WebWareHouse
import com.example.nasaphotooftheday.defs.MediaTypeDef.Companion.IMAGE
import com.example.nasaphotooftheday.defs.MediaTypeDef.Companion.VIDEO
import com.example.nasaphotooftheday.models.PhotoResponse
import com.example.nasaphotooftheday.ui.ExoPlayerActivity
import com.example.nasaphotooftheday.utils.Logging
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    var mContext: Context? = null
    var toolBar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar = findViewById(R.id.toolbarPhotoView)
        setSupportActionBar(toolBar)
        mContext = this
        getPhotosApi(false, "")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_calender -> {
                val cal = Calendar.getInstance()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        layoutLoading.visibility = View.VISIBLE
                        getPhotosApi(true, "$year-$monthOfYear-$dayOfMonth")
                    }
                DatePickerDialog(
                    this@MainActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getPhotosApi(isForDate: Boolean, date: String) {
        if (isForDate && date.isNotEmpty()) {
            RestApiManager.getInstance(this)
                .callDatedPhotoApi(WebWareHouse.API_KEY, date, object : ApiResponseCallback {
                    override fun onSuccess(obj: Any) {
                        val photosResponse = obj as PhotoResponse
                        setParams(photosResponse)
                    }

                    override fun onFailure(message: String) {
                        showCrashDialog(isForDate, date)
                        Logging.instance?.e(TAG, "onFailure = $message")
                    }

                    override fun onError(message: String) {
                        showCrashDialog(isForDate, date)
                        Logging.instance?.e(TAG, "onError = $message")
                    }

                })
        } else {
            RestApiManager.getInstance(this)
                .callPhotoApi(WebWareHouse.API_KEY, object : ApiResponseCallback {
                    override fun onSuccess(obj: Any) {
                        val photosResponse = obj as PhotoResponse
                        setParams(photosResponse)
                    }

                    override fun onFailure(message: String) {
                        showCrashDialog(isForDate, date)
                        Logging.instance?.e(TAG, "onFailure = $message")
                    }

                    override fun onError(message: String) {
                        showCrashDialog(isForDate, date)
                        Logging.instance?.e(TAG, "onError = $message")
                    }

                })
        }
    }

    private fun setParams(photosResponse: PhotoResponse) {
        textViewDescription.text = photosResponse.explanation
        val toolBarTextView: TextView = toolBar!!.findViewById(R.id.toolbarTextViewTitle)
        toolBarTextView.text = photosResponse.title
        layoutLoading.visibility = View.GONE
        when (photosResponse.mediaType) {
            IMAGE -> {
                Glide
                    .with(mContext!!)
                    .load(photosResponse.hdurl)
                    .fitCenter()
                    .placeholder(R.drawable.ic_spaceship)
                    .into(imageViewPhotoOfTheDay)
            }
            VIDEO -> {
                val requestOptions = RequestOptions()
                requestOptions.isMemoryCacheable
                Glide
                    .with(mContext!!)
                    .setDefaultRequestOptions(requestOptions)
                    .load(photosResponse.hdurl)
                    .fitCenter()
                    .placeholder(R.drawable.ic_spaceship)
                    .into(imageViewPhotoOfTheDay)
                exoPlayerPlay.visibility = View.VISIBLE
                exoPlayerPlay.setOnClickListener {
                    val mIntent = Intent(this@MainActivity, ExoPlayerActivity::class.java)
                    mIntent.putExtra(getString(R.string.str_video_intent), photosResponse.hdurl)
                    startActivity(mIntent)
                }
            }
        }
    }

    private fun showCrashDialog(isForDate: Boolean, date: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_crash)
        val closeButton: Button = dialog.findViewById(R.id.buttonClose) as Button
        closeButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        val retryButton: Button = dialog.findViewById(R.id.buttonRetry) as Button
        retryButton.setOnClickListener {
            layoutLoading.visibility = View.VISIBLE
            getPhotosApi(isForDate, date)
            dialog.dismiss()
        }
        dialog.show()
    }

}
