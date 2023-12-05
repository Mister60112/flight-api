-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database

INSERT INTO reservations(id, flightId, passengerId) VALUES (NEXTVAL('reservations_sequence'), 1, 1);
INSERT INTO reservations(id, flightId, passengerId) VALUES (NEXTVAL('reservations_sequence'), 1, 2);
INSERT INTO reservations(id, flightId, passengerId) VALUES (NEXTVAL('reservations_sequence'), 2, 2);

