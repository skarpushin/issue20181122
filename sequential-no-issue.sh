#NOTE: Make sure to run it as root

# TODO: Update it with your non-loopback device name
ETH_DEV=enp0s3

mvn package -DskipTests

cd docker-image
cp ../target/issue20181122-0.0.1-SNAPSHOT.jar issue20181122-0.0.1-SNAPSHOT.jar
cp ../keystore.jks keystore.jks
sudo docker build -t isrdc/webapp:0.0.1 .

sudo ip addr add 10.9.9.1/24 dev $ETH_DEV

cd ../docker-compose
set -e

sudo docker-compose up -d web0
sleep 15
sudo docker-compose up web1

sudo docker-compose down -v --remove-orphans --rmi local
