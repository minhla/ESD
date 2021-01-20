CREATE TABLE invoice(
    invoiceid INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    servicetype VARCHAR(100),
    detail VARCHAR(500),
    amount INT,
    patient_username VARCHAR(35),
    issuedate VARCHAR(20),
    weeknum INT,
    paymenttype VARCHAR(100),
    paid CHAR(1),
    FOREIGN KEY (patient_username) REFERENCES Users(username) ON DELETE CASCADE
)
