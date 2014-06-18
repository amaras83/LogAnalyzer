/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.service

import mcross1882.loganalyzer.export.{Export, ExportFactory, FileExport}
import mcross1882.loganalyzer.analyzer.SimpleAnalyzer
import mcross1882.loganalyzer.parser.SimpleParser
import mcross1882.loganalyzer.test.DefaultTestSuite
import mcross1882.loganalyzer.service.{Service, ServiceFactory}
import scala.util.matching.Regex

class ServiceSpec extends DefaultTestSuite {

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
    
    "export" should "write the service results to all the associated exports" in {
        val service = buildService
        service.export
    }
    
    protected def buildService = 
        new Service("test_service", "A Test Service", buildParsers, List(
            ExportFactory.createFileExport("src/test/resources/fixtures/small-log-out.txt")
        ))
            
    protected def buildParsers = List(
        new SimpleParser("sample_parser", List("src/test/resources/fixtures/parser-spec.txt"), buildAnalyzers)
    )
    
    protected def buildAnalyzers = List(
        new SimpleAnalyzer("timestamp", "timestamp", new Regex("""\[(\d+-\d+-\d+)\]""", "timestamp"), "$timestamp")
    )
}
