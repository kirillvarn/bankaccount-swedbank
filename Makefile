build:
	mvnw clean package -DskipTests && docker compose up --build --force-recreate