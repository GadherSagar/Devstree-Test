package com.test.devstree_test.ui.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.test.devstree_test.R
import com.test.devstree_test.databinding.FragmentLocationDirectionBinding
import com.test.devstree_test.network.dto.response.Route
import com.test.devstree_test.ui.activity.MainActivity
import com.test.devstree_test.ui.viewmodel.LocationViewModel

class LocationDirectionFragment : Fragment() {
    private lateinit var _activity: MainActivity
    private var _binding: FragmentLocationDirectionBinding? = null
    private val binding get() = _binding!!
    private val locationVM by activityViewModels<LocationViewModel>()

    lateinit var supportMapFragment: SupportMapFragment

    private var googleMap: GoogleMap? = null
    private var mRouteMarkerList = ArrayList<Marker>()
    private lateinit var mRoutePolyline: Polyline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_location_direction, container, false
        )
        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        locationVM.getDirection()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportMapFragment.getMapAsync { googleMap ->
            // When map is loaded
            this@LocationDirectionFragment.googleMap = googleMap
            locationVM.directions.observe(viewLifecycleOwner) { directions ->
                if (directions.status == "OK") {
                    val legs = directions.routes[0].legs[0]
                    val route = Route(
                        "",
                        "",
                        legs.startLocation.lat,
                        legs.startLocation.lng,
                        legs.endLocation.lat,
                        legs.endLocation.lng,
                        directions.routes[0].overviewPolyline.points
                    )
                    setMarkersAndRoute(route)
                } else {
                    Toast.makeText(_activity, directions.status, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun setMarkersAndRoute(route: Route) {
        googleMap?.let {
            val startLatLng = LatLng(route.startLat!!, route.startLng!!)
            val startMarkerOptions: MarkerOptions =
                MarkerOptions().position(startLatLng).title(route.startName).icon(
                    BitmapDescriptorFactory.fromBitmap(drawMarker(_activity, "S"))
                )
            val endLatLng = LatLng(route.endLat!!, route.endLng!!)
            val endMarkerOptions: MarkerOptions =
                MarkerOptions().position(endLatLng).title(route.endName)
                    .icon(BitmapDescriptorFactory.fromBitmap(drawMarker(_activity, "E")))
            val startMarker = it.addMarker(startMarkerOptions)
            val endMarker = it.addMarker(endMarkerOptions)
            mRouteMarkerList.add(startMarker!!)
            mRouteMarkerList.add(endMarker!!)

            val polylineOptions = drawRoute(_activity)
            val pointsList = PolyUtil.decode(route.overviewPolyline)
            for (point in pointsList) {
                polylineOptions.add(point)
            }

            mRoutePolyline = it.addPolyline(polylineOptions)

            it.animateCamera(autoZoomLevel(mRouteMarkerList))
        }
    }
    private fun autoZoomLevel(markerList: List<Marker>): CameraUpdate {
        if (markerList.size == 1) {
            val latitude = markerList[0].position.latitude
            val longitude = markerList[0].position.longitude
            val latLng = LatLng(latitude, longitude)

            return CameraUpdateFactory.newLatLngZoom(latLng, 13f)
        } else {
            val builder = LatLngBounds.Builder()
            for (marker in markerList) {
                builder.include(marker.position)
            }
            val bounds = builder.build()

            val padding = 200 // offset from edges of the map in pixels

            return CameraUpdateFactory.newLatLngBounds(bounds, padding)
        }
    }
    private fun drawMarker(context: Context, text: String): Bitmap {
        val drawable = ContextCompat.getDrawable(_activity,R.drawable.ic_marker)!!
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        val paint = Paint()
        paint.textSize = 50 * context.resources.displayMetrics.density / 2
        paint.style = Paint.Style.FILL
        val textCanvas = Canvas(bitmap)
        textCanvas.drawText(text, ((bitmap.width * 7) / 20).toFloat(), (bitmap.height / 2).toFloat(), paint)

        return bitmap
    }
    private fun drawRoute(context: Context): PolylineOptions {
        val polylineOptions = PolylineOptions()
        polylineOptions.width(px2dip(context, 48.toFloat()).toFloat())
        polylineOptions.geodesic(true)
        polylineOptions.color(ContextCompat.getColor(context,R.color.red_light))
        return polylineOptions
    }
    private fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}