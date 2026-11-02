/*Se crea la base de datos solicitada 2 ptos*/
DROP SCHEMA IF EXISTS caso01;
CREATE SCHEMA caso01;



/*Se crea un usuario para la base de datos 2 ptos*/
DROP USER IF EXISTS 'mateo31'@'%';
CREATE USER 'mateo31'@'%' IDENTIFIED BY '116270570';



/*Se asignan todos los privilegios sobre la base de datos solicitada al usuario creado 2 ptos*/
GRANT ALL PRIVILEGES ON caso01.* TO 'mateo31'@'%';
FLUSH PRIVILEGES;



/* Se crea la tabla solicitada 2 ptos*/
USE caso01;
CREATE TABLE pregunta (
  id_pregunta INT NOT NULL AUTO_INCREMENT,
  enunciado VARCHAR(40),
  tema INT,
  puntos_pregunta DOUBLE,
  imagen_pregunta VARCHAR(1024),
  PRIMARY KEY (id_pregunta)
);



/*Se insertan 4 registros en la tabla solicitada 2ptos */
INSERT INTO pregunta (id_pregunta, enunciado, tema, puntos_pregunta, imagen_pregunta) VALUES
(1, '¿Qué es una clase en Java?', 1, 10.0, 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97'),
(2, '¿Qué es el polimorfismo?', 1, 15.0, 'https://images.unsplash.com/photo-1555066931-4365d14bab8c'),
(3, 'Inyección de dependencias', 2, 20.0, 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5'),
(4, '¿Para qué sirve el patrón MVC?', 2, 12.5, 'https://images.unsplash.com/photo-1461749280684-dccba630e2f6');

