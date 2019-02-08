import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClientBuilder
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder
import com.amazonaws.services.dynamodbv2.streamsadapter.{AmazonDynamoDBStreamsAdapterClient, StreamsWorkerFactory}
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDBClientBuilder, AmazonDynamoDBStreamsClientBuilder}
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.{InitialPositionInStream, KinesisClientLibConfiguration}

object Main extends App {
  val documentEndpoint = sys.env("DOCUMENT_ENDPOINT")
  val region = sys.env("REGION")
  val applicationName = sys.env("APPLICATION_NAME")
  val streamName = sys.env("STREAM_NAME")
  val workerId = sys.env("WORKER_ID")

  val dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
    .withRegion(region)
    .build()

  val cloudWatchClient = AmazonCloudWatchClientBuilder.standard()
    .withRegion(region)
    .build()

  val dynamoDBStreamsClient = AmazonDynamoDBStreamsClientBuilder.standard()
    .withRegion(region)
    .build()


  val cloudSearchDomainClient = AmazonCloudSearchDomainClientBuilder.standard()
    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
      documentEndpoint,
      region
    ))
    .build()

  val adapterClient = new AmazonDynamoDBStreamsAdapterClient(dynamoDBStreamsClient)

  val workerConfig = new KinesisClientLibConfiguration(applicationName,
    streamName,
    DefaultAWSCredentialsProviderChain.getInstance(),
    workerId)
    .withMaxRecords(1000)
    .withIdleTimeBetweenReadsInMillis(500)
    .withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON)

  val factory = new TodoRecordProcessorFactory(cloudSearchDomainClient)

  val worker = StreamsWorkerFactory.createDynamoDbStreamsWorker(factory, workerConfig, adapterClient, dynamoDBClient, cloudWatchClient)

  worker.run()
}