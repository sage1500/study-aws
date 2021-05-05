#!/bin/bash

. $(dirname $0)/0-env.sh
#
#echo "AWS_ACCOUNT_ID : $AWS_ACCOUNT_ID"

test () {
	local v1=$1
	shift
	
	while (($# > 1)); do
		echo "loop: $1 $2 $v1"
		shift 2
	done
}


test 1 2 3 4 5 6

exists_function example-hello; echo $?
exists_function example-hello5; echo $?

