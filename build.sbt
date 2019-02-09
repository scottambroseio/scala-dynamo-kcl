name := "scala-dynamo-kcl"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "com.amazonaws" % "amazon-kinesis-client" % "1.9.3"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.494"

libraryDependencies += "com.amazonaws" % "dynamodb-streams-kinesis-adapter" % "1.4.0"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-cloudsearch" % "1.11.494"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.8"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
