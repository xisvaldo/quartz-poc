# quartz-poc
A dynamic scheduler using Quartz and persisting the scheduler interval into database.

<hr>

curl --location --request POST 'localhost:8080/scheduler' \
--header 'Content-Type: application/json' \
--data-raw '{
"identity": "job one",
"description": "this job will do something amazing",
"intervalInSeconds": 10
}'
