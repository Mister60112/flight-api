-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database

INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'Quentin', 'Boyeldieu', 'quentin@gmail.fr');
INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'Guillaume', 'Rohee', 'guigui@gmail.com');

-- Inserting a passenger from Barcelona
INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'GARCIA', 'Maria', 'maria.garcia@gmail.com');

-- Inserting a passenger from Rome
INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'ROSSI', 'Luca', 'luca.rossi@yahoo.com');

-- Inserting a passenger from Paris
INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'LECLERC', 'Sophie', 'sophie.leclerc@hotmail.com');

-- Inserting a passenger from Berlin
INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'MÜLLER', 'Hans', 'hans.mueller@web.de');
