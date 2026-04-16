#!/bin/bash
set -e

echo "Initializing database schema..."

sqlplus -s system/oracle@//localhost:1521/XE <<EOF
@/docker-entrypoint-initdb.d/setup/dbschema.sql
@/docker-entrypoint-initdb.d/setup/schema.sql
@/docker-entrypoint-initdb.d/setup/add-money-procedure.sql
@/docker-entrypoint-initdb.d/setup/bill-pay-procedure.sql
EXIT;
EOF

echo "Database initialization completed."
