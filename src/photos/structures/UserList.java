package photos.structures;

import java.io.*;
import java.util.*;

import photos.Debug;

/**
 * UserList is a serializable class that holds an ArrayList of {@link User}s. An instance
 * of this class can be saved and loaded between runs of the application.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class UserList implements Serializable {

    /**
     * Serial version of this class.
     */
    private static final long serialVersionUID = 2L;

    /**
     * ArrayList of users.
     */
    private ArrayList<User> users;

    /**
     * Directory where serialized UserList objects will be stored.
     */
    public static final String storeDir = "src/resources/bin";

    /**
     * File where serialized UserList objects will be stored.
     */
    public static final String storeFile = "users.dat";
    
    /**
     * Create a UserList object with an empty ArrayList of {@link User}s.
     */
    public UserList(){
        this.users = new ArrayList<User>();
    }

    /**
     * Create a UserList object with a passed ArrayList of {@link User}s.
     * 
     * @param users    ArrayList of {@link User}s.
     */
    public UserList(ArrayList<User> users) {
        if(users == null) users = new ArrayList<User>();
        this.users = users;
    }

    /**
     * Add a user to the UserList.
     */
    public void addUser(User u){
        users.add(u);
    }

    public void addUser(User u, int index) {
        users.add(index, u);
    }

    /**
     * Remove a user to the UserList.
     * 
     * @param u    {@link User} to be removed.
     */
    public void removeUser(User u) {
        users.remove(u);
    }
    
    /**
     * Get the number of {@link User}s in the list.
     * 
     * @return    Number of {@link User}s in the list.
     */
    public int getLength(){
        return users.size();
    }
    
    /**
     * Get the ArrayList of {@link User}s.
     * 
     * @return    ArrayList of {@link User}.
     */
    public ArrayList<User> getUsers() {
        return this.users;
    }
    
    /**
     * Get the user at index <code>index</code>.
     * 
     * @param index    Index of {@link User}.
     * @return         {@link User} at index <code>index</code>.
     */
    public User getUser(int index){
        return users.get(index);
    }

    /**
     * Write the passed instance of UserList to the filesystem.
     * 
     * @param userList        Instance of UserList to be written.
     * @throws IOException    Thrown if file failed to be opened.
     */
    public static void writeUserList(UserList userList) throws IOException {
        if(userList == null || userList.getUsers() == null || userList.getUsers().isEmpty()) return;
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(userList);
        oos.close();
        if(Debug.debugUsers) System.out.println("UserList writeUserList() wrote:" + userList.toString());
    }

    /**
     * Read from the filesystem a previously written UserList instance.
     * 
     * @return                           UserList instance read from filesystem.
     * @throws IOException               Thrown if file failed to be opened.
     * @throws ClassNotFoundException    Thrown if object could not be created.
     */
    public static UserList readUserList() throws IOException, ClassNotFoundException {
        ObjectInputStream ois;
        UserList userList;
        try {
            ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
            userList = (UserList) ois.readObject();
            ois.close();
        } catch(EOFException e) {
            userList = new UserList();
        }
        
        if(userList == null) {
            if(Debug.debugUsers) System.out.println("\tuserList is null");
            userList = new UserList();
            userList.addUser(new User("admin"));
        } else {
            if(userList.getUsers() == null) {
                if(Debug.debugUsers) System.out.println("\tuserList.users is null");
                userList = new UserList();
                userList.addUser(new User("admin"));
            } 
            if(!userList.containsUser("admin")) {
                userList.addUser(new User("admin"), 0);
            }
            if(!userList.containsUser("stock")) {
                if(Debug.debugUsers) System.out.println("UserList: Stock not found, creating it!");
                userList.addUser(new User("stock"), 1);
            }
        }

        if(Debug.debugUsers) System.out.println("UserList readUserList() read:" + userList.toString());

        return userList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(User u : this.users) {
            sb.append(u.getUsername());
            sb.append(": (|");
            for(Album a : u.getAlbumList()) {
                sb.append(a.getName());
                sb.append(", ");
                sb.append(a.getNumPhotos());
                sb.append("|");
            }
            sb.append(")\n");
        }
        sb.append("]\n");
        return sb.toString();
    }
    
    public boolean containsUser(User u) {
        for(User listUser : this.users) {
            if(listUser.equals(u)) return true;
        }
        return false;
    }

    public boolean containsUser(String u) {
        for(User listUser : this.users) {
            if(listUser.equals(u)) return true;
        }
        return false;
    }
}
