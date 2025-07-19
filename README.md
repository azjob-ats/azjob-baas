sudo apt update && sudo apt install unzip -y

sudo apt install zip unzip -y

curl -s "https://get.sdkman.io" | bash

source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk version

sdk list java

sdk install java 21.0.2-tem

sdk use java 21.0.2-tem

sudo apt update && sudo apt upgrade -y

sudo apt install ca-certificates curl gnupg lsb-release -y

sudo mkdir -p /etc/apt/keyrings

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
  sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg


echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt update

sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y

sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
-o /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose

docker-compose --version

sudo apt update

sudo apt install docker-compose -y

docker-compose --version

sudo systemctl start docker

sudo systemctl status docker

sudo apt install maven

sudo docker-compose -f docker-compose-db.yml up -d

docker start postgres-db 

docker start pgadmin

docker exec postgres-db pg_dump -U admin -d educando > backup.sql

docker container ls

docker image ls

mvn clean install

mvn spring-boot:run