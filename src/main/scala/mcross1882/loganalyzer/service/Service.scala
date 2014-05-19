/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.service

import mcross1882.loganalyzer.parser.Parser
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.xml.XML

/**
 * Value object to store information about a given
 * service such as the files it uses and the parsers
 * that it requires
 *
 * @since  1.0
 * @param  name the reference name for this service
 * @param  title the title to display when rendering to stdout
 * @param  files a list of logfiles to watch
 * @param  the parsers that should be used on the logfiles
 */
case class Service(name: String, title: String, files: List[String], parsers: List[Parser]) {
    /**
     * Run will loop through all the logfiles and run the parsers
     * on the input provided by the logfiles
     *
     * @since  1.0
     * @return Unit
     */
    def run: Unit = for (filename <- files) readFile(filename)
    
    /**
     * Print all the results from the parsers
     *
     * @since  1.0
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
     * @param  filename the file to parse
     * @return Unit
     */
    protected def readFile(filename: String): Unit = {
        try {
            Source.fromFile(filename).getLines.foreach(processLine)
        } catch {
            case e: Exception => println("An error occurred while reading %s. (%s)".format(filename, e.getMessage))
        }
    }
    
    /**
     * Feeds a single line to all of the parsers
     *
     * @since  1.0
     * @param  line the input line
     * @return Unit
     */
    protected def processLine(line: String): Unit = 
        for (parser <- parsers) parser.parseLine(line)
}
