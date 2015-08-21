# Purchase Transaction Alerting with Couchbase and Kafka
This project is an all in one environment that sets up 2 Vagrant machines. One with Couchbase installed with a fresh bucket and another with Kafka installed and a topic created. It has a Python script to insert data into Couchbase over the span of 5 minutes, and two Java projects. One project is for transporting data from Couchbase to Kafka, the other is for monitoring the Kafka stream for actionable transactions.

Sample data was generated using the generatedata-vagrant project: https://github.com/benkeen/generatedata-vagrant

Prerequisites
-------------
1. Install Virtualbox: https://www.virtualbox.org/wiki/Downloads

2. Install Vagrant: http://www.vagrantup.com/downloads.html

3. Install necessary Vagrant plugins:

```sh
vagrant plugin install vagrant-hostmanager
vagrant plugin install vagrant-cachier
```

4. Install Ansible

```sh
brew install ansible
```

5. Install Maven

```sh
brew install maven
```

Getting Started
------
Start by building the transport application and copying the jar to the shared vagrant folder:

```sh
cd kafka-transport
mvn clean package
cp target/kafka-transport-1.0-SNAPSHOT.jar ../vagrant
```

Do the same for the alerter application:

```sh
cd kafka-alerter
mvn clean package
cp target/kafka-alerter-1.0-SNAPSHOT.jar ../vagrant
```

Bring up the Vagrant machines, it is configured to install everything you need to run the example

```sh
cd vagrant
vagrant up
```

Run the Alerting Example
------------------------
There are several moving parts in this example so to fully understand the way the data is moving it is best to run the example using 4 seperate console windows.

#### Window 1 - The Transport Application
The transport application is a small Java application using the Couchbase Kafka Connector: http://docs.couchbase.com/connectors/kafka-1.0/kafka-intro.html. It will monitor all DCPEvents from Couchbase and forward MutationMessages on into a Kafka topic.

```sh
cd vagrant
vagrant ssh kafka
java -cp /vagrant/kafka-transport-1.0-SNAPSHOT.jar com.avalonconsult.couchbase.kafka.KafkaTransport
```

#### Window 2 - The Kafka Consumer
We will start a basic console consumer to monitor the Couchbase documents coming into the Kafka topic.

```sh
cd vagrant 
vagrant ssh kafka
/opt/kafka_2.10-0.8.2.0/bin/kafka-console-consumer.sh --zookeeper kafka.vagrant:2181 --topic transactions
```

#### Window 3 - The Alerter Application
The alerter application is a small example of a Java Kafka Consumer. It will consume the documents coming through the topic and print a message to the console if the amount paid was greater then $9000.00.

```sh
cd vagrant
vagrant ssh kafka
java -cp /vagrant/kafka-alerter-1.0-SNAPSHOT.jar com.avalonconsult.couchbase.kafka.KafkaAlerter
```

#### Window 4 - Insert Couchbase Data
Once the previous three windows are running, we are ready to start pushing data into Couchbase. This python script will read sample data from transactions.json and insert them one at a time into Couchbase every 0.5 seconds.

```sh
cd vagrant
vagrant ssh couchbase
python /vagrant/send_transactions.py /vagrant/transactions.json
```

As the documents are added to Couchbase you should see them in the console consumer in Window 2, and when you hit a document that has an amount greater then $9000.00 you should see a message in the alerter application in Window 3.

You can access the Couchbase UI at http://couchbase.vagrant:8091 with credentials: couchbase//couchbase
