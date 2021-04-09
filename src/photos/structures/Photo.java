package photos.structures;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;

/**
 * Photo is a class that models a photo in the application
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */

public class Photo implements Serializable {
    
    private static final long serialVersionUID = 3L;

    private String path;
    private String caption;
    private GregorianCalendar lastModified;
    private TreeMap<String, ArrayList<String>> tags;

    public Photo(String path){
        this.path = path;
        this.caption = null;
        this.lastModified = initLastModified(path);       // NEED TO FIX
        /* this.tags = new TreeMap<String, ArrayList<String>>(new Comparator<String>()  {

            @Override
            public int compare(String arg0, String arg1) {
                return arg0.compareToIgnoreCase(arg1);
            }
            
        }); */

        this.tags = new TreeMap<String, ArrayList<String>>(
            (Comparator<String> & Serializable) (o1, o2) -> {
                return o1.compareToIgnoreCase(o2);
            }
        );
    }

    public String getPath(){
        return this.path;
    }
    
    public void setPath(String newPath){
        this.path = newPath;
    }

    public GregorianCalendar initLastModified(String filepath){
        try {

            Path file = Paths.get(filepath);
            BasicFileAttributes attr =
                Files.readAttributes(file, BasicFileAttributes.class);

            String fileTimeToString;
            if(attr.lastModifiedTime() != null)
                fileTimeToString = attr.lastModifiedTime().toString();
            else
                fileTimeToString = attr.creationTime().toString();

            
            String year = fileTimeToString.substring(0,4);
            String month = fileTimeToString.substring(5,7);
            String day = fileTimeToString.substring(8, 10);
            String hours = fileTimeToString.substring(11, 13);
            String minutes = fileTimeToString.substring(14, 16);
            String seconds = fileTimeToString.substring(17,19);
            
            return new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
                                            Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds));

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
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

    public void setLastModified(GregorianCalendar newDate) {
        this.lastModified = newDate;
    }

    public TreeMap<String, ArrayList<String>> getTags(){
        return this.tags;
    }

    public Set<String> getTagNames() {
        return this.tags.keySet();
    }

    public void addTag(String name, String val) throws IllegalArgumentException {
        name = name.toLowerCase();
        val = val.toLowerCase();
        ArrayList<String> vals = new ArrayList<String>();
        if(!this.getTagNames().contains(name)) {
            this.tags.put(name, vals);
        }
        vals = this.getTagValsByName(name);
        for(String s : vals) {
            if(s.equals(val)) throw new IllegalArgumentException("Tag Value already exists for this photo!");
        }
        this.tags.get(name).add(val);
    }

    public ArrayList<String> getTagValsByName(String name) {
        name = name.toLowerCase();
        ArrayList<String> vals = this.tags.get(name);
        if(vals == null) vals = new ArrayList<String>();
        return vals;
    }

    
    public void removeTag(String name, String val) throws IllegalArgumentException {
        name = name.toLowerCase();
        if(!this.getTagNames().contains(name)) throw new IllegalArgumentException("Tag Name does not exist!");

        final String value = val.toLowerCase();
        ArrayList<String> vals = this.getTagValsByName(name);
        if(!vals.contains(val)) throw new IllegalArgumentException("Tag Value does not exist!");
        vals.removeIf(new Predicate<String>() {
            @Override
            public boolean test(String arg0) {
                return arg0.equalsIgnoreCase(value);
            }
        });
    }

    public boolean hasTag(String name, String val) {
        name = name.toLowerCase();
        val = val.toLowerCase();
        ArrayList<String> vals = this.getTagValsByName(name);
        if(vals.isEmpty()) return false;
        for(String s : vals) {
            if(s.equalsIgnoreCase(val)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String s = "";
        s+= this.path + "||";
        if(this.caption != null)
            s+= this.caption;
        else
            s+= "no caption available";
        s+= "||" + this.lastModified.toString();
        return s;
    }

    public boolean isInDateRange(GregorianCalendar from, GregorianCalendar to) {
        return this.lastModified.compareTo(from) >= 0 && this.lastModified.compareTo(to) <= 0;
    }
}
