from couchbase.bucket import Bucket
import json
import time
import sys

bucket = Bucket("couchbase://couchbase.vagrant/transactions")
f = open(sys.argv[1],'r')
data = json.load(f)

for transaction in data:
    bucket.upsert("transaction::%d: " % transaction['transaction_id'], transaction)
    print "Upserting transaction::%d: " % transaction['transaction_id'], transaction
    time.sleep(.5)
