INSERT INTO USER VALUES(1,'lfryc@redhat.com')
INSERT INTO USER VALUES(2,'ozizka@redhat.com')
INSERT INTO USER VALUES(3,'mvecera@redhat.com')

INSERT INTO AUCTION VALUES(1,'Zde se bude dražit o Pět Švestek','Brno','Pět Švestek',164,NULL,1)
INSERT INTO AUCTION VALUES(2,'Zde se bude dražit o velorex...','Brno','Velorex',183,NULL,1)
INSERT INTO AUCTION VALUES(3,'Zde se bude dražit o tajemný klíč...','Brno','Tajemný klíč',73,NULL,1)
INSERT INTO AUCTION VALUES(4,'Zde se bude dražit o prkenná ohrada...','Brno','Prkenná ohrada',180,NULL,1)
INSERT INTO AUCTION VALUES(5,'Zde se bude dražit o průvodce pokojových plodin...','Brno','Průvodce pokojových plodin',166,NULL,1)
INSERT INTO AUCTION VALUES(6,'Zde se bude dražit o socové kafe...','Brno','Socové kafe',34,NULL,1)
INSERT INTO AUCTION VALUES(7,'Zde se bude dražit o staré kolo...','Brno','Staré kolo',44,NULL,1)
INSERT INTO AUCTION VALUES(8,'Zde se bude dražit o příručka mladých svišťů...','Brno','Příručka mladých svišťů',197,NULL,3)
INSERT INTO AUCTION VALUES(9,'Zde se bude dražit o zámek...','Brno','Zámek',72,NULL,3)
INSERT INTO AUCTION VALUES(10,'Zde se bude dražit o olejnička...','Brno','Olejnička',26,NULL,3)
INSERT INTO AUCTION VALUES(11,'Zde se bude dražit o osmisměrky...','Brno','Osmisměrky',181,NULL,2)
INSERT INTO AUCTION VALUES(12,'Zde se bude dražit o vodováha...','Brno','Vodováha',68,NULL,2)
INSERT INTO AUCTION VALUES(13,'Zde se bude dražit o neznámá hmota...','Brno','Neznámá hmota',179,NULL,2)
INSERT INTO AUCTION VALUES(14,'Zde se bude dražit o past na myši...','Brno','Past na myši',207,NULL,2)
INSERT INTO AUCTION VALUES(15,'Zde se bude dražit o nabíjecí baterie...','Brno','Nabíjecí baterie',139,NULL,2)
INSERT INTO AUCTION VALUES(16,'Zde se bude dražit o inkoustové náplně...','Brno','Inkoustové náplně',117,NULL,2)
INSERT INTO AUCTION VALUES(17,'Zde se bude dražit o violoncello...','Brno','Violoncello',146,NULL,2)
INSERT INTO AUCTION VALUES(18,'Zde se bude dražit o hadí akvárium...','Brno','Hadí akvárium',54,NULL,2)
INSERT INTO AUCTION VALUES(19,'Zde se bude dražit o dvojdílné trámoví...','Brno','Dvojdílné trámoví',23,NULL,2)
INSERT INTO AUCTION VALUES(20,'Zde se bude dražit o leporelo...','Brno','Leporelo',106,NULL,3)
INSERT INTO AUCTION VALUES(21,'Zde se bude dražit o rohlík za odvoz...','Brno','Rohlík za odvoz',80,NULL,3)
INSERT INTO AUCTION VALUES(22,'Zde se bude dražit o dětská sedačka...','Brno','Dětská sedačka',92,NULL,3)
INSERT INTO AUCTION VALUES(23,'Zde se bude dražit o plastová víčka...','Brno','Plastová víčka',212,NULL,3)
INSERT INTO AUCTION VALUES(24,'Zde se bude dražit o mycí houba...','Brno','Mycí houba',137,NULL,3)
INSERT INTO AUCTION VALUES(25,'Zde se bude dražit o psí bouda...','Brno','Psí bouda',75,NULL,3)

INSERT INTO BID VALUES(1,456,2,2)
INSERT INTO BID VALUES(2,274,2,2)
INSERT INTO BID VALUES(3,365,2,3)
INSERT INTO BID VALUES(4,145,3,3)
INSERT INTO BID VALUES(5,97,3,3)
INSERT INTO BID VALUES(6,121,3,2)
INSERT INTO BID VALUES(7,221,5,3)
INSERT INTO BID VALUES(8,295,8,2)
INSERT INTO BID VALUES(9,246,8,1)
INSERT INTO BID VALUES(10,144,9,2)
INSERT INTO BID VALUES(11,108,9,1)
INSERT INTO BID VALUES(12,32,10,1)
INSERT INTO BID VALUES(13,413,14,3)
INSERT INTO BID VALUES(14,310,14,1)
INSERT INTO BID VALUES(15,182,17,1)
INSERT INTO BID VALUES(16,162,18,1)
INSERT INTO BID VALUES(17,81,18,3)
INSERT INTO BID VALUES(18,108,18,1)
INSERT INTO BID VALUES(19,135,18,3)
INSERT INTO BID VALUES(20,141,20,2)

UPDATE AUCTION SET HIGHESTBID_ID=1 WHERE ID=2;
UPDATE AUCTION SET HIGHESTBID_ID=4 WHERE ID=3;
UPDATE AUCTION SET HIGHESTBID_ID=7 WHERE ID=5;
UPDATE AUCTION SET HIGHESTBID_ID=8 WHERE ID=8;
UPDATE AUCTION SET HIGHESTBID_ID=10 WHERE ID=9;
UPDATE AUCTION SET HIGHESTBID_ID=12 WHERE ID=10;
UPDATE AUCTION SET HIGHESTBID_ID=13 WHERE ID=14;
UPDATE AUCTION SET HIGHESTBID_ID=15 WHERE ID=17;
UPDATE AUCTION SET HIGHESTBID_ID=16 WHERE ID=18;
UPDATE AUCTION SET HIGHESTBID_ID=20 WHERE ID=20;