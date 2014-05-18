/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.service

import java.io.File
import java.util.Scanner
import mcross1882.loganalyzer.parser.Parser
import scala.collection.mutable.ListBuffer
import scala.xml.XML

/**
 * Value object to store information about a given
 * service such as the files it uses and the parsers
 * that it requires
 *
 * @since  1.0
 * @access public
 * @param  String name the reference name for this service
 * @param  String title the title to display when rendering to stdout
 * @param  List[String] files a list of logfiles to watch
 * @param  List[Parser] the parsers that should be used on the logfiles
 */
class Service(name: String, title: String, files: List[String], parsers: List[Parser]) {
    /**
     * Run will loop through all the logfiles and run the parsers
     * on the input provided by the logfiles
     *
     * @since  1.0
     * @access public
     * @return Unit
     */
    def run: Unit = for (filename <- files) readFile(filename)
    
    /**
     * Print all the results from the parsers
     *
     * @since  1.0
     * @access public
     * @return Unit
     */
    def print: Unit = {
        println("%s\n%s".format(title, "=" * title.length))
        for (parser <- parsers) parser.printResults
    }
    
    /**
     * Reads a file and loops through it line-by-line feeding
     * it to all of the registered parsers
     *
     * @since  1.0
     * @access protected
     * @param  String filename the file to parse
     * @return Unit
     */
    protected def readFile(filename: String): Unit = {
        var reader: Scanner = null
        try {
            reader = new Scanner(new File(filename))
            while (reader.hasNext) {
                processLine(reader.nextLine)
            }
        } catch {
            case e: Exception => println("An error occurred while reading %s. (%s)".format(filename, e.getMessage))
        } finally {
            if (null != reader) {
                reader.close
            }
        }
    }
    
    /**
     * Feeds a single line to all of the parsers
     *
     * @since  1.0
     * @access protected
     * @param  String line the input line
     * @return Unit
     */
    protected def processLine(line: String): Unit = 
        for (parser <- parsers) parser.parseLine(line)
}
