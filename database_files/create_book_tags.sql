drop table if exists book_tags;

create table book_tags (
  isbn_13 varchar (13) REFERENCES books(isbn_13),
  tag_name varchar (100),
  PRIMARY KEY(isbn_13, tag_name)
);

insert into book_tags values (
  '1111111111111',         
  'Adventures'
);