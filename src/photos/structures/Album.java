package photos.structures;
import java.util.*;
/**
 * Photo is a class that models a photo-album in the application
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class Album {
    /**
     * Name of the album
     */
    private String name;
    
    /**
     * Data structure that holds all the photos associated with the album
     */
    private ArrayList<Photo> photoAlbum;

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
        this.photoAlbum = new ArrayList<Photo>();
        this.earliestDate = null;
        this.latestDate = null;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public void insertPhoto(Photo p){
        this.photoAlbum.add(p);
        this.updateDates();
    }

    public void deletePhoto(Photo p){
        this.photoAlbum.remove(p);
        this.updateDates();
    }

    public ArrayList<Photo> getPhotoAlbum(){
        return this.photoAlbum;
    }

    @Override
    public String toString(){
        String s = "";
        s += this.name;
        s += "\t";
        s+= Integer.toString(this.getPhotoAlbum().size());
        s+= "\t";
        
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

    public void updateDates(){
        if(this.photoAlbum.size() == 0){
            this.earliestDate = null;
            this.latestDate = null;
        } else if(this.photoAlbum.size() == 1){
            this.earliestDate = this.photoAlbum.get(0).getLastModified();
        }else {
            for(Photo p : this.photoAlbum){
                if(p.getLastModified().compareTo(this.earliestDate) < 0)
                    this.earliestDate = p.getLastModified();
                else if(p.getLastModified().compareTo(this.latestDate) > 0)
                    this.latestDate = p.getLastModified();
            }
        }
    }

}