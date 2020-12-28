CREATE TABLE Prescription (
    prescriptionid INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    weight INT,
    allergies VARCHAR(500),
    medicine VARCHAR(500),
    patient_username VARCHAR(35),
    issuedate VARCHAR(20),
    FOREIGN KEY (patient_username) REFERENCES Users(username)    
)