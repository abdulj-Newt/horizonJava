#!/bin/bash
echo "Running database initialization scripts..."

sqlplus -s system/${ORACLE_PASSWORD}@//localhost:1521/XE <<EOF
WHENEVER SQLERROR EXIT SQL.SQLCODE

@/docker-entrypoint-initdb.d/setup/dbschema.sql
@/docker-entrypoint-initdb.d/setup/schema.sql
@/docker-entrypoint-initdb.d/setup/add-money-procedure.sql
@/docker-entrypoint-initdb.d/setup/bill-pay-procedure.sql

EXIT;
EOF

echo "Database initialization completed successfully."
