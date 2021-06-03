## How to configure couchbase
1. Download and install Couchbase
2. Run through the initial setup and navigate to the dashboard on `http://127.0.0.1:8091`
3. In `Buckets`: create a new bucket called `sports`
5. In `Query`: Add a primary index to the bucket `sports` by executing the query `CREATE PRIMARY INDEX ON sports USING GSI;`