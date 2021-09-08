#!/usr/bin/env bash
set -e

REVISION=$(git rev-parse --short HEAD)

DOCKER_USERNAME=mekomsolutions

echo "⚙️ Run Docker build commands on remotes..."
archs=arm64,amd64
for arch in ${archs//,/ }
do
  ip=${!arch}
  echo "Remote: $arch: $ip"

  echo "🔑 Log in Docker Hub"
  ssh -t -o StrictHostKeyChecking=no -i $AWS_AMI_PRIVATE_KEY_FILE -p 22 ubuntu@$ip /bin/bash -e << EOF
  sudo docker login -p $DOCKER_PASSWORD -u $DOCKER_USERNAME
EOF

echo "⚙️ Run Docker push commands on remote."
ssh -t -o StrictHostKeyChecking=no -i $AWS_AMI_PRIVATE_KEY_FILE -p 22 ubuntu@$ip /bin/bash -e << EOF
  cd repo/docker
  echo "⚙️ Pushing EIP Client image"
  echo "⚙️ Pushing '$DOCKER_USERNAME/eip-client:${REVISION}_$arch'..."
  sudo docker push $DOCKER_USERNAME/eip-client:${REVISION}_$arch
EOF

done
