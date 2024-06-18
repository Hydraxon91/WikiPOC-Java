-- Create Role table
CREATE TABLE role (
                      id uniqueidentifier PRIMARY KEY,
                      name VARCHAR(255) NOT NULL
);

-- Create UserProfile table first
CREATE TABLE user_profile (
                              id uniqueidentifier PRIMARY KEY,
                              user_id VARCHAR(255) NOT NULL,
                              user_name VARCHAR(255),
                              profile_picture VARCHAR(255),
                              display_name VARCHAR(255),
                              UNIQUE(user_id)
);

-- Create ApplicationUser table with corrected foreign key order
CREATE TABLE application_user (
                                  id uniqueidentifier PRIMARY KEY,
                                  username VARCHAR(255) NOT NULL,
                                  password VARCHAR(255) NOT NULL,
                                  email VARCHAR(255),
                                  profile_id uniqueidentifier,
                                  role_id uniqueidentifier,
                                  FOREIGN KEY (profile_id) REFERENCES user_profile(id),
                                  FOREIGN KEY (role_id) REFERENCES role(id)
);
