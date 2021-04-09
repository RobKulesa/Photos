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
    private static final long serialVersionUID = 2;

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

        if(username.equalsIgnoreCase("stock")) {
            Album stockNature = new Album("nature");
            Album stockArt = new Album("art");

            Photo nature1 = new Photo("data/nature1.jpg");
            Photo nature2 = new Photo("data/nature2.jpg");
            Photo art1 = new Photo("data/art1.png");
            Photo art2 = new Photo("data/art2.png");
            Photo art3 = new Photo("data/art3.jpg");

            nature1.addTag("scene", "mountain");
            nature1.addTag("scene", "lake");
            nature1.addTag("season", "fall");

            nature2.addTag("scene", "trail");
            nature2.addTag("season", "summer");

            art1.addTag("medium", "digital");
            art1.addTag("subject", "chameleon");

            art2.addTag("medium", "digital");
            art2.addTag("subject", "watermelon");

            art3.addTag("medium", "digital");
            art3.addTag("subject", "pokemon");

            stockNature.addPhoto(nature1);
            stockNature.addPhoto(nature2);

            stockArt.addPhoto(art1);
            stockArt.addPhoto(art2);
            stockArt.addPhoto(art3);

            this.albums.add(stockNature);
            this.albums.add(stockArt);
        }
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
        if(o==null || !(o instanceof User || o instanceof String)) {
            return false;
        }
        if(o instanceof User) {
            User otherUser = (User) o;
            return this.getUsername().equalsIgnoreCase(otherUser.getUsername());
        } else {
            String otherString = (String) o;
            return this.getUsername().equalsIgnoreCase(otherString);
        }
    }
}
