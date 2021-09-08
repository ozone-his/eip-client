#!/usr/bin/env bash
set -e

REVISION=$(git rev-parse --short HEAD)

DOCKER_USERNAME=mekomsolutions

echo "⚙️ Compute docker manifest CLI arguments"
archs=arm64,amd64
args=" "
for arch in ${archs//,/ }
do
  args="${args} --amend $DOCKER_USERNAME/eip-client:${REVISION}_${arch}"
done
echo "Args: $args"

# Log in one of the machines only
arch=arm64
ip=${!arch}
echo "Remote: $arch: $ip"

echo "🔑 Log in Docker Hub"
ssh -t -o StrictHostKeyChecking=no -i $AWS_AMI_PRIVATE_KEY_FILE -p 22 ubuntu@$ip /bin/bash -e << EOF
sudo docker login -p $DOCKER_PASSWORD -u $DOCKER_USERNAME
EOF

ssh -t -o StrictHostKeyChecking=no -i $AWS_AMI_PRIVATE_KEY_FILE -p 22 ubuntu@$ip /bin/bash -e << EOF
cd repo/docker
echo "⚙️ Will push the manifests for EIP Client"
echo "⚙️ Create manifest '$DOCKER_USERNAME/eip-client:${REVISION}'..."
sudo docker manifest create $DOCKER_USERNAME/eip-client:${REVISION} ${args}
echo "⚙️ Pushing manifest..."
sudo docker manifest push $DOCKER_USERNAME/eip-client:${REVISION}

echo "⚙️ Create manifest '$DOCKER_USERNAME/eip-client:latest'..."
sudo docker manifest create $DOCKER_USERNAME/eip-client:latest ${args}
echo "⚙️ Pushing manifest..."
sudo docker manifest push $DOCKER_USERNAME/eip-client:latest

EOF
