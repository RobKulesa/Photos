package photos.structures;
import java.util.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;

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
        this.updateDates();
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

    public boolean containsPhoto(Photo p) {
        return this.photos.contains(p);
    }

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
                    System.out.println("Comparing::\n" + p.toString());
                    System.out.printf("\tComparetoValue: %d\n", p.getLastModified().compareTo(this.earliestDate));

                    allNull = false;
                    if(p.getLastModified().compareTo(this.earliestDate) < 0){
                        this.earliestDate = p.getLastModified();
                        System.out.println("because compareto is negative, we are making it the new earliest date");
                        System.out.println(this.toString());
                    }
                    else if(p.getLastModified().compareTo(this.latestDate) > 0){
                        System.out.println("because compareto is positive, we are making it the new latest date");
                        System.out.println(this.toString());
                        this.latestDate = p.getLastModified();
                    }
                        
                }
            }
            if(allNull){
                System.out.println("Setting all to null");
                this.earliestDate = null;
                this.latestDate = null;
            }
        }

        System.out.println("Final result:" + this.toString());
    }

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
