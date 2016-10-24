
DROP DATABASE IF EXISTS UNIVERSITY;

CREATE DATABASE UNIVERSITY;

USE UNIVERSITY;
#-------------------------------------------CREATE DEPARTMENT TABLE-------------------------------------------
CREATE TABLE DEPARTMENT
(
	DEPT_NAME CHAR(3) PRIMARY KEY,
	BUILDING INTEGER,
	BUDGET INTEGER
);
#===============================================================================================================

#-------------------------------------------CREATE CLASSROOM TABLE----------------------------------------------
CREATE TABLE CLASSROOM
(
	BUILDING INTEGER,
	ROOM_NO INTEGER,
	CAPACITY INTEGER,
	PRIMARY KEY (BUILDING,ROOM_NO)
);
#===============================================================================================================

#-------------------------------------------CREATE STUDENT TABLE-----------------------------------------------
CREATE TABLE STUDENT
(
	STUDENT_ID INTEGER PRIMARY KEY,
	NAME VARCHAR(32),
	DEPT_NAME CHAR(3),
	TOTAL_CREDIT INTEGER,
	FOREIGN KEY (DEPT_NAME) REFERENCES DEPARTMENT(DEPT_NAME)
);

#===============================================================================================================

#-------------------------------------------CREATE COURSE TABLE------------------------------------------------

CREATE TABLE COURSE
(
	COURSE_ID INTEGER PRIMARY KEY,
	TITLE VARCHAR(32),
	DEPT_NAME CHAR(3),
	CREDITS INTEGER,
	FOREIGN KEY (DEPT_NAME) REFERENCES DEPARTMENT(DEPT_NAME)
);
#===============================================================================================================

#-------------------------------------------CREATE INSTRUCTOR TABLE---------------------------------------------

CREATE TABLE INSTRUCTOR
(
	INSTRUCTOR_ID INTEGER PRIMARY KEY,
	NAME VARCHAR(32),
	DEPT_NAME CHAR(3),
	SALARY INTEGER,
	FOREIGN KEY (DEPT_NAME) REFERENCES DEPARTMENT(DEPT_NAME)
);
#===============================================================================================================

#-------------------------------------------CREATE ADVISOR TABLE-----------------------------------------------

CREATE TABLE ADVISOR
(
	STUDENT_ID INTEGER PRIMARY KEY,
	INSTRUCTOR_ID INTEGER,
	FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(STUDENT_ID),
	FOREIGN KEY (INSTRUCTOR_ID) REFERENCES INSTRUCTOR(INSTRUCTOR_ID)
);
#===============================================================================================================

#-------------------------------------------CREATE PREREQUISITE TABLE-------------------------------------------

CREATE TABLE PREREQUISITE
(
	COURSE_ID INTEGER PRIMARY KEY,
	PREREQUISITE_ID INTEGER,
	FOREIGN KEY (COURSE_ID) REFERENCES COURSE(COURSE_ID),
	FOREIGN KEY (PREREQUISITE_ID) REFERENCES COURSE(COURSE_ID)
);
#===============================================================================================================

#-------------------------------------------CREATE TIMESLOT TABLE-------------------------------------------

CREATE TABLE TIMESLOT
(
	TIMESLOT_ID INTEGER,
	DAY CHAR(1),
	START_TIME INTEGER,
	END_TIME INTEGER,
	PRIMARY KEY (TIMESLOT_ID,DAY)
);
#===============================================================================================================

#-------------------------------------------CREATE TIMESLOT TABLE-------------------------------------------

CREATE TABLE SECTION
(
	COURSE_ID INTEGER,
	SECTION_ID INTEGER,
	SEMESTER VARCHAR(8),
	YEAR VARCHAR(4),
	BUILDING INTEGER,
	ROOM_NO INTEGER,
	TIMESLOT_ID INTEGER,
	PRIMARY KEY (COURSE_ID,SECTION_ID,SEMESTER,YEAR),
	FOREIGN KEY (BUILDING,ROOM_NO) REFERENCES CLASSROOM(BUILDING,ROOM_NO),
	FOREIGN KEY (TIMESLOT_ID) REFERENCES TIMESLOT(TIMESLOT_ID)
);

#===============================================================================================================

#-------------------------------------------CREATE TIMESLOT TABLE-------------------------------------------

CREATE TABLE TEACHES
(
	INSTRUCTOR_ID INTEGER,
	COURSE_ID INTEGER,
	SECTION_ID INTEGER,
	SEMESTER VARCHAR(8),
	YEAR VARCHAR(4),
	PRIMARY KEY (INSTRUCTOR_ID,COURSE_ID,SECTION_ID,SEMESTER,YEAR),
	FOREIGN KEY (INSTRUCTOR_ID) REFERENCES INSTRUCTOR(INSTRUCTOR_ID),
	FOREIGN KEY (COURSE_ID,SECTION_ID,SEMESTER,YEAR) REFERENCES SECTION(COURSE_ID,SECTION_ID,SEMESTER,YEAR)
);

#===============================================================================================================

#-------------------------------------------CREATE TIMESLOT TABLE-------------------------------------------

CREATE TABLE TAKES
(
	STUDENT_ID INTEGER,
	COURSE_ID INTEGER,
	SECTION_ID INTEGER,
	SEMESTER VARCHAR(8),
	YEAR VARCHAR(4),
	GRADE CHAR(1),
	PRIMARY KEY (STUDENT_ID,COURSE_ID,SECTION_ID,SEMESTER,YEAR),
	FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(STUDENT_ID),
	FOREIGN KEY (COURSE_ID,SECTION_ID,SEMESTER,YEAR) REFERENCES SECTION(COURSE_ID,SECTION_ID,SEMESTER,YEAR)
);

#===============================================================================================================


