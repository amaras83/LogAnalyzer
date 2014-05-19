/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.service

import org.scalatest._
import mcross1882.loganalyzer.analyzer.SimpleAnalyzer
import mcross1882.loganalyzer.parser.SimpleParser
import mcross1882.loganalyzer.service.ServiceFactory

class ServiceFactorySpec extends FlatSpec with Matchers {

    "createFromXML(...)" should "return a list of services from an external XML file" in {
        val services = ServiceFactory.createFromXml("src/test/resources/services.config", buildParsers)
        services(0).name should be("demo")
    }
    
    protected def buildParsers = List(
        new SimpleParser("sample_parser", buildAnalyzers)
    )
    
    protected def buildAnalyzers = List(
        new SimpleAnalyzer("sample_test", """some regex""".r, "the output message")
    )
}
