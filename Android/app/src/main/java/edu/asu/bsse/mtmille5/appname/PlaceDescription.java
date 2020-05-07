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
 * Purpose: A place is a simplified holder for information about geographic places of interest, such as a waypoint
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Marcus Miller mailto:mtmille5@asu.edu
 * @version April 2020
 */
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.Math;
import org.json.JSONObject;

import static java.lang.Math.PI;

public class PlaceDescription implements Serializable {
    //A simple string, which is unique among all names of places for this user. Usually one or two words.
    public String name;
    //Text providing descriptive information about the place.
    public String description;
    //Text describing the type of place this entry describes. Sample categories are residence, travel, and hike.
    public String category;
    //This field is equivalent to the first line that would appear in an address, sometimes called the recipient line. It indicates the individual or organization to which the address pertains. This field is generally not used in a geocoding lookup.
    @SerializedName(value = "addressTitle", alternate = "address-title")
    public String addressTitle;
    //The rest of an address. This includes the street address, city or town, the state and optionally country and/or zip code. Although the examples indicate otherwise, you may assume USA address format only.
    @SerializedName(value = "addressStreet", alternate = "address-street")
    public String addressStreet;
    // Feet mean sea level elevation of the place. This field is also generally not used in geocoding services, but is useful in route planning.
    public Double elevation;
    //Degrees latitude, using a single double value to represent. Lines of equal latitude run parallel to the Equator. Values range from -90.0 to +90.0. Negative values refer to the southern hemisphere and positive values to the northern hemisphere.
    public Double latitude;
    //Degrees of longitude, using a single double value. Values range from -180.0 to +180.0. Lines of equal longitude run perpendicular to the Equator. Negative values increase west from the Prime Meridian, which is longitude 0.0 and is located in Greenwich England. Positive values increase east from the Prime Meridian. 180.0 (plus and minus) is the International Date Line.
    public Double longitude;

    PlaceDescription(){
        name = "";
        description = "";
        category = "";
        addressTitle = "";
        addressStreet = "";
        elevation = 0.0;
        latitude = 0.0;
        longitude = 0.0;
    }

    Double greatCircleSphericalDistance(PlaceDescription pd1, PlaceDescription pd2){
        Double R = 6371e3; // metres
        Double phi1 = pd1.latitude * Math.PI / 180;
        Double phi2 = pd2.latitude * Math.PI / 180;
        Double deltaPhi = (pd2.latitude-pd1.latitude) * Math.PI / 180;
        Double deltaLambda = (pd2.longitude-pd1.longitude) * Math.PI / 180;

        Double a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        Double d = R * c;
        return d;
    }

    Double initialHeading(PlaceDescription pd1, PlaceDescription pd2){
        Double phi1 = pd1.latitude * Math.PI / 180;
        Double phi2 = pd2.latitude * Math.PI / 180;
        Double lambda1 = pd1.longitude * Math.PI / 180;
        Double lambda2 = pd2.longitude * Math.PI / 180;
        Double y = Math.sin(lambda2-lambda1) * Math.cos(phi2);
        Double x = Math.cos(phi1)*Math.sin(phi2) -
                Math.sin(phi1)*Math.cos(phi2)*Math.cos(lambda2-lambda1);
        Double theta = Math.atan2(y, x);
        Double brng = (theta * 180 / Math.PI + 360) % 360;
        return brng;
    }

    PlaceDescription(String jsonStr){
        try{
            JSONObject jo = new JSONObject(jsonStr);
            name = jo.getString("name");
            description = jo.getString("description");
            category = jo.getString("category");
            addressTitle = jo.getString("addressTitle");
            addressStreet = jo.getString("addressStreet");
            elevation = jo.getDouble("elevation");
            latitude = jo.getDouble("latitude");
            longitude = jo.getDouble("longitude");
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
    }

    public String toJsonString(){
        String ret = "";
        try{
            JSONObject jo = new JSONObject();
            jo.put("name",name);
            jo.put("description", description);
            jo.put("category", category);
            jo.put("addressTitle", addressTitle);
            jo.put("addressStreet", addressStreet);
            jo.put("elevation", elevation);
            jo.put("latitude", latitude);
            jo.put("longitude", longitude);
            ret = jo.toString();
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return ret;
    }
}
