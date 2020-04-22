package edu.asu.bsse.mtmille5.appname;

import android.content.res.AssetManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaceLibrary implements Serializable {
    List<PlaceDescription> places;

    PlaceLibrary(List<PlaceDescription> places){
        this.places = places;
    }

    List<String> getPlacesNames(){
        List<String> placesNames = new ArrayList<>();
        for(PlaceDescription pd: places){
            placesNames.add(pd.name);
        }
        return placesNames;
    }
}
