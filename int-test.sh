#/bin/bash
docker run -it --rm --name my-maven-project \
 -v /var/run/docker.sock:/var/run/docker.sock \
 -v /var/lib/docker:/var/lib/docker -v ~/.m2:/root/.m2 \
 -v $PWD:/usr/src/mymaven -w /usr/src/mymaven \
 -v $PWD/target:/usr/src/mymaven/target -w /usr/src/mymaven \
 --network host \
maven \
mvn test -P test
