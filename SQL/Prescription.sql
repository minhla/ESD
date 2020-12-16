CREATE TABLE Prescription (
    prescriptionid INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    weight INT,
    allergies VARCHAR(500),
    medicine VARCHAR(500),
    patientid INT,
    issuedate VARCHAR(20),
    FOREIGN KEY (patientid) REFERENCES Users(uuid)    
)