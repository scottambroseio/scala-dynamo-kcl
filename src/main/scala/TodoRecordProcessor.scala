import java.io.ByteArrayInputStream
import java.util

import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain
import com.amazonaws.services.cloudsearchdomain.model.{ContentType, UploadDocumentsRequest}
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.streamsadapter.model.RecordAdapter
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason
import com.amazonaws.services.kinesis.clientlibrary.types.{InitializationInput, ProcessRecordsInput, ShutdownInput}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

import scala.collection.JavaConverters._

class TodoRecordProcessor(private val cloudSearchDomainClient: AmazonCloudSearchDomain) extends IRecordProcessor {
  private val mapper = new ObjectMapper

  override def initialize(initializationInput: InitializationInput): Unit = {}

  override def processRecords(processRecordsInput: ProcessRecordsInput): Unit = {
    for (record <- processRecordsInput.getRecords().asScala) {
      val streamRecord = record.asInstanceOf[RecordAdapter].getInternalObject

      streamRecord.getEventName match {
        case "INSERT" | "MODIFY" =>
          val image = streamRecord.getDynamodb().getNewImage()

          val document = createSearchDocument(image)

          val json = document.toString()

          val stream = new ByteArrayInputStream(json.getBytes())

          val request = new UploadDocumentsRequest()
            .withDocuments(stream)
            .withContentLength(json.length.toLong)
            .withContentType(ContentType.Applicationjson)

          cloudSearchDomainClient.uploadDocuments(request)
        case "REMOVE" => // todo
      }
    }

    processRecordsInput.getCheckpointer.checkpoint
  }

  private def createSearchDocument(record: util.Map[String, AttributeValue]): JsonNode = mapper.createArrayNode()
    .add(mapper.createObjectNode()
      .put("type", "add")
      .put("id", record.get("Id").getS)
      .set("fields", mapper.createObjectNode()
        .put("id", record.get("Id").getS)
        .put("userid", record.get("UserId").getS)
        .put("content", record.get("Content").getS)
      ))

  override def shutdown(shutdownInput: ShutdownInput): Unit = {
    if (shutdownInput.getShutdownReason eq ShutdownReason.TERMINATE) {
      shutdownInput.getCheckpointer.checkpoint
    }
  }
}
