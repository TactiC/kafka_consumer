name := "kafka_consumer"

version := "1.0"
//
scalaVersion := "2.11.8"
//
//libraryDependencies += "org.apache.kafka"     % "kafka-clients"           % "0.11.0.0"
//
//libraryDependencies += "com.typesafe.akka"    % "akka-stream_2.11"        % "2.5.4"
//libraryDependencies += "com.typesafe.akka"    % "akka-actor_2.11"         % "2.5.4"
//
//libraryDependencies += "com.typesafe.akka"    % "akka-stream-kafka_2.11"  % "0.16"

libraryDependencies ++= {
  val akkaVersion = "2.4.18"
  Seq(
    "com.typesafe.akka"       %% "akka-actor"                         % akkaVersion,
//    "com.typesafe.akka"       %% "akka-http-core"                     % akkaVersion,
//    "com.typesafe.akka"       %% "akka-http-experimental"             % akkaVersion,
//    "com.typesafe.akka"       %% "akka-http-spray-json-experimental"  % akkaVersion,
    "com.typesafe.akka"       %% "akka-slf4j"                         % akkaVersion,
    "com.typesafe.akka"       %% "akka-stream-kafka"                         % "0.16",

    "org.apache.kafka"        %% "kafka"                              % "0.10.2.1" excludeAll(
      ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12"),
      ExclusionRule(organization = "log4j", name = "log4j")
    ),

    // logging
    "ch.qos.logback"          %  "logback-classic"                    % "1.1.3",

    // testing
    "com.typesafe.akka"       %% "akka-testkit"                       % akkaVersion   % "test",
    "org.scalatest"           %% "scalatest"                          % "2.2.0"       % "test"
  )
}
