profile="docker"
if [ -z "$SERVICE_PROFILE" ]; then echo "No SERVICE_PROFILE set"; else  profile="$profile,$SERVICE_PROFILE"; fi
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=50050 -Dspring.profiles.active="$profile" -jar /usr/share/app.jar
