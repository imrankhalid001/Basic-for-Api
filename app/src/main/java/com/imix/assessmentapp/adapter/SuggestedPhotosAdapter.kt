package com.imix.assessmentapp.adapter

import android.util.Log
import com.imix.assessmentapp.R
import com.imix.assessmentapp.databinding.IvDetailsBinding
import com.imix.assessmentapp.model.get_suggested_photos.Data
import com.imix.assessmentapp.utils.Utils
import com.imix.assessmentapp.base.BaseRecyclerViewAdapter

class SuggestedPhotosAdapter : BaseRecyclerViewAdapter<Data, IvDetailsBinding>() {
    override fun getLayout() = R.layout.iv_details

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<IvDetailsBinding>,
        position: Int
    ) {
        holder.binding.apply {
            suggestedPhotos = items[position]

            root.setOnClickListener {
                try {
                    listener?.invoke(it, items[position], position)

                } catch (e: IndexOutOfBoundsException) {
                    print(e.localizedMessage)
                }

                tvName.isSelected = true  // Set focus to the textview
            }

        }

        try {
            Utils.setImageWithCoil(items[position].cover_image, holder.binding.imgMovies)
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.d(TAG, "image not found ")
            // handler
        }

    }

}