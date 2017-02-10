# .INIT

## About

This project is a Apache Spark Stream, Apache Kafka Stream, MongoDB and Scala. The target is provide entertainment content based on Facebook User ID to get all user events on Facebook using the Facebook Graph API.

## Requirements

To run and use this project you need Scala, SBT and Java 8 installed locally to compile. Also you will need a environment to provide a MongoDB and a Kafka instance.

To run locally please follow the instructions below.

## Installing
If you don't have Java 8 or Scala installed, please follow these steps before proceed to How to Run section.

### Java 8 (Linux)
To install Java 8 via apt-get follow these steps below.

> sudo add-apt-repository ppa:webupd8team/java

> sudo apt update; sudo apt install oracle-java8-installer

After the installing finish, check your java version.

> java -version

It should print java 1.8.x_xxx

### Scala and SBT (Linux) 

If you don't have Scala or SBT environment in your local machine, follow these steps.

> sudo apt-get remove scala-library scala

> sudo wget www.scala-lang.org/files/archive/scala-2.11.8.deb

> sudo gdebi -i scala-2.11.8.deb

> wget http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.13.0/sbt.deb

> sudo gdebi -i sbt.deb


## How to Run

Apache Spark and Spark provides a easy way to run locally. You can see all possible configurations in Program Arguments section.

After you checkout this project, on project's root directory you will need perform  ```sbt assembly``` to generate a Fat Jar, when it finish the artifact will be generated in ./target/scala/crawler.jar. You can run this jar with a common java -jar command but you will need run with all required parameters as you can see below.

## Program Arguments

| Parameter                   | Description                                      | Default Value  |
| ----------------------------|:------------------------------------------------:| --------------:|
| app.name                    | App name used in Spark Web UI                    |      N/A                        |
| app.master                  | Value used in Spark Config to define Spark Master| yarn                            |
| app.batch.duration          | Duration between each Streaming process          | N/A                             |
| kafka.servers               | Connection URL with Kafka                        | N/A                             |
| kafka.topic                 | Topic which Spark will use to read messages      | N/A                             |
| group.id                    | Kafka Group ID property                          | N/A                             |
| enable.auto.commit          | Auto commit Kafka OffsetRange                    | true                            |
| mongo.connection.url        | Mongo Connection URL                             | mongodb://localhost:27017       |
| mongo.database.name         | Database where will be the collection            | test                            |
| mongo.collection.name       | Mongo collection where will be persists the data | fb_events                       |
| graph.facebook.uri          | The URL to connect with Facebook Graph API       | https://graph.facebook.com/v2.8 |
| graph.facebook.access.token | Your App Access Token to use Facebook Graph API  | N/A                             |
| limit                       | Limit of result set                              | 50                              |
| fields                       | Fields returing in the call user_vents. Please Check the Graph API documentation | category,description,name,place,start_time,end_time |

### This software is licensed under the Apache 2 license, quoted below.

Copyright 2017 Fernando Soliva <lfernando.soliva@outlook.com>

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
