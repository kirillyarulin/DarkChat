CREATE TABLE users (
  user_id   BIGSERIAL PRIMARY KEY,
  username CHARACTER VARYING(32) NOT NULL UNIQUE CHECK (USERS.username ~ ('^[A-Za-z][A-Za-z0-9_\-.]{2,}$')),
  password CHARACTER VARYING(60) NOT NULL CHECK (length(PASSWORD) > 6),
  is_male BOOLEAN DEFAULT TRUE,
  photo text
);

CREATE TABLE CHATS (
  chat_id BIGSERIAL PRIMARY KEY,
  time_of_creation TIMESTAMP NOT NULL,
  is_read    BOOLEAN DEFAULT FALSE
);

CREATE TABLE GROUP_CHATS (
  chat_id   INTEGER REFERENCES CHATS (chat_id) PRIMARY KEY,
  chat_name CHARACTER VARYING(32) NOT NULL CHECK (GROUP_CHATS.chat_name ~ ('^[A-Za-zА-Яа-я][A-Za-zА-Яа-я0-9_\-.]{2,}$'))
);


CREATE TABLE MESSAGES (
  message_id BIGSERIAL PRIMARY KEY,
  sender_id  INTEGER REFERENCES USERS (user_id),
  chat_id    INTEGER REFERENCES CHATS (chat_id),
  content   CHARACTER VARYING NOT NULL,
  time      TIMESTAMP WITH TIME ZONE NOT NULL ,
  is_read    BOOLEAN DEFAULT FALSE
);

CREATE TABLE CHAT_PARTICIPANTS (
  chat_id INTEGER REFERENCES CHATS (chat_id),
  user_id INTEGER REFERENCES USERS (user_id),
  PRIMARY KEY (chat_id, user_id)
);
