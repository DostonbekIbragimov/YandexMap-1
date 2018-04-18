package com.droid.ykozh.yandexmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MainActivity extends AppCompatActivity {


    private MapView mapview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("4f18e9f3-97ee-414e-9ce1-c7d27e8fb424");
        MapKitFactory.initialize(this);

        // Укажите имя activity вместо map.
        setContentView(R.layout.activity_main);
        mapview = (MapView)findViewById(R.id.mapview);
        mapview.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapview.onStart();
    }
}
