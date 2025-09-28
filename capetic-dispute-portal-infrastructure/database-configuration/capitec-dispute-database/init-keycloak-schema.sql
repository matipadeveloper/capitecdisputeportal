-- Create the Keycloak schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS "capitec-dispute-keycloak";

-- Grant permissions to the database user
GRANT ALL PRIVILEGES ON SCHEMA "capitec-dispute-keycloak" TO "capitec-dispute-db-admin";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA "capitec-dispute-keycloak" TO "capitec-dispute-db-admin";
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA "capitec-dispute-keycloak" TO "capitec-dispute-db-admin";

-- Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA "capitec-dispute-keycloak" GRANT ALL PRIVILEGES ON TABLES TO "capitec-dispute-db-admin";
ALTER DEFAULT PRIVILEGES IN SCHEMA "capitec-dispute-keycloak" GRANT ALL PRIVILEGES ON SEQUENCES TO "capitec-dispute-db-admin";
