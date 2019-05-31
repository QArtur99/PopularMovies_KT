package com.artf.popularmovies

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.artf.popularmovies.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.tool_bar.view.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        if (binding.root.findViewById<FrameLayout>(R.id.detailsViewFrame) == null) {
            setSupportActionBar(binding.root.toolbar)
        } else {
            supportActionBar?.setBackgroundDrawable(
                ContextCompat.getDrawable(this, R.drawable.gradient_tool_bar)
            )
        }
    }
}
