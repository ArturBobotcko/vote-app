# Netty Client-Server Application Makefile

# Define variables
MAVEN = mvn
MAIN_SERVER = org.somecompany.server.Server
MAIN_CLIENT = org.somecompany.client.Client
SERVER_PORT = 8080
CLIENT_HOST = localhost
CLIENT_PORT = 8080

# Default target - show help
.PHONY: help
help:
	@echo "Available commands:"
	@echo "  make compile     - Compile the project"
	@echo "  make server      - Start the server on port $(SERVER_PORT)"
	@echo "  make client      - Start the client connecting to $(CLIENT_HOST):$(CLIENT_PORT)"
	@echo "  make clean       - Clean the project"
	@echo ""
	@echo "You can override default settings:"
	@echo "  make server PORT=9090               - Start server on port 9090"
	@echo "  make client HOST=192.168.1.5 PORT=9090  - Connect to specific host:port"

# Compile the project
.PHONY: compile
compile:
	$(MAVEN) clean compile

# Start the server
.PHONY: server
server: compile
	$(MAVEN) exec:java -Dexec.mainClass=$(MAIN_SERVER) -Dexec.args="$(or $(PORT),$(SERVER_PORT))"

# Start the client
.PHONY: client
client: compile
	$(MAVEN) exec:java -Dexec.mainClass=$(MAIN_CLIENT) -Dexec.args="$(or $(HOST),$(CLIENT_HOST)) $(or $(PORT),$(CLIENT_PORT))"

# Clean the project
.PHONY: clean
clean:
	$(MAVEN) clean 