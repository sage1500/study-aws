#!/bin/bash

. $(dirname $0)/0-env.sh
. $(dirname $0)/define.sh

## 関数一括更新
delete_functions ${FUNCTIONS[@]}
