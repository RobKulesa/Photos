package photos.structures;
import java.util.*;
/**
 * Photo is a class that models a photo in the application
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */

public class Photo {
    private String path;
    private String caption;
    private GregorianCalendar lastModified;
    private HashMap<String,String> tags;

    public Photo(String path){
        this.path = path;
        this.caption = null;
        this.lastModified = null;       // NEED TO FIX
        this.tags = new HashMap<String, String>();
    }

    public String getPath(){
        return this.path;
    }
    
    public void setPath(String newPath){
        this.path = newPath;
    }

    public String getCaption(){
        return this.caption;
    }

    public void setCaption(String newCaption){
        this.caption = newCaption;
    }

    public GregorianCalendar getLastModified(){
        return this.lastModified;
    }

    public void setLastModified(GregorianCalendar newDate){
        this.lastModified = newDate;
    }

    public HashMap<String, String> getTags(){
        return this.tags;
    }

    public void insertTag(String name, String val){
        this.tags.put(name, val);
    }

    public String getTagByName(String name){
        return this.tags.get(name);
    }


}
