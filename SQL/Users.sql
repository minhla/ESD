CREATE TABLE users (
    username VARCHAR(35) PRIMARY KEY,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    usertype VARCHAR(1) NOT NULL,
    dob DATE,
    regdate DATE,
    phone VARCHAR(11),
    email VARCHAR(64),
    address VARCHAR(70),
    password VARCHAR(30) NOT NULL
);


INSERT INTO USERS (username, firstname, lastname, usertype, dob, phone, email, address, password, regdate) VALUES ('m-tonkin', 'Michael', 'Tonkin', 'A', '1999-01-09', '999', 'michael@email.com', 'UWE', 'admin_password', '2020-12-20');
