package photos.structures;

import java.io.*;
import java.util.*;

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
     * 
     * @param u     The User to be added to the UserList
     */
    public void addUser(User u){
        users.add(u);
    }

    /**
     * Add a user to the UserList at a specific index.
     * 
     * @param u         The User to be added to the UserList
     * @param index     The index at which the User will be inserted
     */
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
            userList = new UserList();
            userList.addUser(new User("admin"));
        } else {
            if(userList.getUsers() == null) {
                userList = new UserList();
                userList.addUser(new User("admin"));
            } 
            if(!userList.containsUser("admin")) {
                userList.addUser(new User("admin"), 0);
            }
            if(!userList.containsUser("stock")) {
                userList.addUser(new User("stock"), 1);
            }
        }


        return userList;
    }
    
    /**
     * Returns <code>true</code> if passed User u is already in this user list.
     * 
     * @param u    The user to be checked.
     * @return     <code>true</code> if passed User u is already in this user list.
     *             <code>false</code> otherwise.
     */
    public boolean containsUser(User u) {
        for(User listUser : this.users) {
            if(listUser.equals(u)) return true;
        }
        return false;
    }

    /**
     * Returns <code>true</code> if passed String u matches a user's username in this user list.
     * 
     * @param u    The username to be checked.
     * @return     <code>true</code> if passed String u matches a user's username in this user list.
     *             <code>false</code> otherwise.
     */
    public boolean containsUser(String u) {
        for(User listUser : this.users) {
            if(listUser.equals(u)) return true;
        }
        return false;
    }
}
