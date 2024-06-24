-- V2__Add_Active_And_Dates_To_Application_User.sql

-- Add is_active column
ALTER TABLE application_user
    ADD is_active BIT NOT NULL DEFAULT 1;

-- Add created_date and last_modified_date columns
ALTER TABLE application_user
    ADD created_date DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME(),
        last_modified_date DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME();