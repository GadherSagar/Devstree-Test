package com.test.devstree_test.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.test.devstree_test.BuildConfig.MAPS_API_KEY
import com.test.devstree_test.R
import com.test.devstree_test.database.entity.LocationEntity
import com.test.devstree_test.databinding.FragmentLocationFindBinding
import com.test.devstree_test.ui.activity.MainActivity
import com.test.devstree_test.ui.viewmodel.LocationViewModel


class LocationFindFragment : Fragment() {
    private lateinit var _activity: MainActivity
    private var _binding: FragmentLocationFindBinding? = null
    private val binding get() = _binding!!
    private val locationVM by activityViewModels<LocationViewModel>()

    lateinit var supportMapFragment: SupportMapFragment
    lateinit var autocompleteFragment: AutocompleteSupportFragment
    private val defaultLatLng = LatLng(20.5937, 78.9629)

    private var locationId: String? = null
    private var isPrimary: Boolean = false
    private val IS_PRIMARY = "IS_PRIMARY"
    private val LOCATION_ID = "LOCATION_ID"
    private var googleMap: GoogleMap? = null
    private var place: Place? = null

    companion object {
        fun newInstance(isPrimary: Boolean? = null, locationId: String? = null) =
            LocationFindFragment().apply {
                arguments = bundleOf(IS_PRIMARY to isPrimary, LOCATION_ID to locationId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activity = activity as MainActivity
        arguments?.let {
            isPrimary = it.getBoolean(IS_PRIMARY, false)
            locationId = it.getString(LOCATION_ID)
        }
        Places.initialize(_activity, MAPS_API_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location_find, container, false)
        binding.fragment = this
        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS
            )
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                if (place.latLng != null && googleMap != null) {
                    this@LocationFindFragment.place = place
                    setMarker(place.latLng!!, googleMap!!)
                    isLocationChange()
                }
            }

            override fun onError(status: Status) {
                Log.i("MAP_ERROR", "An error occurred: $status")
            }
        })
        supportMapFragment.getMapAsync { googleMap ->
            // When map is loaded
            this.googleMap = googleMap
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 5f))
            locationId?.let {
                locationVM.getLocationById(it)?.apply {
                    setMarker(LatLng(latitude, longitude), googleMap)
                }
            }
        }
    }

    private fun setMarker(latLng: LatLng, googleMap: GoogleMap) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        googleMap.clear()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f))
        googleMap.addMarker(markerOptions)

    }

    private fun isLocationChange() {
        binding.mcvSave.visibility = View.VISIBLE
        if (locationId != null) {
            binding.tvSaveMsg.text = getString(R.string.lbl_do_you_want_to_save)
            binding.btnSave.text = getString(R.string.lbl_update)
        }
    }

    fun save() {
        place?.let {
            val locationEntity = LocationEntity(
                it.name,
                it.address,
                it.latLng.latitude,
                it.latLng.longitude,
                isPrimary,
                locationId?.toIntOrNull()
            )
            locationId?.let {
                locationVM.updateLocation(locationEntity)
            } ?: run {
                locationVM.insetLocation(locationEntity)
            }
            _activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}