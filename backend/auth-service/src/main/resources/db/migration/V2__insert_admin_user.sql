-- Insert default admin user (password: Admin@123456789)
-- BCrypt hash generated with strength 12
INSERT INTO users (id, name, email, password, role, active, created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000',
    'Admin User',
    'admin@medical-scheduling.local',
    '$2a$12$JkBZW/GZcK1Aq.bZ8q8BYubPO5WkqOqJXBkJzXf.t9BzXpKPWVxH2',
    'ADMIN',
    true,
    NOW(),
    NOW()
)
ON CONFLICT (email) DO NOTHING;
