-- SQL tables
CREATE TABLE Movie(
id INTEGER PRIMARY KEY AUTO_INCREMENT,
title VARCHAR(40) NOT NULL,
movie_description VARCHAR(40) NOT NULL,
relese_date DATE,
profit_billion_$ DOUBLE NOT NULL,
director_id INTEGER NOT NULL,
CONSTRAINT foreign_key FOREIGN KEY (director_id) REFERENCES Director(id)
 );


CREATE TABLE Actor(
id INTEGER PRIMARY KEY AUTO_INCREMENT,
full_name VARCHAR(40)
);

CREATE TABLE Director(
id INTEGER PRIMARY KEY AUTO_INCREMENT,
full_name VARCHAR(40)
);

CREATE TABLE Movie_Actor(
id INTEGER PRIMARY KEY AUTO_INCREMENT,
movie_id INTEGER,
actor_id INTEGER,
CONSTRAINT foreign_key_1 FOREIGN KEY(movie_id) REFERENCES Movie(id),
CONSTRAINT foreign_key_2 FOREIGN KEY(actor_id) REFERENCES Actor(id)
);


-- Stored Procedures
CREATE PROCEDURE `getActorMovieAppearances`(IN actorName VARCHAR(40))
BEGIN
SELECT Actor.full_name, COUNT(movie_id) AS number_of_movies
FROM Actor INNER JOIN movie_actor ON  actor.id=movie_actor.actor_id
INNER JOIN movie ON movie.id=movie_actor.movie_id
WHERE actor.full_name LIKE CONCAT("%", actorName, "%");
END

CREATE PROCEDURE `getAllMoviesByActor` (IN actorName VARCHAR(40))
BEGIN
SELECT actor.full_name AS actor, movie.title AS movie
FROM ((movie_actor
INNER JOIN actor ON actor.id=movie_actor.actor_id)
INNER JOIN movie ON movie.id=movie_actor.movie_id)
WHERE actor.full_name LIKE CONCAT("%", actorName, "%");
END


CREATE PROCEDURE `getAllMoviesCountByDirector` (IN directorName VARCHAR(40))
BEGIN
SELECT director.full_name AS director, COUNT(director_id) AS number_of_movies
FROM Director INNER JOIN Movie ON director.id=movie.director_id
WHERE director.full_name LIKE CONCAT("%", directorName, "%");
END


CREATE PROCEDURE `getActorMovieAppCountByMovieDirector`(IN actorName VARCHAR(30), directorName VARCHAR(30))
BEGIN
SELECT actor.full_name AS actor, movie.title AS movie, director.full_name AS director, COUNT(movie_id) AS number_of_movies
FROM movie_actor
INNER JOIN actor ON actor.id=movie_actor.actor_id
INNER JOIN movie ON movie.id=movie_actor.movie_id
LEFT JOIN director ON director.id=movie.director_id
WHERE actor.full_name LIKE CONCAT("%", actorName, "%") and director.full_name LIKE CONCAT("%", directorName, "%");
END


CREATE PROCEDURE `getDirectorProfit`(IN directorName VARCHAR(30), startDate DATE, endDate DATE)
BEGIN
SELECT Director.full_name AS director, SUM(profit_billion_$)
FROM Director INNER JOIN Movie ON director.id=movie.director_id
WHERE director.full_name LIKE CONCAT("%", directorName, "%") AND
relese_date BETWEEN startDate AND endDate;
END