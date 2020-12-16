CREATE TABLE invoice(
    invoiceid INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    servicetype VARCHAR(100),
    detail VARCHAR(500),
    amount INT,
    patientid INT,
    issuedate VARCHAR(20),
    weeknum INT,
    paymenttype VARCHAR(100),
    FOREIGN KEY (patientid) REFERENCES Users(uuid)    
)