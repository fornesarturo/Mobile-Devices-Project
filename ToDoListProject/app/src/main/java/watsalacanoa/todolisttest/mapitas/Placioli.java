package watsalacanoa.todolisttest.mapitas;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by spide on 4/4/2017.
 */

public class Placioli {

    private String title;
    private String description;
    private LatLng latLng;

    public String getTitle(){ return this.title;}
    public String getDescription() {return this.description;}

    public Placioli(String title, String desc, LatLng latLng){
        this.title = title;
        this.description = desc;
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public String toString(){
        return getTitle() + " " + getDescription();
    }
}
