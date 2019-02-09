import com.amazonaws.services.dynamodbv2.model.AttributeValue
import org.scalatest.FlatSpec

import scala.collection.JavaConverters._

class DocumentFactorySpec extends FlatSpec {
  "A DocumentFactory" should "be able to create a document for uploading" in {
    val id = "1234"
    val userid = "5678"
    val content = "Hello world"

    val record = Map(
      "Id" -> new AttributeValue().withS(id),
      "UserId" ->  new AttributeValue().withS(userid),
      "Content" ->  new AttributeValue().withS(content),
    )

    val results = DocumentFactory.createDocumentToUpload(record.asJava)
    val document = results.get(0)

    assert(document.get("id").asText() == id)
    assert(document.get("type").asText() == "add")
    assert(document.get("fields").get("id").asText() == id)
    assert(document.get("fields").get("userid").asText() == userid)
    assert(document.get("fields").get("content").asText() == content)
  }

  it should "be able to create a document for deleting" in {
    val id = "1234"

    val results = DocumentFactory.createDocumentToDelete(id)
    val document = results.get(0)

    assert(document.get("id").asText() == id)
    assert(document.get("type").asText() == "delete")
  }
}
