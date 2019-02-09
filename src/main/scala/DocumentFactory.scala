import java.util

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

object DocumentFactory {
  private val mapper = new ObjectMapper()

  def createDocumentToUpload(record: util.Map[String, AttributeValue]): JsonNode = mapper.createArrayNode()
    .add(mapper.createObjectNode()
      .put("type", "add")
      .put("id", record.get("Id").getS)
      .set("fields", mapper.createObjectNode()
        .put("id", record.get("Id").getS)
        .put("userid", record.get("UserId").getS)
        .put("content", record.get("Content").getS)
      ))

  def createDocumentToDelete(id: String): JsonNode = mapper.createArrayNode()
    .add(mapper.createObjectNode()
      .put("type", "delete")
      .put("id", id))
}
