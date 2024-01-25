#!/usr/bin/env sh
if [ -n "$EIP_PROFILE" ]; then
  ENV="$EIP_PROFILE"
else
  # Check for the provided environment or use 'dev' as default
  if [ -z "$1" ]; then
    ENV="dev"
  else
    ENV="$1"
  fi
fi

# Ensure that the provided environment is valid
case "$ENV" in
  dev|prod|test|staging)
    ;;
  *)
    echo "Error: Invalid environment provided."
    ;;
esac

export ENV SPRING_PROFILES_ACTIVE="$ENV"

# Check if routes directory exists and has contents
if [ -d "routes" ] && [ "$(ls -A routes)" ]; then
    if [ "$ENV" = "dev" ]; then
        echo "Routes directory contents:"
        ls -p routes/ | grep -v / | sed 's/^/routes\//' | while read -r file; do
            echo "- ${file}"
        done
    fi
else
    mkdir -p routes
    echo "No routes found in routes directory."
fi

echo "Starting Ozone EIP Client Application in $ENV environment."
java -cp "eip-client.jar" "-Dloader.path=routes/" org.springframework.boot.loader.PropertiesLauncher
