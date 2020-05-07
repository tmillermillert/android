package edu.asu.bsse.mtmille5.appname;

/*
 * Copyright 2020 Marcus Miller,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Purpose: Depreciated: Holds place description.
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Marcus Miller mailto:mtmille5@asu.edu
 * @version April 2020
 */

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
