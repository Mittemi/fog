echo "Set CN correctly to a valid domain name!"
openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -keyout certs/key.key -out certs/cert.crt
echo "Copy crt file to the docker client's certs directory"
