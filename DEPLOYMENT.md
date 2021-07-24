## Deployment

## Setup on a fresh instance

#### 1. Install system dependencies
- java SDK(to run the app): `sudo amazon-linux-extras install java-openjdk11`
- check java version: `java -version`


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
