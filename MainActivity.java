package com.bouh.mapgps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class MainActivity extends Activity {
    private static final int REQ_LOCATION = 505;
    private WebView webView;
    private LocationManager locationManager;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setGeolocationEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        webView.addJavascriptInterface(new BouhBridge(), "BouhAndroid");

        ensureLocationPermission();
        webView.loadUrl("file:///android_asset/map.html");
    }

    private void ensureLocationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23 &&
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQ_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "GPS enabled for BOUH Map", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission is required for GPS navigation.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class BouhBridge {
        @JavascriptInterface
        public String getLastKnownLocation() {
            try {
                if (android.os.Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return "{\"ok\":false,\"error\":\"NO_PERMISSION\"}";
                }
                Location best = null;
                for (String provider : locationManager.getProviders(true)) {
                    Location loc = locationManager.getLastKnownLocation(provider);
                    if (loc != null && (best == null || loc.getAccuracy() < best.getAccuracy())) best = loc;
                }
                if (best == null) return "{\"ok\":false,\"error\":\"NO_FIX\"}";
                return "{\"ok\":true,\"lat\":" + best.getLatitude() +
                       ",\"lon\":" + best.getLongitude() +
                       ",\"acc\":" + best.getAccuracy() +
                       ",\"alt\":" + best.getAltitude() +
                       ",\"time\":" + best.getTime() + "}";
            } catch (Exception e) {
                return "{\"ok\":false,\"error\":\"" + e.getMessage().replace("\"","'") + "\"}";
            }
        }

        @JavascriptInterface
        public void openLocationSettings() {
            try {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } catch (Exception ignored) {}
        }
    }
}
