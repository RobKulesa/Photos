package photos.structures;
import java.util.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Album is a class that models a photo-album in the application
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class Album implements Serializable {
    /**
     * Serial version of this class.
     */
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

    /**
     * Create a new Album given its name.
     * 
     * @param name  String that the Album will have its name assigned to
     */
    public Album(String name){
        this.name = name;
        this.photos = new ArrayList<Photo>();
        this.earliestDate = null;
        this.latestDate = null;
    }

    /**
     * Get the name of this album.
     * 
     * @return String   the name of the Album
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of this album.
     * 
     * @param newName   New name for the album.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Add a photo to this album and update the album's date properties.
     * 
     * @param p    The photo to be added.
     */
    public void addPhoto(Photo p) {
        this.photos.add(p);
        this.updateDates();
    }

    /**
     * Remove a photo from this album and update the album's date properties.
     * 
     * @param p    The photo to be removed from this album.
     */
    public void deletePhoto(Photo p) {
        this.photos.remove(p);
        this.updateDates();
    }

    /**
     * Get the ArrayList of photos in this album.
     * 
     * @return    ArrayList of type Photo with the photos in this album.
     */
    public ArrayList<Photo> getPhotos() {
        if(this.photos == null) this.photos = new ArrayList<Photo>();
        return this.photos;
    }

    /**
     * Get the number of photos in this album.
     * 
     * @return    The number of photos in this album.
     */
    public int getNumPhotos() {
        return this.photos.size();
    }

    /**
     * Return <code>true</code> if this album contains the passed photo.
     * 
     * @param p    The photo to be checked if located in this album.
     * @return     <code>true</code> if this album contains the passed photo.
     *             <code>false</code> otherwise.
     */
    public boolean containsPhoto(Photo p) {
        return this.photos.contains(p);
    }

    /**
     * Return the formatted string to display basic info about this album.
     * 
     * @return    Formatted string with basic info about this album.
     */
    @Override
    public String toString() {
        String s = "";
        s += this.name;
        s += " | ";
        s += Integer.toString(this.getPhotos().size());
        s += " | ";
        
        if(this.earliestDate != null && this.latestDate != null){
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
            fmt.setCalendar(this.earliestDate);
            String formattedDate = fmt.format(this.earliestDate.getTime());
            s+= formattedDate;

            s+= "\t---\t";
            SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
            fmt2.setCalendar(this.latestDate);
            formattedDate = fmt2.format(this.latestDate.getTime());
            s+= formattedDate;

        } else {
            s+= "N/A";
            s+= "-";
            s+="N/A";
        }
        return s;
                
    }

    /**
     * Update the earliest date and latest date fields of this album.
     */
    public void updateDates() {
        if(this.photos.size() == 0) {
            this.earliestDate = null;
            this.latestDate = null;
        } else if(this.photos.size() == 1) {
            if(this.photos.get(0).getLastModified()!= null){
                this.earliestDate = this.photos.get(0).getLastModified();
                this.latestDate = this.photos.get(0).getLastModified();
            }
            else{
                this.earliestDate = null;
                this.latestDate = null;
            }
        } else {
            boolean allNull = true;
            if(this.photos.get(0).getLastModified()!= null){
                this.earliestDate = this.photos.get(0).getLastModified();
                this.latestDate = this.photos.get(0).getLastModified();
            }
            else{
                this.earliestDate = null;
                this.latestDate = null;
            }


            for(Photo p : this.photos) {
                if(p.getLastModified() != null){
                    allNull = false;
                    if(p.getLastModified().compareTo(this.earliestDate) < 0){
                        this.earliestDate = p.getLastModified();
                    }
                    else if(p.getLastModified().compareTo(this.latestDate) > 0){
                        this.latestDate = p.getLastModified();
                    }
                        
                }
            }
            if(allNull){
                this.earliestDate = null;
                this.latestDate = null;
            }
        }

    }

    /**
     * Returns <code>true</code> if the passed string or album's name is equal to this album's name.
     * 
     * @return    <code>true</code> if the passed object is equal to this album.
     *            <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof Album || o instanceof String)) {
            return false;
        }
        if(o instanceof Album) {
            Album otherUser = (Album) o;
            return this.getName().equalsIgnoreCase(otherUser.getName());
        } else {
            String otherString = (String) o;
            return this.getName().equalsIgnoreCase(otherString);
        }
    }
}
