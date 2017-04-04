package watsalacanoa.todolisttest.objects;

import java.util.Date;

/**
 * Created by MiguelAngel on 03/04/2017.
 */

public class Pending {

    private String title, event;
    private Date date;

    public Pending(Date date, String title, String event) {
        this.date = date;
        this.title = title;
        this.event = event;
    }

    public Date getDate() {
        return this.date;
    }

    public String getTitle() {
        return this.title;
    }

    public String getEvent() {
        return this.event;
    }

}
