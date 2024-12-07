-- Create the database
CREATE DATABASE movement_db;

-- Use the database
USE movement_db;

-- Table to store tags
CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- Unique identifier for each tag
    name VARCHAR(255) NOT NULL,                -- Name of the tag
    is_global BOOLEAN DEFAULT FALSE,           -- Indicates if the tag is global
    user_id BIGINT,                            -- User ID for custom tags (null for global tags)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Creation timestamp
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Last update timestamp

    -- Index for faster lookup by user_id and name
    INDEX idx_tags_user_id (user_id),
    INDEX idx_tags_name (name)
);

-- Table to store movements
CREATE TABLE movements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,          -- Unique identifier for each movement
    user_id BIGINT NOT NULL,                       -- User ID from the users microservice
    date DATE NOT NULL,                            -- Date of the movement
    description TEXT NOT NULL,                     -- Description of the movement
    amount DECIMAL(10, 2) NOT NULL,                -- Amount of the movement
    movement_type ENUM('income', 'savings', 'expense') NOT NULL, -- Type of movement
    tag_id BIGINT,                                 -- Foreign key to the tags table
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Creation timestamp
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Last update timestamp

    -- Foreign key relationship
    CONSTRAINT fk_movements_tag FOREIGN KEY (tag_id) REFERENCES tags (id),

    -- Index for faster lookup by user_id and movement_type
    INDEX idx_movements_user_id (user_id),
    INDEX idx_movements_movement_type (movement_type),
    INDEX idx_movements_tag_id (tag_id)
);

-- Example data for tags
INSERT INTO tags (name, is_global, user_id) VALUES
('Food', FALSE, 1),
('Transport', TRUE, NULL),
('Savings', TRUE, NULL);

-- Example data for movements
INSERT INTO movements (user_id, date, description, amount, movement_type, tag_id) VALUES
(1, '2024-12-01', 'Grocery shopping', 100.50, 'expense', 1),
(1, '2024-12-02', 'Monthly saving deposit', 500.00, 'savings', 3),
(2, '2024-12-03', 'Bus ticket', 2.75, 'expense', 2);


