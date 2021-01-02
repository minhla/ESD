CREATE TABLE Appointments (
    appointmentid INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    appointmentDate DATE,
    starttime TIME,
    endtime TIME,
    comment VARCHAR(50),
    doctorid INT,
    patientid INT,
    locationid INT,
    FOREIGN KEY (locationid) REFERENCES Locations(locationID),
    FOREIGN KEY (doctorid) REFERENCES Users(uuid),
    FOREIGN KEY (patientid) REFERENCES Users(uuid)
)