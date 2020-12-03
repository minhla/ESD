CREATE TABLE Users (
    uuid INT NOT NULL,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    usertype VARCHAR(1) NOT NULL,
    dob DATE,
    phone VARCHAR(11),
    email VARCHAR(64),
    address VARCHAR(70),
    PRIMARY KEY (uuid)
)

INSERT INTO YODA.USERS (UUID, FIRSTNAME, LASTNAME, USERTYPE, DOB, PHONE, EMAIL, ADDRESS) 
	VALUES (1, 'Michael', 'Tonkin', 'A', '2020-12-01', '999', 'Michael2.Tonkin@live.uwe.ac.uk', 'Frenchay Campus, Coldharbour Ln, Bristol BS16 1QY')