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
import mcross1882.loganalyzer.service.{Service, ServiceFactory}

class ServiceSpec extends FlatSpec with Matchers {

    "run" should "iterate through the file list parsing each one iteratively" in {
        val service = buildService
        service.run
    }
    
    "print" should "iterate through each parser and print its result" in {
        val service = buildService
        service.run
        service.print
    }
    
    protected def buildService = 
        new Service("test_service", "A Test Service", List("src/test/resources/sample.txt"), buildParsers)
            
    protected def buildParsers = List(
        new SimpleParser("sample_parser", buildAnalyzers)
    )
    
    protected def buildAnalyzers = List(
        new SimpleAnalyzer("sample_test", """some regex""".r, "the output message")
    )
}
