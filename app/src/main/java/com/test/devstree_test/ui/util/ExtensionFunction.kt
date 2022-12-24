package com.test.devstree_test.ui.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng

object ExtensionFunction {
    inline fun FragmentManager.doTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commitAllowingStateLoss()
    }

    fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment, isStacked: Boolean) {
        supportFragmentManager.doTransaction {
            if (isStacked) {
                add(
                    frameId, fragment, fragment::class.java.name
                ).addToBackStack(fragment::class.java.name)
            } else {
                add(frameId, fragment, fragment::class.java.name)
            }
        }
    }


    fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment, isStacked: Boolean) {
        supportFragmentManager.doTransaction {
            if (isStacked) {
                replace(
                    frameId, fragment, fragment::class.java.name
                ).addToBackStack(fragment::class.java.name)
            } else {
                replace(frameId, fragment, fragment::class.java.name)
            }
        }
    }

    inline fun <T> RecyclerView.Adapter<*>.autoNotify(
        oldList: List<T>,
        newList: List<T>,
        crossinline compare: (T, T) -> Boolean,
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(oldList[oldItemPosition], newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
        })
        diff.dispatchUpdatesTo(this)
    }

    fun LatLng.distance(destination: LatLng): Double {
        val lat1 = this.latitude
        val lon1 = this.longitude
        val lat2 = destination.latitude
        val lon2 = destination.longitude
        val theta = lon1 - lon2
        var dist =
            (Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + (Math.cos(deg2rad(lat1)) * Math.cos(
                deg2rad(lat2)
            ) * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}