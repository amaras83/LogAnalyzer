/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.service

import mcross1882.loganalyzer.analyzer.SimpleAnalyzer
import mcross1882.loganalyzer.parser.SimpleParser
import mcross1882.loganalyzer.test.FactoryTestSuite
import mcross1882.loganalyzer.service.ServiceFactory

class ServiceFactorySpec extends FactoryTestSuite("src/test/resources/conf/services") {

    "createFromXML(...)" should "return a list of services from an external XML file" in {
        val services = ServiceFactory.createFromXml(loadTestFile("default"), buildParsers)
        services(0).name should be("demo")
    }
        
    it should "return an empty List[Service] if no xml services are defined" in {
        val services = ServiceFactory.createFromXml(loadTestFile("empty"), buildParsers)
        
        assert(services.isEmpty)
    }
    
    it should "throw an exception if the file does not exists" in {
        intercept[java.io.FileNotFoundException] {
            val services = ServiceFactory.createFromXml("this_file_does_not_exist.nothing", buildParsers)
        }
    }
    
    it should "throw an exception if the file has broken xml" in {
        intercept[org.xml.sax.SAXParseException] {
            val services = ServiceFactory.createFromXml(loadTestFile("broken"), buildParsers)
        }
    }
    
    protected def buildParsers = List(
        new SimpleParser("sample_parser", buildAnalyzers)
    )
    
    protected def buildAnalyzers = List(
        new SimpleAnalyzer("sample_test", """some regex""".r, "the output message")
    )
}
