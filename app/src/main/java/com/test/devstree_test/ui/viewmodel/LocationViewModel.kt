package com.test.devstree_test.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.test.devstree_test.database.entity.LocationEntity
import com.test.devstree_test.network.dto.response.directions.Directions
import com.test.devstree_test.ui.repository.LocationRepository
import com.test.devstree_test.ui.util.Constant.SORT_ASCENDING
import com.test.devstree_test.ui.util.ExtensionFunction.distance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val locationRepo = LocationRepository()
    var sortType = SORT_ASCENDING

    private val _location = MutableStateFlow<List<LocationEntity>>(emptyList())
    val location: LiveData<List<LocationEntity>> = _location.asLiveData()
    private val _directions: MutableLiveData<Directions> = MutableLiveData()
    val directions: LiveData<Directions> = _directions

    init {
        getLocation()
    }

    fun insetLocation(locationEntity: LocationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepo.insertLocation(locationEntity)
        }
    }

    fun updateLocation(locationEntity: LocationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepo.updateLocation(locationEntity)
        }
    }

    fun deleteLocation(locationEntity: LocationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepo.deleteLocation(locationEntity)
        }
    }

    fun getDirection() {
        viewModelScope.launch(Dispatchers.IO) {
            val origin = _location.value.find { it.isPrimary }
            origin?.let {
                val originLatLng = LatLng(origin.latitude, origin.longitude)
                val destination = location.value?.maxBy {
                    originLatLng.distance(
                        LatLng(
                            it.latitude, it.longitude
                        )
                    )
                }
                destination?.let {
                    val waypoints = _location.value.filterNot { it.id == origin.id }
                        .filterNot { it.id == destination.id }.sortedBy {
                        originLatLng.distance(
                            LatLng(it.latitude, it.longitude)
                        )
                    }
                    locationRepo.getDirection(origin, destination, *waypoints.toTypedArray())
                        ?.apply {
                            _directions.postValue(this)
                        }
                }
            }
        }
    }

    private fun getLocation() {
        locationRepo.getLocations().onEach {
            _location.value = it
        }.launchIn(viewModelScope)
    }

    fun sortLocation() {
        if (_location.value.isNotEmpty()) {
            val origin = _location.value.find { it.isPrimary }
            origin?.let {
                val originLatLng = LatLng(origin.latitude, origin.longitude)
                _location.value = if (sortType == SORT_ASCENDING) {
                    _location.value.sortedBy {
                        originLatLng.distance(
                            LatLng(
                                it.latitude, it.longitude
                            )
                        )
                    }
                } else {
                    _location.value.sortedByDescending {
                        originLatLng.distance(
                            LatLng(
                                it.latitude, it.longitude
                            )
                        )
                    }
                }
            }
        }
    }

    fun getLocationById(id: String): LocationEntity? {
        return location.value?.find { it.id.toString() == id }
    }
}