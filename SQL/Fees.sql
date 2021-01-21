create table "SMARTCARE".FEES
(
	feeID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
        	(START WITH 1, INCREMENT BY 1),
    	PRICE INT,
    	PERIOD INT,
	SERVICE_TYPE VARCHAR(100)
);

INSERT INTO FEES (PRICE, PERIOD, SERVICE_TYPE ) VALUES (40, 10, 'Surgery');
INSERT INTO FEES (PRICE, PERIOD, SERVICE_TYPE ) VALUES (20, 10, 'Consultation');