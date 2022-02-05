package com.imix.assessmentapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.imix.assessmentapp.adapter.MoviesAdapter
import com.imix.assessmentapp.api.Coroutines
import com.imix.assessmentapp.api.RetrofitClient
import com.imix.assessmentapp.base.BaseActivityWithoutVM
import com.imix.assessmentapp.databinding.ActivityMainBinding
import com.imix.assessmentapp.model.get_all_photos.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : BaseActivityWithoutVM<ActivityMainBinding>() {

    private val show by lazy {
        MoviesAdapter()
    }


    private var dataList = mutableListOf<Data>()


    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListner()

    }

    private fun init() {


        mViewBinding.rvShow.adapter = show
        tvShowsFun()

        show.listener ={ view, item, position ->

            val intent = Intent(this,DetailsActivity::class.java)
            intent.putExtra("id",item.id)
            startActivity(intent)

        }

    }

    private fun setListner() {

    }

    fun tvShowsFun() {

        mViewBinding.progressBar.visibility = View.VISIBLE
//            showProgressDialog()
        if (isNetworkConnected()) {
            Coroutines.io {
                val tvShows =
                    RetrofitClient.instance!!.webServices.getAllPhotos(
                    )
                dataList.clear()

                withContext(Dispatchers.Main) {
//                        hideProgressDialog()
                    mViewBinding.progressBar.visibility = View.GONE

                    if (tvShows.isSuccessful) {
                        tvShows.body()!!.apply {
                            when (status) {
                                1 -> {
                                    for (item in data.indices) {
                                        dataList.add(data[item])
                                    }

                                    show.addItems(dataList)

                                    showToast(message)
                                }
                                0 -> {
                                    showSnackBar(message)
                                }
                                else -> {
                                    showSnackBar(tvShows.message())
                                }
                            }
                        }
                    } else {
                        showSnackBar(tvShows.message())

                    }
                }
            }
        } else {
//                hideProgressDialog()
            mViewBinding.progressBar.visibility = View.GONE
            showSnackBar("Internet Not Connected!")
        }


    }

}