package photos.structures;

import java.io.Serializable;
import java.util.*;
/**
 * User is a serializable class used to differentiate between different users of the application.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class User implements Serializable {

    /**
     * Serial version of this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Username of this user.
     */
    private String username;

    /**
     * List of albums assocaited with this user
     */
    private ArrayList<Album> albums;

    /**
     * Create a new user.
     * 
     * @param username    Username for the new user.
     */
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<Album>();
    }

    /**
     * Set the username of this user.
     * 
     * @param username    Username to be for this user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the username of this user.
     * 
     * @return    Username of this user.
     */
    public String getUsername() {
        return this.username;
    }

    public ArrayList<Album> getAlbumList(){
        return this.albums;
    }

    /**
     * Get the string representation of this user.
     * 
     * @return    String representation of this user.
     */
    @Override
    public String toString() {
        return this.username;
    }

    /**
     * Return <code>true</code> if this user equals User o.
     * 
     * @return    <code>true</code> if this user equals User o.
     *            <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        return this.getUsername().equalsIgnoreCase(other.getUsername());
    }
}
