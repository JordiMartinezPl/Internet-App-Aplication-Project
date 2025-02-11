-- Insert roles
INSERT INTO role (enumid, name)
VALUES (1, 'ADMIN'),
       (2, 'PREMIUM'),
       (3, 'FREE');

-- Insert users password1
INSERT INTO collection_user (name, second_name, email, password, date_of_birth, date_of_registration, role_id)
VALUES ('John', 'Doe', 'john.doe1@example.com', '$2a$10$02KoAO/ZhbNp7y/FEft91OgYc405goo/aRIejYIhKrCjjLTFz789y',
        '1990-01-01', '2023-10-01', 1),--password= password1
       ('Jane', 'Smith', 'jane.smith2@example.com', '$2a$10$vNR2o.bnBpUcu04g8ofh6OhqYCXc/0YdplXfpz4B659kWo.mLp4x.',
        '1992-02-02', '2023-10-02', 2),
       ('Robert', 'Brown', 'robert.brown3@example.com', '$2a$10$n6kLO/E0MMXPfLIXq6zl0uZ7mpCbZedOWBmRAWwXSb5oLSEtTr7fG',
        '1988-03-03', '2023-10-03', 3),
       ('Emily', 'Davis', 'emily.davis4@example.com', '$2a$10$bt0CA6ykdOQ2zm3HBFsKweMyNi3PqB3PM.tsTyIMMMmwIC2/aFGPC',
        '1991-04-04', '2023-10-04', 1),
       ('Michael', 'Wilson', 'michael.wilson5@example.com',
        '$2a$10$IVF2Ms63Fso/PZo12HrgEewvakHhZ3wFFhGH1KPOYi4bCaWNegsCa', '1993-05-05', '2023-10-05', 2),
       ('Sarah', 'Taylor', 'sarah.taylor6@example.com', '$2a$10$/8BKWy71XsG1UuhMAUEEy.2UxcQhKLPO3nEN8/LXrniG4gJvRIG8G',
        '1990-06-06', '2023-10-06', 3),
       ('David', 'Anderson', 'david.anderson7@example.com',
        '$2a$10$gMBuY0.fB/tkWTO6ZKpdNOIRtIYlO42zyW7g9ElhcXhbhWKZoEYS.', '1987-07-07', '2023-10-07', 1),
       ('Laura', 'Thomas', 'laura.thomas8@example.com', '$2a$10$UOr9l8vpzq/VLs.matBFUuAkFiVvkHzCwLdCQUq51EM585bMI87ai',
        '1994-08-08', '2023-10-08', 2),
       ('James', 'Jackson', 'james.jackson9@example.com',
        '$2a$10$cNYe4CMCvxCCiqiPeSZhDOqp6B0Xf6cUQor.M5Qyilb7r7uZpnzPS', '1992-09-09', '2023-10-09', 3),
       ('Emma', 'White', 'emma.white10@example.com', '$2a$10$MaQRwHxtSzs0l4J6eIVjQehgj1K/m78dxtbAIh6u.JLHuLyPcgtSS',
        '1989-10-10', '2023-10-10', 1),
       ('Olivia', 'Harris', 'olivia.harris11@example.com',
        '$2a$10$iqIBfIg/U2y0lcVz9yDPUejeQJiGu0yNlXu42FRsCouR5nC9L7NKG', '1991-11-11', '2023-10-11', 2),
       ('Lucas', 'Martin', 'lucas.martin12@example.com', '$2a$10$55h0N3bcCe0WSzcjqwO1O.sGEcRj4tDSa.RqUZaH58neA6wklFAfK',
        '1988-12-12', '2023-10-12', 3),
       ('Sophia', 'Lee', 'sophia.lee13@example.com', '$2a$10$2XSWVQWi0xe7AS5wdMxhG.iq1JdjpWg/Rmz.fwoTNQxC74slYfsB6',
        '1990-01-13', '2023-10-13', 1),
       ('Ethan', 'Perez', 'ethan.perez14@example.com', '$2a$10$FBtRTHgjJSOc5HfiZJNMi.HSgTJZfL.AfKH3OguOjQDTDiUNWHMim',
        '1993-02-14', '2023-10-14', 2),
       ('Isabella', 'Gonzalez', 'isabella.gonzalez15@example.com',
        '$2a$10$OZub4o/T3rYvKkSlJIA6KeaZq6wCIsljH9MJDDGEoHaK0PReuX9Pq', '1989-03-15', '2023-10-15', 3),
       ('Mason', 'Rodriguez', 'mason.rodriguez16@example.com',
        '$2a$10$XJOvrYW7CailVHJ.PFTVeuzF/MpW1dRdOfp2rtb6JkZanwNgCHyHi', '1992-04-16', '2023-10-16', 1),
       ('Ava', 'Martinez', 'ava.martinez17@example.com', '$2a$10$eNqGjB9Gq4MFuJgnUrmz7OcLz5RT4/DExVCyXem.4HceaQlXHPZO2',
        '1987-05-17', '2023-10-17', 2),
       ('Logan', 'Robinson', 'logan.robinson18@example.com',
        '$2a$10$7P43t9cduPc.VS0C94mBIe2ZVnDv5bCS6n96TxGZGUbERvhTU3kv.', '1994-06-18', '2023-10-18', 3),
       ('Mia', 'Clark', 'mia.clark19@example.com', '$2a$10$QrBPQ2/n.el.M/6rKQfDIOk2N7vGsue94lATzK5rnrAR/selJisay',
        '1990-07-19', '2023-10-19', 1),
       ('Benjamin', 'Lewis', 'benjamin.lewis20@example.com',
        '$2a$10$vMO6a2b90WVSpc1XMIu5U.gBexZ.PXLIfaFLw4aPteqDUJ0PLSdBq', '1991-08-20', '2023-10-20', 2);


-- Insert albums
INSERT INTO album (title, description, public_availability, editor, forum, begin_date, ending_date)
VALUES ('Album Pokemon', 'Descripción del Album Pokemon', TRUE, 1, NULL, '2024-01-01', '2025-01-01'),
       ('Album 2', 'Descripción del Album 2', TRUE, 1, NULL, '2024-01-01', '2025-01-01'),
       ('Album Dragon Ball', 'Álbum dedicado a los personajes de Dragon Ball', TRUE, 1, NULL, '2023-01-01',
        '2024-01-01');;

-- Insert album sections
INSERT INTO album_section (album, title, description)
VALUES
    -- Sections Album Pokemon
    (1, 'Tipo Fuego', 'Descripción de la seccion Tipo Fuego'),
    (1, 'Tipo Agua', 'Descripción de la seccion Tipo Agua'),
    (1, 'Tipo Planta', 'Descripcion de la seccion Tipo Planta'),
    -- Sections Album 2
    (2, 'Sección 1 del Album 2', 'Descripción de la Sección 1 VACIO'),
    -- Pegatinas para la sección Tipo Planta (section 3)
    (3, 'Saiyans', 'Sección dedicada a los Saiyans'),
    (3, 'Namekians', 'Sección dedicada a los Namekians'),
    (3, 'Villains', 'Sección dedicada a los villanos de Dragon Ball');

-- Insert stickers
INSERT INTO sticker (sectionid, number_in_album, name, description, imageurl, type_of_sticker)
VALUES
    -- Pegatinas para la sección Tipo Fuego (section 1)
    (1, 1, 'Charmander', 'Pokémon de tipo fuego inicial de la región Kanto', 'url_charmander', 'Tipo Fuego'),
    (1, 2, 'Vulpix', 'Pokémon de tipo fuego con múltiples colas', 'url_vulpix', 'Tipo Fuego'),
    (1, 3, 'Growlithe', 'Pokémon de tipo fuego conocido por su lealtad', 'url_growlithe', 'Tipo Fuego'),
    (1, 4, 'Magmar', 'Pokémon de tipo fuego con aspecto volcánico', 'url_magmar', 'Tipo Fuego'),

    -- Pegatinas para la sección Tipo Agua (section 2)
    (2, 5, 'Squirtle', 'Pokémon de tipo agua inicial de la región Kanto', 'url_squirtle', 'Tipo Agua'),
    (2, 6, 'Psyduck', 'Pokémon de tipo agua conocido por sus dolores de cabeza', 'url_psyduck', 'Tipo Agua'),
    (2, 7, 'Poliwag', 'Pokémon de tipo agua con un espiral en su estómago', 'url_poliwag', 'Tipo Agua'),
    (2, 8, 'Lapras', 'Pokémon de tipo agua que ayuda a cruzar el océano', 'url_lapras', 'Tipo Agua'),

    -- Pegatinas para la sección Tipo Planta (section 3)
    (3, 9, 'Bulbasaur', 'Pokémon de tipo planta inicial de la región Kanto', 'url_bulbasaur', 'Tipo Planta'),
    (3, 10, 'Oddish', 'Pokémon de tipo planta con hojas en la cabeza', 'url_oddish', 'Tipo Planta'),
    (3, 11, 'Bellsprout', 'Pokémon de tipo planta con cuerpo de tallo', 'url_bellsprout', 'Tipo Planta'),
    (3, 12, 'Tangela', 'Pokémon de tipo planta cubierto de enredaderas', 'url_tangela', 'Tipo Planta'),

    -- Pegatinas para la sección Tipo Saiyans (section 1)
    (5, 1, 'Goku', 'Saiyan protagonista de la saga Dragon Ball', 'url_goku', 'Saiyan'),
    (5, 2, 'Vegeta', 'Príncipe de los Saiyans y rival de Goku', 'url_vegeta', 'Saiyan'),
    (5, 3, 'Gohan', 'Hijo de Goku, un poderoso Saiyan híbrido', 'url_gohan', 'Saiyan'),
    (5, 4, 'Trunks', 'Saiyan del futuro hijo de Vegeta y Bulma', 'url_trunks', 'Saiyan'),

    -- Stickers for Namekians section 2
    (6, 5, 'Piccolo', 'Namekiano y mentor de Gohan', 'url_piccolo', 'Namekian'),
    (6, 6, 'Kami', 'Dios guardián de la Tierra, Namekiano', 'url_kami', 'Namekian'),
    (6, 7, 'Dende', 'Namekiano encargado de las esferas del dragón', 'url_dende', 'Namekian'),

    -- Stickers for Villains section 3
    (7, 8, 'Frieza', 'Tirano galáctico y enemigo de Goku', 'url_frieza', 'Villain'),
    (7, 9, 'Cell', 'Bioandroide que busca la perfección', 'url_cell', 'Villain'),
    (7, 10, 'Majin Buu', 'Entidad mágica con múltiples formas', 'url_buu', 'Villain');
;

-- Insert collection albums and stickers
INSERT INTO collection_album (owner, album, public_availability)
VALUES (1, 1, TRUE),--1
       (1, 2, TRUE),--2
       (2, 1, TRUE),--3
       (2, 2, TRUE),--4
       (3, 1, TRUE),--5
       (3, 2, TRUE),--6
       (1, 3, TRUE),--7
       (2, 3, TRUE),--8
       (3, 3, TRUE),--9
       (4, 1, TRUE),--10
       (5, 1, TRUE),--11
       (7, 1, TRUE);--12


INSERT INTO collection_sticker (sticker, quantity, collection_album_id,blocked_copies)
VALUES (1, 5, 1,0),   --collectionAlbum 1  (1,4,7,8,12)   / (2,4,8,5,11)  /(2,3,8,7)/ (3,8,7)  / ((6) / (11)
       (4, 1, 1,0),   --collectionAlbum 1          1-2 (1, 7 ,12 , 2 ,5 )          1-3 (1, 12,3)    (1, 12, 3) / (1,7,12, 6)  /(1,7,12, 11)
       (7, 4, 1,0),   --collectionAlbum 1
       (8, 1, 1,0),   --collectionAlbum 1
       (12, 7, 1,0),  --collectionAlbum 1
       (2, 3, 3,1),   --collectionAlbum 3
       (4, 1, 3,0),   --collectionAlbum 3
       (8, 2, 3,0),   --collectionAlbum 3
       (5, 2, 3,1),   --collectionAlbum 3
       (11, 1, 3,0),  --collectionAlbum 3
       (2, 1, 5,0),   --collectionAlbum 5
       (3, 4, 5,0),   --collectionAlbum 3
       (8, 1, 5,0),   --collectionAlbum 3
       (7, 3, 5,0),   --collectionAlbum 5
       (9, 10, 10,0), --collectionAlbum 10
       (8, 12, 10,0), --collectionAlbum 10
       (6, 4, 10,0),  --collectionAlbum 10
       (6, 1, 11, 0), --collectionAlbum 11
       (11, 8, 12,0); --collectionAlbum 12

-- Insert forums
-- Insert forums for existing albums
INSERT INTO forum (forumid)
VALUES (DEFAULT),
       (DEFAULT),
       (DEFAULT);

-- Update albums to link them with the forums
UPDATE album
SET forum = 1
WHERE album_id = 1;

UPDATE album
SET forum = 2
WHERE album_id = 2;
UPDATE album
SET forum = 3
WHERE album_id = 3;

-- Insert forum messages
INSERT INTO forum_message (forum, reply_to, send_date, sender, body)
VALUES (1, NULL, '2024-2-15 10:00:00', 1, 'Let go!'),
       (1, NULL, '2024-9-15 10:00:00', 1, 'Attack!'),
       (1, NULL, '2024-11-15 10:00:00', 1, 'Welcome to the Album Pokemon Forum!'),
       (1, NULL, '2024-11-15 10:05:00', 2, 'Looking forward to trading Pokemon stickers!'),
       (1, 1, '2024-11-15 10:10:00', 3, 'Thanks for the welcome! Let’s trade.'),
       (2, NULL, '2024-11-15 10:20:00', 4, 'Welcome to the Album 2 Forum!'),
       (3, NULL, '2024-11-15 10:00:00', 1, 'Welcome to the Dragon Ball Forum!'),
       (3, NULL, '2024-11-15 10:05:00', 2, 'Excited to collect all Saiyan stickers!'),
       (3, 1, '2024-11-15 10:10:00', 3, 'Let’s trade stickers and complete our albums!');
;

-- Insert private messages
INSERT INTO private_message (read, receiver, reply_to, send_date, sender, body, one_time_message, forum_id)
VALUES (FALSE, 2, NULL, '2024-11-15 09:00:00', 1, 'Hey, are you interested in trading stickers?', FALSE, 1),
       (FALSE, 1, 1, '2024-11-15 09:10:00', 2, 'Sure! Let me know what you need.', FALSE, 1),
       (TRUE, 3, NULL, '2024-11-15 09:15:00', 1, 'Can you help me find the fire-type Pokemon stickers?', FALSE, 1),
       (TRUE, 1, 3, '2024-11-15 09:20:00', 3, 'Of course! I have some extras.', FALSE, 3),
       (FALSE, 2, NULL, '2024-11-15 09:20:10', 1, 'OneTimeMessage', TRUE, 3);

INSERT INTO user_forum (collection_userid, forumid)
VALUES (1, 1), -- John Doe suscrito al forum del Album 1
       (2, 1), -- Jane Smith suscrito al forum del Album 1
       (3, 1), -- Robert Brown suscrito al forum del Album 1
       (4, 2), -- Emily Davis suscrito al forum del Album 2
       (5, 2), -- Michael Wilson suscrito al forum del Album 2
       (6, 2), -- Sarah Taylor suscrito al forum del Album 2
       (1, 3), -- John Doe suscrito al forum del Album Dragon Ball
       (2, 3), -- Jane Smith suscrito al forum del Album Dragon Ball
       (3, 3); -- Robert Brown suscrito al forum del Album Dragon Ball

INSERT INTO sale_sticker (collection_sticker_id, price, seller_collection_userid, sold)
VALUES (1, 100, 1, false);
INSERT INTO forum_message_read_by (read_forum_messageid, read_collection_userid)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 3),
       (3, 1);

-- Insert exchange_sticker (revisado para tener sentido)
INSERT INTO exchange_sticker (owner_id, interested_id, status)
VALUES
    -- Usuario 2 está interesado en el sticker 1 (Charmander) del Usuario 1
    (1, 2, 'PENDING'),

    -- Usuario 3 está interesado en el sticker 2 (Vulpix) del Usuario 2
    (2, 3, 'PENDING'),

    -- Usuario 1 está interesado en el sticker 5 (Squirtle) del Usuario 3
    (3, 1, 'PENDING'),

    -- Usuario 2 está interesado en los stickers 1,7 del Usuario 1 a cambio de 2,5
    (1, 2, 'PENDING');

UPDATE exchange_sticker
SET proposal_initiation_date = DATEADD('MINUTE',-4,NOW())
WHERE id = 4;

INSERT INTO STICKER_INTERESTED_ID (exchange_sticker_ID, STICKERS_FROM_OWNER)
VALUES (1, 1),
       (2, 2),
       (3, 5),
       (4, 1),
       (4, 7);
INSERT INTO STICKER_EXCHANGING_TO_OWNER (exchange_sticker_ID, STICKERS_FROM_INTERESTED)
VALUES (4, 2),
       (4, 5);