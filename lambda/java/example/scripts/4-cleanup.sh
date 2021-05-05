#!/bin/bash
set -eo pipefail

STACK=java-basic

FUNCTION1=$(aws cloudformation describe-stack-resource --stack-name $STACK --logical-resource-id HelloFunction  --query 'StackResourceDetail.PhysicalResourceId' --output text)
FUNCTION2=$(aws cloudformation describe-stack-resource --stack-name $STACK --logical-resource-id Hello2Function --query 'StackResourceDetail.PhysicalResourceId' --output text)
FUNCTION3=$(aws cloudformation describe-stack-resource --stack-name $STACK --logical-resource-id Hello3Function --query 'StackResourceDetail.PhysicalResourceId' --output text)
FUNCTION4=$(aws cloudformation describe-stack-resource --stack-name $STACK --logical-resource-id Hello4Function --query 'StackResourceDetail.PhysicalResourceId' --output text)

## Delete Stack
echo "Deleting stack $STACK"
aws cloudformation delete-stack --stack-name $STACK
echo "Deleted $STACK stack."

## Delete Bucket
if [ -f bucket-name.txt ]; then
    ARTIFACT_BUCKET=$(cat bucket-name.txt)
    if [[ ! $ARTIFACT_BUCKET =~ lambda-artifacts-[a-z0-9]{16} ]] ; then
        echo "Bucket was not created by this application. Skipping."
    else
        echo "Deleting bucket $ARTIFACT_BUCKET"
        aws s3 rb --force s3://$ARTIFACT_BUCKET
        rm bucket-name.txt
        echo "Deleted bucket $ARTIFACT_BUCKET"
    fi
fi

## Delete log groups
echo "Deleting log groups."
aws logs delete-log-group --log-group-name /aws/lambda/$FUNCTION1
aws logs delete-log-group --log-group-name /aws/lambda/$FUNCTION2
aws logs delete-log-group --log-group-name /aws/lambda/$FUNCTION3
aws logs delete-log-group --log-group-name /aws/lambda/$FUNCTION4
echo "Deleted log groups."

## その他
rm -f out.yml out.json
gradle clean
