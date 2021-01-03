
package smartcare.models.users;

public class User {
    
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String userType;

    public User() {
        
    }

    public User(String userID, String name, String lastname) {
        this.userID = userID;
        this.name = name;
        this.lastname = lastname;
    }
    
    public String getUserID() {
        return userID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUserType()
    {
        return userType;
    } 
    
    public void setUserType(String userType)
    {
        this.userType = userType;
    }
    
}   
