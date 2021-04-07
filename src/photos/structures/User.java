package photos.structures;

import java.io.Serializable;

/**
 * User is a serializable class used to differentiate between different users of the application.
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
     * Create a new user.
     * 
     * @param username    Username for the new user.
     */
    public User(String username) {
        this.username = username;
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
