## Deployment

## Setup on a fresh instance

#### 1. Install system dependencies
- nginx(proxy server): `sudo amazon-linux-extras install nginx1`
- java SDK(to run the app): `sudo amazon-linux-extras install java-openjdk11`
- tar(to decompress the artifact): `sudo yum install tar`

Utils:
- check nginx version: `nginx -v`
- check java version: `java -version`


#### 1.1 Add a Nginx config:

Add a new config to the default nginx server:

```bash
sudo nano /etc/nginx/default.d/my_app.conf
```

Add this to the new file so nginx will forward request to the .net app(running as a service):

```
    location / {
        proxy_pass         http://127.0.0.1:8080;
        proxy_http_version 1.1;
        proxy_set_header   Upgrade $http_upgrade;
        proxy_set_header   Connection keep-alive;
        proxy_set_header   Host $host;
        proxy_cache_bypass $http_upgrade;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }

```

#### 2. Create a service file:

```bash
sudo nano /etc/systemd/system/my_rest_app.service
```

Add the following into it:

```bash
[Unit]
Description=Java REST API running on AWS Linux

[Service]
WorkingDirectory=/home/ec2-user
ExecStart=/usr/bin/java -jar /home/ec2-user/rest-app-1.jar
Restart=always
# Restart service after 10 seconds if the dotnet service crashes:
RestartSec=10
KillSignal=SIGINT
SyslogIdentifier=java-rest-app
User=ec2-user

[Install]
WantedBy=multi-user.target
```

#### 3. Enable the service:

`sudo systemctl enable my_rest_app.service`

Start the service:
`sudo systemctl start my_rest_app.service`

You can view service logs by running:

`sudo journalctl -fu my_rest_app.service`

#### 4. Re-Start the service:

sudo systemctl restart my_rest_app.service

### Debugging 

You can inspect the running ports by running: 

`sudo lsof -i -P -n | grep LISTEN`

You can reload the linux services by running:

`sudo systemctl daemon-reload`
{"mode":"full","isActive":false}