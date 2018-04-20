package com.droid.ykozh.yandexmap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.SearchType;
import com.yandex.mapkit.search.SuggestItem;
import com.yandex.runtime.Error;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

/**
 * This example shows how to request a suggest for search requests.
 */
public class SuggestActivity extends Activity implements SearchManager.SuggestListener {
    /**
     * Replace "your_api_key" with a valid developer key.
     * You can get it at the https://developer.tech.yandex.ru/ website.
     */
    private final String MAPKIT_API_KEY = "4f18e9f3-97ee-414e-9ce1-c7d27e8fb424";
    private final int RESULT_NUMBER_LIMIT = 5;

    private SearchManager searchManager;
    private ListView suggestResultView;
    private ArrayAdapter resultAdapter;
    private List<String> suggestResult;

    private final Point CENTER = new Point(55.75, 37.62);
    private final double BOX_SIZE = 0.2;
    private final BoundingBox BOUNDING_BOX = new BoundingBox(
        new Point(CENTER.getLatitude() - BOX_SIZE, CENTER.getLongitude() - BOX_SIZE),
        new Point(CENTER.getLatitude() + BOX_SIZE, CENTER.getLongitude() + BOX_SIZE));
    private final SearchOptions SEARCH_OPTIONS =  new SearchOptions().setSearchTypes(
        SearchType.GEO.value |
        SearchType.BIZ.value |
        SearchType.TRANSIT.value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(com.droid.ykozh.yandexmap.R.layout.suggest);
        super.onCreate(savedInstanceState);

        searchManager = MapKitFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
        EditText queryEdit = (EditText)findViewById(com.droid.ykozh.yandexmap.R.id.suggest_query);
        suggestResultView = (ListView)findViewById(com.droid.ykozh.yandexmap.R.id.suggest_result);
        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                suggestResult);
        suggestResultView.setAdapter(resultAdapter);

        queryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                requestSuggest(editable.toString());
            }
        });
    }

    @Override
    protected void onStop() {
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onSuggestResponse(List<SuggestItem> suggest) {
        suggestResult.clear();
        for (int i = 0; i < Math.min(RESULT_NUMBER_LIMIT, suggest.size()); i++) {
            suggestResult.add(suggest.get(i).getDisplayText());
        }
        resultAdapter.notifyDataSetChanged();
        suggestResultView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuggestError(Error error) {
        String errorMessage = getString(com.droid.ykozh.yandexmap.R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMessage = getString(com.droid.ykozh.yandexmap.R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(com.droid.ykozh.yandexmap.R.string.network_error_message);
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void requestSuggest(String query) {
        suggestResultView.setVisibility(View.INVISIBLE);
        searchManager.suggest(query, BOUNDING_BOX, SEARCH_OPTIONS, this);
    }
}
