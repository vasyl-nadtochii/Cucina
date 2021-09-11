package com.faint.cucina.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.CafeActivity;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.activities.OrderActivity;
import com.faint.cucina.classes.Cafe;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private FloatingActionButton fabNext;
    private TextView infoTV;

    private Location currentLocation;
    private FusedLocationProviderClient providerClient;
    private SupportMapFragment supportMapFragment;
    private LatLng latLng;
    private GoogleMap myGmap;
    private LocationCallback locationCallback;
    private LocationManager locationManager;

    private ViewGroup loadingLayout;

    private static final int REQUEST_CODE = 10001;
    private boolean initialized = false;
    private final boolean forOrder;

    public MapFragment(boolean forOrder) {
        this.forOrder = forOrder;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        infoTV = root.findViewById(R.id.cafe_info_tv);
        ViewGroup infoLayout = root.findViewById(R.id.infoField);
        loadingLayout = root.findViewById(R.id.loadingLayout);

        if (forOrder)
            infoLayout.setVisibility(View.VISIBLE);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        fabNext = root.findViewById(R.id.fabNext);
        fabNext.setOnClickListener(this);

        FloatingActionButton fabUpdate = root.findViewById(R.id.fabUpdate);
        fabUpdate.setOnClickListener(this);

        providerClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        checkGPS();

        return root;
    }

    private void fetchLastLocation() {
        // here we`re getting perms for user`s location data
        assert getActivity() != null;
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat
                        .checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1500);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;

                        // init map fragment
                        if (isAdded()) {
                            supportMapFragment = (SupportMapFragment)
                                    getChildFragmentManager().findFragmentById(R.id.google_map);
                        }

                        assert supportMapFragment != null;
                        supportMapFragment.getMapAsync(MapFragment.this);
                    }
                }
            }
        };

        providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchLastLocation();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setMessage(getString(R.string.gps_notification))
                .setCancelable(false)
                .setPositiveButton(R.string.goto_sett,
                        (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)));

        final AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        myGmap = googleMap;
        googleMap.setOnMarkerClickListener(this);

        try {
            int currentNightMode = requireContext()
                    .getResources()
                    .getConfiguration()
                    .uiMode & Configuration.UI_MODE_NIGHT_MASK;

            // customising gmap depending on user`s theme
            if(currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                requireActivity(), R.raw.map_style_dark));

                if (!success) {
                    Log.e("Google Map Style", "Style parsing failed.");
                }
            }
        }
        catch (Resources.NotFoundException e) {
            Log.e("Google Map Style", "Can't find style. Error: ", e);
        }

        myGmap.setMyLocationEnabled(true);
        latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        // if this is a 1st launch of map, then it will move to the curr location,
        // but then it will only update it without moving
        if(!initialized) {
            getCafes();

            updateLocation(true);
            initialized = true;
        }
        else
            updateLocation(false);
    }

    private void getCafes() {
        final String userCity = UserDataSP.getInstance(requireContext()).getUser().getCity();

        MainActivity.requestFinished = false;

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_GET_CAFES,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            double latitude = object.getDouble("latitude");
                            double longitude = object.getDouble("longitude");
                            int state = object.getInt("state");

                            int drawable;

                            if(state == 1) {
                                drawable = R.drawable.map_cafe_open_marker;
                            }
                            else if(state == 2) {
                                drawable = R.drawable.map_cafe_takeaway_marker;
                            }
                            else {
                                drawable = R.drawable.map_cafe_closing_marker;
                            }

                            if(!(forOrder && state == 3)) {
                                LatLng innerLatLng = new LatLng(latitude, longitude);
                                MarkerOptions innerOptions = new MarkerOptions()
                                        .position(innerLatLng)
                                        .icon(bitmapDescriptorFromVector(getActivity(), drawable))
                                        .title("cafe");

                                myGmap.addMarker(innerOptions);
                            }
                        }

                        MainActivity.requestFinished = true;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(requireActivity(), "Не удалось подключиться к серверу!", Toast.LENGTH_SHORT).show();
                    MainActivity.requestFinished = true;
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_city", userCity);

                return params;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }

    // method that moves user to his current location (gmap)
    public void updateLocation(boolean move) {
        if(move)
            myGmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    // checks if GPS is enabled or not
    private void checkGPS() {
        if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            fetchLastLocation();
        else
            buildAlertMessageNoGps();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab && initialized) {
            if( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) )
                updateLocation(true);
            else
                buildAlertMessageNoGps();
        }
        else if(view.getId() == R.id.fabNext && initialized) {
            OrderActivity.orderConfInterface.goToNext();
            OrderActivity.orderConfInterface.showBtnNext(true);
        }
        else if(view.getId() == R.id.fabUpdate) {
            myGmap.clear();
            getCafes();     // i`m not sure about that
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            }
        } else {
            Log.d("LOCATION PERMISSION", "DENIED");
        }
    }

    // converter for vector drawables
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(loadingLayout.getVisibility() == View.GONE) {
            final double latitude = marker.getPosition().latitude;
            final double longitude = marker.getPosition().longitude;

            MainActivity.requestFinished = false;
            loadingLayout.setVisibility(View.VISIBLE);

            StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_GET_CAFE_BY_POS,
                    response -> {
                        try {
                            if (!response.trim().equals("fail")) {
                                JSONObject object = new JSONObject(response);

                                int state = object.getInt("state");
                                String address = object.getString("address");
                                int id = object.getInt("id");

                                JSONArray jsonArray = new JSONArray(object.getString("img_urls"));
                                ArrayList<String> urlList = new ArrayList<>();

                                for (int j = 0; j < jsonArray.length(); j++) {
                                    urlList.add(jsonArray.getString(j));
                                }

                                Cafe chosenCafe = new Cafe(latitude, longitude, state, address, id, urlList);

                                if (forOrder) {
                                    OrderActivity.order.setCafeID(chosenCafe.getCafeID());
                                    OrderActivity.address = chosenCafe.getAddress();

                                    String info = "Выбрано: " + chosenCafe.getAddress();
                                    infoTV.setText(info);

                                    fabNext.show();
                                } else {
                                    Intent intent = new Intent(requireActivity(), CafeActivity.class);
                                    intent.putExtra("CAFE", chosenCafe);
                                    startActivity(intent);
                                }
                            }

                            MainActivity.requestFinished = true;
                            loadingLayout.setVisibility(View.GONE);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(requireActivity(), "Не удалось подключиться к серверу!", Toast.LENGTH_SHORT).show();
                        MainActivity.requestFinished = true;
                        loadingLayout.setVisibility(View.GONE);
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("lat", String.valueOf(latitude));
                    params.put("lng", String.valueOf(longitude));

                    return params;
                }
            };

            VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
        }

        return true;
    }

    @Override
    public void onDestroy() {
        providerClient.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }
}
