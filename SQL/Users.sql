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
