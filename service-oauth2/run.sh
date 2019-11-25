# Start the proxy
./cloud_sql_proxy -instances=${CLOUDSQL_INSTANCE}=tcp:3306 -credential_file=${CLOUDSQL_CREDENTIALS} &

# wait for the proxy to spin up
sleep 10

# Start the server
#java -Xmx400M -Djava.security.egd=file:/dev/./urandom -Dserver.port=${PORT} -jar /service-oauth2.jar &
