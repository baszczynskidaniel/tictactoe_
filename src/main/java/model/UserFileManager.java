package model;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class UserFileManager {
    static public Map<String, String> getUsersFromFile(String filename) {
        Map<String, String> users = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object readMap = ois.readObject();
            if (readMap instanceof HashMap) {
                users.putAll((HashMap) readMap);
            }
        }

        catch (Exception e) {
            return new HashMap<>();
        }
        return users;
    }
    static public void deleteUser(String userName, String fileName) throws Exception {
        Map<String, String> users = getUsersFromFile(fileName);
        users.remove(userName);
        File usersFile = new File(fileName);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile));
            oos.writeObject(users);
            oos.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    static public boolean validateUser(String userName, String userPassword, String fileName) {
        Map<String, String>  users = getUsersFromFile(fileName);
        if(!users.containsKey(userName))
            return false;

        try {
            return PasswordSecure.validatePassword(userPassword, users.get(userName));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    static public void insertUser(String userName, String password, String fileName) throws Exception {
        Map<String, String> users = null;
        try {
            users =getUsersFromFile(fileName);
        } catch (Exception e) {
            users = new HashMap<>();
        }

        String encryptedPassword = PasswordSecure.generateStrongPasswordHash(password);
        users.put(userName, encryptedPassword);
        File usersFile = new File(fileName);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile));
            oos.writeObject(users);
            oos.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    static private boolean doesUserExists(String userName, Map<String, String> users) {
        return users.containsKey(userName);
    }
}
