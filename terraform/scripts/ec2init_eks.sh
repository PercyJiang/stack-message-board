#!/bin/bash
set -o xtrace
echo "percy: Adding worker node to the percy_eks_cluster"
/etc/eks/bootstrap.sh "percy_eks_cluster"
