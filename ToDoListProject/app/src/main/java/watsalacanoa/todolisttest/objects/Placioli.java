package watsalacanoa.todolisttest.objects;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by spide on 4/4/2017.
 */

public class Placioli {

    private String title;
    private String description;
    private LatLng latLng;
    private int id;

    public String getTitle(){ return this.title;}
    public String getDescription() {return this.description;}
    public int getId() { return this.id; }

    public Placioli(String title, String desc, LatLng latLng, int id){
        this.title = title;
        this.description = desc;
        this.latLng = latLng;
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public String toString(){
        return getTitle() + " " + getDescription();
    }
}
