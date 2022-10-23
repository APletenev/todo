CREATE TABLE users (
                       username varchar(15),
                       password varchar(100),
                       enabled smallint,
                       PRIMARY KEY (username)
) ;

CREATE TABLE authorities (
                             username varchar(15),
                             authority varchar(25),
                             FOREIGN KEY (username) references users(username)
) ;