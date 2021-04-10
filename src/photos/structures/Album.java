package photos.structures;
import java.util.*;
import java.io.Serializable;

/**
 * Photo is a class that models a photo-album in the application
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class Album implements Serializable {
    
    private static final long serialVersionUID = 2L;
    
    /**
     * Name of the album
     */
    private String name;
    
    /**
     * Data structure that holds all the photos associated with the album
     */
    private ArrayList<Photo> photos;

    /**
     * A Calendar object that represents the earliest modification date in the album
     */
    private GregorianCalendar earliestDate;

    /**
     * A Calendar object that represents the latest modification date in the album
     */
    private GregorianCalendar latestDate;


    public Album(String name){
        this.name = name;
        this.photos = new ArrayList<Photo>();
        this.earliestDate = null;
        this.latestDate = null;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void addPhoto(Photo p) {
        this.photos.add(p);
        //this.updateDates(); TODO: fix this
    }

    public void deletePhoto(Photo p) {
        this.photos.remove(p);
        this.updateDates();
    }

    public ArrayList<Photo> getPhotos() {
        if(this.photos == null) this.photos = new ArrayList<Photo>();
        return this.photos;
    }

    public int getNumPhotos() {
        return this.photos.size();
    }

    @Override
    public String toString() {
        String s = "";
        s += this.name;
        s += " | ";
        s += Integer.toString(this.getPhotos().size());
        s += " | ";
        
        if(this.earliestDate != null && this.latestDate != null){
            s+= this.earliestDate.toString();
            s+= "-";
            s+= this.latestDate.toString();
        } else {
            s+= "N/A";
            s+= "-";
            s+="N/A";
        }
        return s;
    }

    public void updateDates() {
        if(this.photos.size() == 0) {
            this.earliestDate = null;
            this.latestDate = null;
        } else if(this.photos.size() == 1) {
            if(this.photos.get(0).getLastModified()!= null)
                this.earliestDate = this.photos.get(0).getLastModified();
            else{
                this.earliestDate = null;
                this.latestDate = null;
            }
        } else {
            boolean allNull = true;
            for(Photo p : this.photos) {
                if(p.getLastModified() != null){
                    allNull = false;
                    if(p.getLastModified().compareTo(this.earliestDate) < 0)
                        this.earliestDate = p.getLastModified();
                    else if(p.getLastModified().compareTo(this.latestDate) > 0)
                        this.latestDate = p.getLastModified();
                }
            }
            if(allNull){
                this.earliestDate = null;
                this.latestDate = null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof Album)) {
            return false;
        }
        Album other = (Album) o;
        return this.getName().equalsIgnoreCase(other.getName());
    }
}
