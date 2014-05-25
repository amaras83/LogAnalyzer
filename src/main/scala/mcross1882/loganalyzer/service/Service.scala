/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.service

import mcross1882.loganalyzer.export.Export
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
 * @param  exports the export objects to write too
 * @param  the parsers that should be used on the logfiles
 */
case class Service(name: String, 
    title: String, 
    files: List[String], 
    parsers: List[Parser],
    exports: List[Export]) {
    /**
     * "Pretty" formatted title for writing to stdout or exports
     *
     * @since  1.0
     */
    val prettyTitle = "%s\n%s\n".format(title, "=" * title.length)
    
    /**
     * Run will loop through all the logfiles and run the parsers
     * on the input provided by the logfiles
     *
     * @since  1.0
     * @param  dates the dates to filter on
     * @return Unit
     */
    def run(dates: List[String]): Unit = for (filename <- files) readFile(filename, dates)
    
    /**
     * Print all the results from the parsers
     *
     * @since  1.0
     * @return Unit
     */
    def print: Unit = {
        println(prettyTitle)
        for (parser <- parsers) println(parser.results)
    }
    
    /**
     * Export the services results
     *
     * @since  1.0
     */
    def export: Unit = {
        val builder = new StringBuilder(prettyTitle)
        for (parser <- parsers) {
            builder.append(parser.results)
        }
        
        val message = builder.toString
        for (export <- exports) {
            export.send(message)
        }
    }
    
    /**
     * Reads a file and loops through it line-by-line feeding
     * it to all of the registered parsers
     *
     * @since  1.0
     * @param  filename the file to parse
     * @param  dates a list of dates to filter on
     * @return Unit
     */
    protected def readFile(filename: String, dates: List[String]): Unit = {
        try {
            Source.fromFile(filename).getLines.foreach{ line =>
                processLine(line, dates)
            }
        } catch {
            case e: Exception => println("An error occurred while reading %s. (%s)".format(filename, e.getMessage))
        }
    }
    
    /**
     * Feeds a single line to all of the parsers
     *
     * @since  1.0
     * @param  line the input line
     * @param  dates the dates to filter on
     * @return Unit
     */
    protected def processLine(line: String, dates: List[String]): Unit = 
        for (parser <- parsers) parser.parseLine(line, dates)   
}
