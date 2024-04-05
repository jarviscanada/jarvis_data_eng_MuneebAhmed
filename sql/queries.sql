-- Question 1
-- Insert some data into a table
INSERT INTO cd.facilities
VALUES(9, 'Spa', 20, 30, 100000, 800);

-- Question 2
-- Insert calculated data into a table
INSERT INTO cd.facilities
VALUES((SELECT max(facid) from cd.facilities) + 1,'Spa', 20, 30, 100000, 800);

-- Question 3
-- Update some existing data
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name LIKE 'Tennis Court 2';

-- Question 4
-- Update a row based on the contents of another row
UPDATE cd.facilities
SET 
	membercost = membercost + (SELECT membercost FROM cd.facilities WHERE name LIKE 'Tennis Court 1') * 0.1,
	guestcost = guestcost + (SELECT guestcost FROM cd.facilities WHERE name LIKE 'Tennis Court 1') * 0.1
WHERE name LIKE 'Tennis Court 2';

-- Question 5
-- Delete all bookings
DELETE FROM cd.bookings;

-- Question 6
-- Delete a member from the cd.members table
DELETE FROM cd.members
WHERE memid = 37;

-- Question 7
-- Control which rows are retrieved - part 2
SELECT facid, name, membercost, monthlymaintenance FROM cd.facilities
WHERE membercost < monthlymaintenance * 0.02 AND membercost > 0;

-- Question 8
-- Basic string searches
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';

-- Question 9
-- Matching against multiple possible values
SELECT * FROM cd.facilities
WHERE facid IN (1, 5);

-- Question 10
-- Working with dates
SELECT memid, surname, firstname, joindate from cd.members
WHERE joindate >= '2012-09-01';

-- Question 11
-- Combining results from multiple queries
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;

-- Question 12
-- Retrieve the start times of members' bookings
SELECT starttime FROM cd.bookings
INNER JOIN cd.members
ON cd.members.memid = cd.bookings.memid
WHERE cd.members.firstname LIKE 'David' AND cd.members.surname LIKE 'Farrell';

-- Question 13
-- Work out the start times of bookings for tennis courts
SELECT bks.starttime as start, facs.name as name FROM cd.facilities facs
INNER JOIN cd.bookings bks
ON bks.facid = facs.facid
WHERE facs.name LIKE 'Tennis%'
AND 
bks.starttime >= '2012-09-21' AND bks.starttime < '2012-09-22'
ORDER BY bks.starttime;

-- Question 14
-- Produce a list of all members, along with their recommender
SELECT mems.firstname as memfname, mems.surname as memsname, recs.firstname as recfname, recs.surname as recsname 
FROM cd.members mems
LEFT OUTER JOIN cd.members recs
ON recs.memid = mems.recommendedby
ORDER BY memsname, memfname;

-- Question 15
-- Produce a list of all members who have recommended another member
SELECT DISTINCT recs.firstname as firstname, recs.surname as surname FROM cd.members mems
INNER JOIN cd.members recs
ON recs.memid = mems.recommendedby
ORDER BY recs.surname, recs.firstname;

-- Question 16
-- Produce a list of all members, along with their recommender, using no joins.
SELECT DISTINCT mems.firstname || ' ' ||  mems.surname as member, 
	(SELECT recs.firstname || ' ' || recs.surname as recommender
	 	FROM cd.members recs
	 	WHERE recs.memid = mems.recommendedby
	)
	FROM cd.members mems
ORDER BY member;

-- Question 17
-- Count the number of recommendations each member makes.
SELECT recommendedby, count(*) FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;

-- Question 18
-- List the total slots booked per facility
SELECT facid, SUM(slots) as "Total Slots" FROM cd.bookings
GROUP BY facid
ORDER BY facid;

-- Question 19
-- List the total slots booked per facility in a given month
SELECT facid, SUM(slots) as "Total Slots" FROM cd.bookings
WHERE starttime >= '2012-09-01' AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY SUM(slots);

-- Question 20
-- List the total slots booked per facility per month
SELECT facid, extract(month from starttime) as month, SUM(slots) as "Total Slots" FROM cd.bookings
WHERE extract(year from starttime) = 2012
GROUP BY facid, month
ORDER BY facid;

-- Question 21
-- Find the count of members who have made at least one booking
SELECT count(DISTINCT memid) FROM cd.bookings;

-- Question 22
-- List each member's first booking after September 1st 2012
SELECT mems.surname as surname, mems.firstname as firstname, mems.memid, min(bks.starttime) FROM cd.bookings bks
INNER JOIN cd.members mems
ON mems.memid = bks.memid
WHERE bks.starttime >= '2012-09-01'
GROUP BY mems.surname, mems.firstname, mems.memid
ORDER BY mems.memid;

-- Question 23
-- Produce a list of member names, with each row containing the total member count
SELECT count(*) over(), firstname, surname FROM cd.members
ORDER BY joindate;

-- Question 24
-- Produce a numbered list of members
SELECT row_number() over(ORDER BY joindate), firstname, surname FROM cd.members
ORDER BY joindate;

-- Question 25
-- Output the facility id that has the highest number of slots booked, again
SELECT facid, SUM(slots) AS total FROM cd.bookings
GROUP BY facid
HAVING SUM(slots) = (
  SELECT MAX(total)
  FROM (
    SELECT SUM(slots) AS total FROM cd.bookings
    GROUP BY facid
  ) AS subquery
);

-- Question 26
-- Format the names of members
SELECT surname || ', ' || firstname as name FROM cd.members;

-- Question 27
-- Find telephone numbers with parentheses
SELECT memid, telephone FROM cd.members
WHERE telephone SIMILAR TO '%[()]%';

-- Question 28
-- Count the number of members whose surname starts with each letter of the alphabet
SELECT substr(surname, 1, 1) as letter, count(*) FROM cd.members
GROUP BY letter
ORDER BY letter;