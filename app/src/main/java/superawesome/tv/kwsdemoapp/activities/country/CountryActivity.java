package superawesome.tv.kwsdemoapp.activities.country;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseActivity;

public class CountryActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.countryToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // get the list
        EditText countrySearch = (EditText) findViewById(R.id.SearchCountry);
        ListView countriesListView = (ListView) findViewById(R.id.countryListView);

        RxTextView.textChanges(countrySearch)
                .map(charSequence -> charSequence.toString().toLowerCase())
                .map(searchQuery -> {
                    String[] isoCountryCodes = Locale.getISOCountries();
                    List<String> filtered = new ArrayList<>();
                    for (String code : isoCountryCodes) {
                        Locale locale = new Locale("", code);
                        if (locale.getDisplayCountry().toLowerCase().contains(searchQuery)) {
                            filtered.add(code);
                        }
                    }
                    return filtered;
                })
                .subscribe(filteredIsoCodes -> {

                    Observable.from(filteredIsoCodes)
                            .map(s -> new CountryRowViewModel(CountryActivity.this, s))
                            .toList()
                            .subscribe(countryRowViewModels -> {

                                RxDataSource.from(CountryActivity.this, countryRowViewModels)
                                        .bindTo(countriesListView)
                                        .customiseRow(R.layout.listitem_country_row, CountryRowViewModel.class, (viewModel, view) -> {

                                            Context context = CountryActivity.this;
                                            Resources resources = context.getResources();
                                            String packageName = context.getPackageName();

                                            TextView countryNameTxt = (TextView) view.findViewById(R.id.CountryName);
                                            countryNameTxt.setText(viewModel.getCountryName());

                                            ImageView countryFlag = (ImageView) view.findViewById(R.id.CountryIcon);

                                            try {
                                                int flagId = resources.getIdentifier(viewModel.getCountryFlagString(), "drawable", packageName);
                                                countryFlag.setImageDrawable(resources.getDrawable(flagId));
                                            } catch (Exception e){
                                                int flagId = resources.getIdentifier(viewModel.getDefaultCountryFlagString(), "drawable", packageName);
                                                countryFlag.setImageDrawable(resources.getDrawable(flagId));
                                            }

                                        })
                                        .onRowClick(R.layout.listitem_country_row, (integer, selectedViewModel) -> {

                                            Intent intent = new Intent();
                                            intent.putExtra("k_COUNTRY_DATA", selectedViewModel.writeToJson().toString());
                                            setResult(RESULT_OK, intent);
                                            finish();

                                        })
                                        .update();

                            });

                });
    }
}
