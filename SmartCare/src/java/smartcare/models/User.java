
package smartcare.models;

public class User {
    
    private String name;
    private String username;
    private String password;
    private String email;
    private String userType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
