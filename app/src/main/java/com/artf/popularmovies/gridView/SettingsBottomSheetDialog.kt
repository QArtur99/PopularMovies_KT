package com.artf.popularmovies.gridView

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import com.artf.popularmovies.R
import com.artf.popularmovies.databinding.DialogSettingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class SettingsBottomSheetDialog(activity: Activity) : BottomSheetDialog(activity){

    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: DialogSettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogSettingsBinding.inflate(LayoutInflater.from(context))
        binding.clickListener = this
        setContentView(binding.root)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sortBy = sharedPreferences.getString(
            context.getString(R.string.pref_sort_by_key),
            context.getString(R.string.pref_sort_by_most_popular)
        )
        setSelected(sortBy!!)
    }

    private fun setSelected(sortBy: String) {
        when (sortBy) {
            context.getString(R.string.pref_sort_by_most_popular) -> binding.mostPopular.isSelected = true
            context.getString(R.string.pref_sort_by_highest_rated) -> binding.highestRated.isSelected = true
            context.getString(R.string.pref_sort_by_favorite) -> binding.favorite.isSelected = true
        }
    }

    fun onClick(view: View) {
        selectorOff()
        view.isSelected = true
        when (view.id) {
            R.id.favorite -> editPref(R.string.pref_sort_by_key, R.string.pref_sort_by_favorite)
            R.id.mostPopular -> editPref(R.string.pref_sort_by_key, R.string.pref_sort_by_most_popular)
            R.id.highestRated -> editPref(R.string.pref_sort_by_key, R.string.pref_sort_by_highest_rated)
        }
        dismiss()
    }

    private fun editPref(keyId: Int, valueId: Int) {
        val editPref = sharedPreferences.edit()
        editPref.putString(context.getString(keyId), context.getString(valueId))
        editPref.apply()
    }

    private fun selectorOff() {
        binding.favorite.isSelected = false
        binding.mostPopular.isSelected = false
        binding.highestRated.isSelected = false
    }

}
