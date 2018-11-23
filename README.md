This is temp repo just to reproduce issue I encountered with Ignite, SSL and Docker. 
Here is a [related question on stack overflow](https://stackoverflow.com/questions/53433749/how-to-prevent-ignite-from-dying-if-first-attempt-to-join-the-cluster-failed):  

##### Assumptions:
1. You're running *nix OS
2. You have `maven` and `jdk 1.8+`
1. You have `docker-compose` `3.7+`
1. `docker` and `docker-compose` require `root` to run (thus `*.sh` scripts I've created will do `sudo` and ask for sudo password
1. Your non-loopback network device's name is `enp0s3` (if it isn't update variable `ETH_DEV` in both `*.sh` files)
2. None of your network devices have network `10.9.9.0/24` (if they do, change sh scripts and docker-compose to use other IP address to create and bind to)

##### In order to reproduce the issue:
1. Clone this repo
1. Run `same-time-ssl-issue.sh` script (this script will simultaneously bring up both `web0` and `web1` containers)
2. You'll see exceptions just like described in aforementioned stackoverflow question, context initialization failed and application will never recover from this error
3. Press `ctrl+c` (this will stop and remove current containers)
4. Run `sequential-no-issue.sh`script (this will bring up `web0` and then after a 20 second delay, `web1`). No issue and you can see it shows `2` nodes in cluster (in logs and if you do `wget http://10.9.9.1:8080/count -q -O -`)

##### Notes:
1. I wasn't able to reproduce the issue outside of docker environment
2. I was able to get rid of this issue by removing SSL configuration from Ignite
