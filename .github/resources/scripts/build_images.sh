#!/usr/bin/env bash
set -e

echo "GITHUB_ENV:"
echo "$GITHUB_ENV"
echo "⚙️ Set the Revision:"
REVISION=$(git rev-parse --short HEAD)

DOCKER_USERNAME=mekomsolutions

echo "⚙️ Run Docker build commands on remotes..."
archs=arm64,amd64
for arch in ${archs//,/ }
do
  ip=${!arch}
  echo "Remote: $arch: $ip"

  ssh -t -o StrictHostKeyChecking=no -i $AWS_AMI_PRIVATE_KEY_FILE -p 22 ubuntu@$ip /bin/bash -e << EOF
cd eip-client/docker
echo "⚙️ Build EIP Client image and tag it as '$DOCKER_USERNAME/eip-client:${REVISION}_$arch'..."
sudo docker build ./ -t $DOCKER_USERNAME/eip-client:${REVISION}_${arch}
EOF
done
