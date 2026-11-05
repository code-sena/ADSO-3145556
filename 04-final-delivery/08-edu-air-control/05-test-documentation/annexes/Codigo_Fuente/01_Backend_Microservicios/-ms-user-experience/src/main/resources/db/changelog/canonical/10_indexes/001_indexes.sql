-- Indices para las consultas mas frecuentes del servicio.
CREATE INDEX ix_user_preferences_user_id
    ON user_experience.user_preferences (user_id);

CREATE INDEX ix_favorites_user_id
    ON user_experience.favorites (user_id);

CREATE INDEX ix_favorites_classroom_id
    ON user_experience.favorites (classroom_id);

CREATE INDEX ix_favorites_variable_id
    ON user_experience.favorites (variable_id);

CREATE INDEX ix_classroom_ratings_user_id
    ON user_experience.classroom_ratings (user_id);

CREATE INDEX ix_classroom_ratings_classroom_id
    ON user_experience.classroom_ratings (classroom_id);

CREATE INDEX ix_searches_user_id
    ON user_experience.searches (user_id);

CREATE INDEX ix_searches_searched_at
    ON user_experience.searches (searched_at DESC);
