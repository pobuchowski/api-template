#/bin/sh
keytool -genkey -alias stub \
 -storetype PKCS12 -keyalg RSA -keysize 2048 \
 -keystore keystore.p12 -validity 3650 \
 -dname "CN=*.stub.io, OU=OJAS, O=Atos, L=London, ST=London, C=GB" \
 -storepass password
