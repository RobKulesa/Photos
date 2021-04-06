package photos.structures;

import java.io.*;
import java.util.*;


public class UserList implements Serializable{

    private static final long serialVersionUID = 1L;

    private ArrayList<User> users;

    public static final String storeDir = "src/resources/bin";
    public static final String storeFile = "users.dat";
    
    public UserList(){
        this.users = new ArrayList<User>();
    }

    public UserList(ArrayList<User> users) {
        this.users = users;
    }

    public void addUser(User u){users.add(u);}
    
    public int getLength(){
        return users.size();
    }
    
    public ArrayList<User> getUsers() {
        return this.users;
    }
    
    public User getUser(int index){
        return users.get(index);
    }

    public static void writeUserList(UserList userList) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(userList);
        oos.close();
    }

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
        
        if(userList.getLength() == 0) {
            userList.addUser(new User("admin"));
        }
        
        return userList;
    }

    @Override
    public String toString() {
        return users.toString();
    }
    
}
