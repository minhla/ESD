CREATE TABLE users (
    uuid INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    usertype VARCHAR(1) NOT NULL,
    dob DATE,
    phone VARCHAR(11),
    email VARCHAR(64) NOT NULL,
    address VARCHAR(70),
    password VARCHAR(30) NOT NULL
);


INSERT INTO USERS (firstname, lastname, usertype, dob, phone, email, address, password) VALUES ('Michael', 'Tonkin', 'A', '09/01/1999', '999', 'michael@email.com', 'UWE', 'admin_password');
INSERT INTO USERS (firstname, lastname, usertype, dob, phone, email, address, password) VALUES ('Asia', 'Benyadilok', 'P', '09/01/2015', '911', 'bas@email.com', 'Thailand?', 'bas');
INSERT INTO USERS (firstname, lastname, usertype, dob, phone, email, address, password) VALUES ('Mihn', 'Leeee', 'N', '01/01/2015', '000', 'mihn@email.com', 'Other Thailand', 'pass');
INSERT INTO USERS (firstname, lastname, usertype, dob, phone, email, address, password) VALUES ('David', 'Giac', 'N', '02/01/2016', '666', 'daveandgiac@email.com', 'Mediterranean', 'pass123');

INSERT INTO USERS (firstname, lastname, usertype, dob, phone, email, address, password) VALUES ('Admin', 'Albedo', 'A', '09/01/1980', '999', 'admin@gmail.com', 'UWE', 'admin');
INSERT INTO USERS (firstname, lastname, usertype, dob, phone, email, address, password) VALUES ('Doctor', 'David', 'D', '12/06/1980', '999', 'doctor@gmail.com', 'UWE', 'doctor');
INSERT INTO USERS (firstname, lastname, usertype, dob, phone, email, address, password) VALUES ('Patient', 'Peter', 'P', '06/03/1995', '999', 'patient@gmail.com', 'UWE', 'patient');
INSERT INTO USERS (firstname, lastname, usertype, dob, phone, email, address, password) VALUES ('Nurse', 'Nemo', 'N', '04/03/1993', '999', 'nurse@gmail.com', 'UWE', 'nurse');