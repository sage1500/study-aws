#!/bin/bash

##
## ロール作成
##

. 0-env.sh

aws iam create-role --role-name $IAM_ROLE_NAME --assume-role-policy-document file://trust-policy.json
aws iam attach-role-policy --role-name $IAM_ROLE_NAME --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
