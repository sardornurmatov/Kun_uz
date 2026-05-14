-- Profile table
CREATE TABLE profile (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    visible BOOLEAN NOT NULL DEFAULT true,
    photo_id VARCHAR(255),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Profile Role table
CREATE TABLE profile_role (
    id SERIAL PRIMARY KEY,
    profile_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE
);

-- Region table
CREATE TABLE region (
    id SERIAL PRIMARY KEY,
    order_number INTEGER NOT NULL,
    name_uz VARCHAR(255) NOT NULL,
    name_ru VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    key VARCHAR(255) NOT NULL UNIQUE,
    visible BOOLEAN NOT NULL DEFAULT true,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Category table
CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    order_number INTEGER NOT NULL,
    name_uz VARCHAR(255) NOT NULL,
    name_ru VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    key VARCHAR(255) NOT NULL UNIQUE,
    visible BOOLEAN NOT NULL DEFAULT true,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Section table
CREATE TABLE section (
    id SERIAL PRIMARY KEY,
    order_number INTEGER NOT NULL,
    name_uz VARCHAR(255) NOT NULL,
    name_ru VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    key VARCHAR(255) NOT NULL UNIQUE,
    image_id VARCHAR(255),
    visible BOOLEAN NOT NULL DEFAULT true,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Attachment table
CREATE TABLE attach (
    id UUID PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    path VARCHAR(500) NOT NULL,
    size BIGINT NOT NULL,
    extension VARCHAR(50) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Article table
CREATE TABLE article (
    id UUID PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    description TEXT NOT NULL,
    content TEXT NOT NULL,
    shared_count BIGINT DEFAULT 0,
    image_id VARCHAR(255),
    region_id INTEGER,
    moderator_id UUID NOT NULL,
    publisher_id UUID,
    status VARCHAR(50) NOT NULL,
    read_time INTEGER,
    view_count BIGINT DEFAULT 0,
    visible BOOLEAN NOT NULL DEFAULT true,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_date TIMESTAMP,
    FOREIGN KEY (region_id) REFERENCES region(id),
    FOREIGN KEY (moderator_id) REFERENCES profile(id),
    FOREIGN KEY (publisher_id) REFERENCES profile(id)
);

-- Article Category table
CREATE TABLE article_category (
    id SERIAL PRIMARY KEY,
    article_id UUID NOT NULL,
    category_id INTEGER NOT NULL,
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Article Section table
CREATE TABLE article_section (
    id SERIAL PRIMARY KEY,
    article_id UUID NOT NULL,
    section_id INTEGER NOT NULL,
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
    FOREIGN KEY (section_id) REFERENCES section(id)
);

-- Article Like table
CREATE TABLE article_like (
    id SERIAL PRIMARY KEY,
    profile_id UUID NOT NULL,
    article_id UUID NOT NULL,
    emotion VARCHAR(50) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (profile_id) REFERENCES profile(id),
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
);

-- Comment table
CREATE TABLE comment (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    profile_id UUID NOT NULL,
    article_id UUID NOT NULL,
    reply_id INTEGER,
    visible BOOLEAN NOT NULL DEFAULT true,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP,
    FOREIGN KEY (profile_id) REFERENCES profile(id),
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
    FOREIGN KEY (reply_id) REFERENCES comment(id)
);

-- Comment Like table
CREATE TABLE comment_like (
    id SERIAL PRIMARY KEY,
    profile_id UUID NOT NULL,
    comment_id INTEGER NOT NULL,
    emotion VARCHAR(50) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (profile_id) REFERENCES profile(id),
    FOREIGN KEY (comment_id) REFERENCES comment(id) ON DELETE CASCADE
);

-- Saved Article table
CREATE TABLE saved_article (
    id SERIAL PRIMARY KEY,
    profile_id UUID NOT NULL,
    article_id UUID NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (profile_id) REFERENCES profile(id),
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
);

-- Email History table
CREATE TABLE email_history (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- SMS History table
CREATE TABLE sms_history (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tag table
CREATE TABLE tag (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_profile_username ON profile(username);
CREATE INDEX idx_article_status ON article(status);
CREATE INDEX idx_article_created_date ON article(created_date DESC);
CREATE INDEX idx_article_published_date ON article(published_date DESC);
CREATE INDEX idx_region_visible ON region(visible);
CREATE INDEX idx_category_visible ON category(visible);
CREATE INDEX idx_section_visible ON section(visible);
