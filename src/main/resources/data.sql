-- Insert authors
INSERT INTO authors (id, name, age, followers_number) VALUES
(100000, 'J.K. Rowling', 58, 100),
(100001, 'George R.R. Martin', 75, 50000),
(100002, 'Yuval Noah Harari', 47, 120000);

-- Insert books
INSERT INTO books (id, title, author_id, publication_date, type) VALUES
(100000, 'Harry Potter and the Philosophers Stone', 100000, '1997-06-26', 'Fantasy'),
(100001, 'A Game of Thrones', 100001, '1996-08-06', 'Fantasy'),
(100002, 'Sapiens: A Brief History of Humankind', 100002, '2011-01-01', 'History'),
(100003, 'Harry Potter and the Chamber of Secrets', 100000, '1998-07-02', 'Fantasy'),
(100004, 'Fire & Blood', 100001, '2018-11-20', 'Fantasy');