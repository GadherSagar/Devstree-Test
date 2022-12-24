package com.test.devstree_test.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.test.devstree_test.R
import com.test.devstree_test.databinding.FragmentLocationListBinding
import com.test.devstree_test.ui.activity.MainActivity
import com.test.devstree_test.ui.adapter.LocationAdapter
import com.test.devstree_test.ui.dialog.DeleteDialog
import com.test.devstree_test.ui.dialog.SortDialog
import com.test.devstree_test.ui.util.Constant.DELETE_VIEW
import com.test.devstree_test.ui.util.Constant.EDIT_VIEW
import com.test.devstree_test.ui.viewmodel.LocationViewModel

class LocationListFragment : Fragment() {
    private lateinit var _activity: MainActivity
    private var _binding: FragmentLocationListBinding? = null
    private val binding get() = _binding!!
    private val locationVM by activityViewModels<LocationViewModel>()

    private lateinit var adapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location_list, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LocationAdapter { message, location ->
            when (message) {
                EDIT_VIEW -> {
                    _activity.moveToFindLocationScreen(locationId = location.id.toString())
                }
                DELETE_VIEW -> {
                    if (location.isPrimary) {
                        Toast.makeText(
                            _activity,
                            getString(R.string.msg_primary_delete),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        DeleteDialog(_activity) {
                            locationVM.deleteLocation(location)
                        }.showDialog()
                    }
                }
            }
        }
        binding.rvLocations.apply {
            setHasFixedSize(true)
            adapter = this@LocationListFragment.adapter
        }
        locationVM.location.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.rvLocations.visibility = View.GONE
                binding.btnAddPoiBottom.visibility = View.GONE
                binding.fabDirection.visibility = View.GONE
                binding.llNoData.visibility = View.VISIBLE
            } else if (it.size == 1) {
                binding.llNoData.visibility = View.GONE
                binding.rvLocations.visibility = View.VISIBLE
                binding.btnAddPoiBottom.visibility = View.VISIBLE
                binding.fabDirection.visibility = View.GONE
            } else {
                binding.llNoData.visibility = View.GONE
                binding.rvLocations.visibility = View.VISIBLE
                binding.btnAddPoiBottom.visibility = View.VISIBLE
                binding.fabDirection.visibility = View.VISIBLE
            }
            adapter.setData(ArrayList(it))
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_sort, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort -> {
                        SortDialog(_activity, locationVM.sortType) {
                            locationVM.sortType = it
                            locationVM.sortLocation()
                        }.showDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addPoi(isPrimary: Boolean?) {
        _activity.moveToFindLocationScreen(isPrimary)
    }

    fun openDirection() {
        _activity.moveToDirectionScreen()
    }
}