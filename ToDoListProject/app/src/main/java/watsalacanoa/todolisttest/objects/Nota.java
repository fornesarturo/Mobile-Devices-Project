package watsalacanoa.todolisttest.objects;

/**
 * Created by forne on 30/03/2017.
 */

public class Nota {

    String title, image;
    int ID;

    public Nota (String title, String img, int ID) {
        this.ID = ID;
        this.title = title;
        this.image = img;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImage() {
        return this.image;
    }

    public int getID() {
        return this.ID;
    }
}
