import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.{IRecordProcessor, IRecordProcessorFactory}

class TodoRecordProcessorFactory(cloudSearchDomainClient: AmazonCloudSearchDomain) extends IRecordProcessorFactory {
  override def createProcessor(): IRecordProcessor = new TodoRecordProcessor(cloudSearchDomainClient)
}
