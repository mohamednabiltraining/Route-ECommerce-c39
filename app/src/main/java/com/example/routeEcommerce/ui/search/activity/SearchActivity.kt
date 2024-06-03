package com.example.routeEcommerce.ui.search.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.routeEcommerce.R
import com.example.routeEcommerce.ui.search.fragment.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }
}
