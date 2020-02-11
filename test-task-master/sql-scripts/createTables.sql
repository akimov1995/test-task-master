CREATE TABLE IF NOT EXISTS DOCTOR(doctor_id INT IDENTITY primary key not null,
doctor_name VARCHAR(40) , doctor_surname VARCHAR(40) , doctor_patronymic VARCHAR(40) ,
specialization VARCHAR(40));

CREATE TABLE IF NOT EXISTS PATIENT(patient_id INT IDENTITY primary key not null,
patient_name VARCHAR(40) , patient_surname VARCHAR(40) , patient_patronymic VARCHAR(40) ,
phone_number VARCHAR(20));

CREATE TABLE IF NOT EXISTS Recipe(recipe_id INT IDENTITY primary key not null,description VARCHAR(40),
recipe_date DATE , validity_period INT , priority VARCHAR(40) ,
patient_id INT not null, doctor_id INT not null,
FOREIGN KEY (patient_id) REFERENCES PATIENT (patient_id) ON DELETE RESTRICT,
FOREIGN KEY (doctor_id) REFERENCES DOCTOR (doctor_id) ON DELETE RESTRICT);

/*DROP TABLE IF EXISTS Recipe;*/