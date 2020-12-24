package smartcare.models;

public class Nurse {
    private String nurseID;
    private String name;
    private String username;
    private String password;

    public Nurse(String nurseID, String name, String username, String password) {
        this.nurseID = nurseID;
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    public class Singleton{)
    ArrayList<Nurse> nurses = new ArrayList<Nurse>();
    For each nurse in db
            nurses.add(New Nurse(blah blah blah))
    }
    
    public Nurse()
    {
        set....
        if db.hasNurseId() {
          Jdbc jdbc = new JDbc();
                jdbc.exedcute_stmt(sql_stmtd
        } else  {
            make a new nurse object
            get nurse from db
        }
    }
    
    private void addNursetoDB(params...) {
        if !db.hasNurseId(nurse_id) {
          Jdbc jdbc = new JDbc();
          jdbc.exedcute_stmt(params...);
        }
    }
    
    public String sql_stmt()
    {
        get ...name       
    }
    
    
    public String getNurseID() {
        return nurseID;
    }

    public void setNurseID(String nurseID) {
        this.nurseID = nurseID;
    }

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
    
    
}
