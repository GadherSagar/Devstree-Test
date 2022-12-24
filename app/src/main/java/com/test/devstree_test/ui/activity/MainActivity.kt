package com.test.devstree_test.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.test.devstree_test.R
import com.test.devstree_test.databinding.ActivityMainBinding
import com.test.devstree_test.ui.fragment.LocationDirectionFragment
import com.test.devstree_test.ui.fragment.LocationFindFragment
import com.test.devstree_test.ui.fragment.LocationListFragment
import com.test.devstree_test.ui.util.ExtensionFunction.addFragment
import com.test.devstree_test.ui.util.ExtensionFunction.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        addFragment(R.id.container, LocationListFragment(), false)
    }

    fun moveToFindLocationScreen(isPrimary: Boolean? = null,locationId: String? = null) {
        replaceFragment(R.id.container, LocationFindFragment.newInstance(isPrimary = isPrimary , locationId = locationId), true)
    }
    fun moveToDirectionScreen() {
        replaceFragment(R.id.container, LocationDirectionFragment(), true)
    }

}