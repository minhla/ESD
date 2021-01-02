CREATE TABLE Appointments (
    appointmentid INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    appointmentDate DATE,
    starttime TIME,
    endtime TIME,
    comment VARCHAR(50),
    doctor_username VARCHAR(35),
    patient_username VARCHAR(35),
    FOREIGN KEY (locationid) REFERENCES Locations(locationID),
    FOREIGN KEY (doctor_username) REFERENCES Users(username),
    FOREIGN KEY (patient_username) REFERENCES Users(username)
)