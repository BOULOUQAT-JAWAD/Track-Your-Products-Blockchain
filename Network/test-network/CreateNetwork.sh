#!/bin/bash

echo "Network Down "
sudo ./network.sh down

sudo docker system prune

rm -R system-genesis-block/

rm -R organizations/fabric-ca/delivery/ organizations/fabric-ca/manufacturer/ organizations/fabric-ca/seller/ organizations/fabric-ca/ordererOrg/

echo "Create Network"
sudo ./network.sh up -ca -s couchdb

echo "Create Channel"
sudo ./network.sh createChannel

sudo chmod 666 /var/run/docker.sock

sudo chmod -R 777 .

echo "Package chaincode"
cd ../chaincode/SmartMDS/

sudo ./gradlew clean

sudo ./gradlew installDist

cd ../../test-network/

source ./lifecycle_setup_manufacturer.sh

peer lifecycle chaincode package SmartMDS.tar.gz --path /home/jawad/Desktop/Track-Your-Products-Blockchain/Network/chaincode/SmartMDS --lang java --label SmartMDS_1

echo "Install chaincode for manufacturing"
peer lifecycle chaincode install SmartMDS.tar.gz --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

peer lifecycle chaincode queryinstalled SmartMDS.tar.gz --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

echo "Install chaincode for delivery"
source ./lifecycle_setup_delivery.sh

peer lifecycle chaincode install SmartMDS.tar.gz --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

peer lifecycle chaincode queryinstalled SmartMDS.tar.gz --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

echo "Install chaincode for seller"
source ./lifecycle_setup_seller.sh

peer lifecycle chaincode install SmartMDS.tar.gz --peerAddresses localhost:11051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

peer lifecycle chaincode queryinstalled SmartMDS.tar.gz --peerAddresses localhost:11051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE

# Execute the command to query the installed chaincode and capture the output
output=$(peer lifecycle chaincode queryinstalled SmartMDS.tar.gz --peerAddresses localhost:11051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE)

# Extract the package ID from the output using grep and awk
packageID=$(echo "$output" | grep -oP '(?<=Package ID: ).*(?=, Label:)')

# Check if the package ID is not empty
if [ -n "$packageID" ]; then
    echo "Package ID: $packageID"
    
    source ./lifecycle_setup_manufacturer.sh
    peer lifecycle chaincode getinstalledpackage --package-id $packageID --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE
    
    peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA -C channelmds --name SmartMDS --version 1.0 --init-required --package-id $PACKAGEID --sequence 1
    
    source ./lifecycle_setup_delivery.sh
    peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA -C channelmds --name SmartMDS --version 1.0 --init-required --package-id $PACKAGEID --sequence 1
    
    source ./lifecycle_setup_seller.sh
    peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA -C channelmds --name SmartMDS --version 1.0 --init-required --package-id $PACKAGEID --sequence 1

    source ./lifecycle_setup_manufacturer.sh
    peer lifecycle chaincode checkcommitreadiness -C channelmds --name SmartMDS --version 1.0 --sequence 1 --output json --init-required

    source ./lifecycle_setup_delivery.sh
    peer lifecycle chaincode checkcommitreadiness -C channelmds --name SmartMDS --version 1.0 --sequence 1 --output json --init-required
    
    source ./lifecycle_setup_seller.sh
    peer lifecycle chaincode checkcommitreadiness -C channelmds --name SmartMDS --version 1.0 --sequence 1 --output json --init-required
    
    
else
    echo "Failed to extract Package ID from the queryinstalled output."
fi
