package com.qartf.popularmovies.gridView

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.DialogSettingsBinding
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_FAVORITE
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_KEY
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_POPULARITY
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_VOTE_AVERAGE

class SettingsBottomSheetDialog(activity: Activity) : BottomSheetDialog(activity) {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: DialogSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogSettingsBinding.inflate(LayoutInflater.from(context))
        binding.clickListener = this
        setContentView(binding.root)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sortBy = sharedPreferences.getString(SORT_BY_KEY, SORT_BY_POPULARITY)
        setSelected(sortBy!!)
    }

    private fun setSelected(sortBy: String) {
        when (sortBy) {
            SORT_BY_POPULARITY -> binding.mostPopular.isSelected = true
            SORT_BY_VOTE_AVERAGE -> binding.highestRated.isSelected = true
            SORT_BY_FAVORITE -> binding.favorite.isSelected = true
        }
    }

    fun onClick(view: View) {
        selectorOff()
        view.isSelected = true
        when (view.id) {
            R.id.favorite -> editPref(SORT_BY_KEY, SORT_BY_FAVORITE)
            R.id.mostPopular -> editPref(SORT_BY_KEY, SORT_BY_POPULARITY)
            R.id.highestRated -> editPref(SORT_BY_KEY, SORT_BY_VOTE_AVERAGE)
        }
        dismiss()
    }

    private fun editPref(keyId: String, value: String) {
        sharedPreferences.edit().putString(keyId, value).apply()
    }

    private fun selectorOff() {
        binding.favorite.isSelected = false
        binding.mostPopular.isSelected = false
        binding.highestRated.isSelected = false
    }
}
