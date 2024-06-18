-- Create ApplicationUser table
CREATE TABLE application_user (
                                  id UUID PRIMARY KEY,
                                  username VARCHAR(255) NOT NULL,
                                  password VARCHAR(255) NOT NULL,
                                  email VARCHAR(255),
                                  profile_id UUID,
                                  role_id UUID,
                                  FOREIGN KEY (profile_id) REFERENCES user_profile(id),
                                  FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Create Role table
CREATE TABLE role (
                      id UUID PRIMARY KEY,
                      name VARCHAR(255) NOT NULL
);

-- Create UserProfile table
CREATE TABLE user_profile (
                              id UUID PRIMARY KEY,
                              user_id VARCHAR(255) NOT NULL,
                              user_name VARCHAR(255),
                              profile_picture VARCHAR(255),
                              display_name VARCHAR(255),
                              UNIQUE(user_id),
                              FOREIGN KEY (user_id) REFERENCES application_user(id)
);
