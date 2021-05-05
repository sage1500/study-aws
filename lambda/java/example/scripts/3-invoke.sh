#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name java-basic --logical-resource-id Hello4Function --query 'StackResourceDetail.PhysicalResourceId' --output text)
PAYLOAD='{ "name": "Saburo" }'

aws lambda invoke --function-name $FUNCTION --payload $(echo -n "$PAYLOAD" | base64) out.json
cat out.json
