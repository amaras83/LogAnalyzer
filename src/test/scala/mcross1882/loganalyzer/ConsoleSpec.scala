/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test

import mcross1882.loganalyzer.Console

/**
 * Tests Console
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
class ConsoleSpec extends DefaultTestSuite {
    
    "argument" should "register an argument with a name, description and value" in {
        val console = buildConsole
        val service = "my_serive"
        val dates = "2014-05-01,2014-05-02"
        
        val args = console.build(Array(service, dates))
        
        args("service") should be(service)
        args("dates") should be(dates)
    }
    
    "print" should "print the required arguments with descriptions" in {
        val console = buildConsole
        console.help
    }
    
    protected def buildConsole: Console = {
        val console = new Console
        console
            .argument("service", "The service to run")
            .argument("dates", "Comma separated dates to filter on")
    }
}