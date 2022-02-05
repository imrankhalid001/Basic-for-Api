package com.imix.assessmentapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import com.imix.assessmentapp.adapter.SuggestedPhotosAdapter
import com.imix.assessmentapp.api.Coroutines
import com.imix.assessmentapp.api.RetrofitClient
import com.imix.assessmentapp.base.BaseActivityWithoutVM
import com.imix.assessmentapp.databinding.ActivityDetailsBinding
import com.imix.assessmentapp.model.get_suggested_photos.Data
import com.imix.assessmentapp.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailsActivity : BaseActivityWithoutVM<ActivityDetailsBinding>() {

    private val show by lazy {
        SuggestedPhotosAdapter()
    }


    private var dataList = mutableListOf<Data>()

    override fun getViewBinding(): ActivityDetailsBinding =
        ActivityDetailsBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        setListner()

    }

    private fun init() {


        val id = intent.getStringExtra("id")

        Log.d(TAG, "$id")
        tvShowsFun(id)

        mViewBinding.rvSuggested.adapter = show
        tvShowsSuggestedPhotos(id)

        mViewBinding.apply {

            tvGenresData.isSelected = true


        }


    }


    private fun tvShowsFun(id: String?) {

        mViewBinding.progressBar.visibility = View.VISIBLE
        if (isNetworkConnected()) {
            Coroutines.io {
                val tvShows =
                    RetrofitClient.instance!!.webServices.getSinglePhotoDetails(
                        id
                    )
                dataList.clear()

                withContext(Dispatchers.Main) {
                    mViewBinding.progressBar.visibility = View.GONE

                    if (tvShows.isSuccessful) {
                        tvShows.body()!!.apply {
                            when (status) {
                                1 -> {
                                    mViewBinding.apply {
                                        data.apply {
                                            Utils.setImageWithCoil(cover_image, imgShows)
                                            tvTitle.text = title
                                            tvRating.text = rating
                                            tvGenresData.text = genres
                                            tvLanguageData.text = language
                                            tvYearData.text = year
                                            tvStorylineData.text = summary

                                        }

                                    }

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
            mViewBinding.progressBar.visibility = View.GONE
            showSnackBar("Internet Not Connected!")
        }


    }

    private fun tvShowsSuggestedPhotos(id: String?) {
        mViewBinding.progressBar.visibility = View.VISIBLE
//            showProgressDialog()
        if (isNetworkConnected()) {
            Coroutines.io {
                val tvShows =
                    RetrofitClient.instance!!.webServices.getSuggestedPhotos(
                        id
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


    private fun setListner() {

        mViewBinding.apply {
            imgBack.setOnClickListener {
                finish()
            }
        }


    }

    companion object {
        const val TAG = "DetailsActivity--"
    }
}