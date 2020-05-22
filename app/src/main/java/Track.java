import java.util.ArrayList;

/*
    This class is the constructor for a Track, and sets the attributes specified by the user in the
    'Add Track-able' form.
 */
public class Track {

    String name;
    String y_n;
    ArrayList<String> details;

    public Track(String name, String y_n, ArrayList<String> details) {
        this.name = name;
        this.y_n = y_n;
        this.details = details;
    }

    public ArrayList<String> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<String> details) {
        this.details = details;
    }

    public String getY_n() {
        return y_n;
    }

    public void setY_n(String y_n) {
        this.y_n = y_n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
