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
import scala.util.matching.Regex

class ServiceSpec extends FlatSpec with Matchers {

    "run" should "iterate through the file list parsing each one iteratively" in {
        val service = buildService
        service.run(List.empty[String])
    }
    
    "print" should "iterate through each parser and print its result" in {
        val service = buildService
        service.run(List.empty[String])
        service.print
    }
    
    it should "iterate only on the dates provided in the dates parameter" in {
        val service = buildService
        service.run(List("2014-05-01"))
        service.print
    }
    
    it should "print empty results if the service was not run" in {
        val service = buildService
        service.print
    }
    
    protected def buildService = 
        new Service("test_service", "A Test Service", List("src/test/resources/sample.txt"), buildParsers)
            
    protected def buildParsers = List(
        new SimpleParser("sample_parser", buildAnalyzers)
    )
    
    protected def buildAnalyzers = List(
        new SimpleAnalyzer("timestamp", new Regex("""\[(\d\d\d\d-\d\d-\d\d)""", "timestamp"), "$timestamp")
    )
}
