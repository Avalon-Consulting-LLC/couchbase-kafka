from couchbase.bucket import Bucket
import json
import time

bucket = Bucket("couchbase://localhost/transactions")
f = open('transactions.json','r')
data = json.load(f)

for transaction in data:
    bucket.upsert("transaction::%d: " % transaction['transaction_id'], transaction)
    time.sleep(.5)
