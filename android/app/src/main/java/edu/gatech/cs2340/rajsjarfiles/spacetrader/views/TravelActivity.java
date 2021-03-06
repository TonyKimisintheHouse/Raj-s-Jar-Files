package edu.gatech.cs2340.rajsjarfiles.spacetrader.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

import edu.gatech.cs2340.rajsjarfiles.spacetrader.R;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.player.Location;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.player.Player;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.universe.Planet;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.universe.SolarSystem;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.model.Model;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.viewmodels.TravelViewModel;

public class TravelActivity extends BaseActivity {
    /**
     * View model for Travel activity
     */
    private TravelViewModel viewModel;

    /**
     * Names of reachable Planet destinations
     */
    private ArrayList<String> destinations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        viewModel = ViewModelProviders.of(this).get(TravelViewModel.class);

        GridView destinationGrid = findViewById(R.id.destinationGrid);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getDestinations());
        destinationGrid.setAdapter(adapter);
        destinationGrid.setOnItemClickListener(clickListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        MapView mapView = findViewById(R.id.solarSystemMapView);
        mapView.turnOffView();
    }

    /**
     * Return possible destinations within in the solar system
     * @return arraylist of the names of destination planets
     */
    private ArrayList<String> getDestinations() {
        // Get object references
        Model model = Model.getCurrent();
        Player player = model.getPlayer();
        Location location = player.getLocation();
        Planet currentPlanet = location.getPlanet();

        // Get current planets
        Planet[] planets = player.getLocation().getSolarSystem().getPlanets();

        // Make a list of reachable planets
        this.destinations = new ArrayList<>();
        for (Planet planet : planets) {
            // If there is enough fuel to reach: current planet --> new planet
            if (location.checkIfTravelPossible(
                    location.getSolarSystem(), planet) != -1) {
                //if (planet.getDist(currentPlanet) <= ship.getFuel()) {
                if (!planet.equals(currentPlanet)) {
                    this.destinations.add(planet.getName());
                }
            }
        }
        return this.destinations;
    }

    private AdapterView.OnItemClickListener clickListener
            = new AdapterView.OnItemClickListener() {
                @Override
            public void onItemClick(
                    AdapterView<?> adapterView, View view, int i, long l) {
                    // Travel to that destination
                    String planetName = TravelActivity.this.destinations.get(i);
                    Player player = Model.getCurrent().getPlayer();
                    SolarSystem system = player.getLocation().getSolarSystem();
                    Log.d("Sonny",
                            system.getPlanetByName(planetName).toString());
                    player.travel(system, system.getPlanetByName(planetName));

            /// Close this activity
                    Intent intent = new Intent(
                            getApplicationContext(), RandomEventActivity.class);
                    TravelActivity.this.finish();
                    startActivity(intent);
                    MapView mapView = findViewById(R.id.solarSystemMapView);
                    mapView.turnOffView();
        }
    };
}

