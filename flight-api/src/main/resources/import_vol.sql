-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database

INSERT INTO flights(id, number, origin, destination, departureDate, departureTime, arrivalDate, arrivalTime, planeId) VALUES(NEXTVAL('flights_sequence'), 'A-000000', 'Paris', 'Beauvais',  '2022-12-03',  '18:00:00',  '2022-12-03',  '20:00:00', 1);
INSERT INTO flights(id, number, origin, destination, departureDate, departureTime, arrivalDate, arrivalTime, planeId) VALUES(NEXTVAL('flights_sequence'), 'B-000000', 'Paris', 'Madrid',  '2022-11-01',  '19:00:00',  '2022-12-01', '22:00:00', 2);

