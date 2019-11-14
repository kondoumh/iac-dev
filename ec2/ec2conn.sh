#!/bin/sh

startInstance() {
    aws ec2 start-instances --instance-ids ${EC2_INSTANCE_ID}
}

showIp() {
    ip=$(aws ec2 describe-instances --instance-ids ${EC2_INSTANCE_ID} --query "Reservations[0].Instances[0].PublicIpAddress")
    echo ${ip}
}

login() {
    ip=$(aws ec2 describe-instances --instance-ids ${EC2_INSTANCE_ID} --query "Reservations[0].Instances[0].PublicIpAddress")
    echo ${ip}
    ssh -i devenv-key.pem ubuntu@${ip}
}

stopInstance() {
    aws ec2 stop-instances --instance-ids ${EC2_INSTANCE_ID}
}

if [ $1 = "start" ]; then
    echo "Starting instance"
    startInstance
elif [ $1 = "ip" ]; then
    echo "show ip"
    showIp
elif [ $1 = "login" ]; then
    echo "login"
    login
elif [ $1 = "stop" ]; then
    echo "Stopping instance"
    stopInstance
fi
