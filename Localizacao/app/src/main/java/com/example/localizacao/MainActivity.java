package com.example.localizacao;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnGpsProg;
    TextView txtLatitudeProg, txtLongitudeProg, txtAltitudeProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitudeProg = (TextView) findViewById(R.id.txtLatitude);
        txtLongitudeProg = (TextView) findViewById(R.id.txtLongitude);
        txtAltitudeProg = (TextView) findViewById(R.id.txtAltitude);

        btnGpsProg = (Button) findViewById(R.id.btnGps);
        btnGpsProg.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                solicitarPermissao();
            }
        });

    }
    private void solicitarPermissao() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
            ativarServicoGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[]
                                                   grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    ativarServicoGPS();
                } else {
                    Toast.makeText(this, "Sem conexão com o GPS!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void ativarServicoGPS(){
        try {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {escreverLocalizacao(location);}
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                public void onProviderEnabled(String provider) { }
                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void escreverLocalizacao(Location location)
    {
        Double longitude = location.getLongitude();
        Double latitude = location.getLatitude();
        Double altitude = location.getAltitude();

        txtLongitudeProg.setText(longitude.toString());
        txtLatitudeProg.setText(latitude.toString());
        txtAltitudeProg.setText(altitude.toString());
    }

}