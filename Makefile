# Netty Client-Server Application Makefile

# Define variables
MAVEN = mvn
MAIN_SERVER = org.somecompany.server.StartServer
MAIN_CLIENT = org.somecompany.client.StartClient

# Default target - show help
.PHONY: help
help:
	@echo "Available commands:"
	@echo "  make compile - Compile the project"
	@echo "  make server  - Start the server"
	@echo "  make client  - Start the client"
	@echo "  make clean   - Clean the project"

# Compile the project
.PHONY: compile
compile:
	$(MAVEN) clean compile

# Start the server
.PHONY: server
server: compile
	$(MAVEN) exec:java -Dexec.mainClass=$(MAIN_SERVER)

# Start the client
.PHONY: client
client: compile
	$(MAVEN) exec:java -Dexec.mainClass=$(MAIN_CLIENT)

# Clean the project
.PHONY: clean
clean:
	$(MAVEN) clean 