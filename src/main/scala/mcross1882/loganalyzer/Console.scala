/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap

/**
 * Command line argument parser
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
class Console {
    /**
     * Class that represents an command line argument
     *
     * @since 1.0
     * @param name the argument name
     * @param description a simple description
     */
    protected case class Argument(name: String, description: String, required: Boolean)

    /**
     * List buffer containing any registered arguments
     *
     * @since 1.0
     */
    protected val _arguments = new ListBuffer[Argument]
    
    /**
     * Add an argument to the console
     *
     * @since  1.0
     * @param  name the argument name
     * @param  description a simple description
     * @param  value the command line value passed in
     * @param  required argument
     * @return self
     */
    def argument(name: String, description: String, required: Boolean): Console = {
        _arguments.append(new Argument(name, description, required))
        this
    }
    
    /**
     * Builds a key value pair of extracted command line arguments
     *
     * @since  1.0
     * @param  inputArguments an array of command line arguments
     * @return key value pair of command line arguments
     */
    def build(inputArguments: Array[String]): Map[String,String] = {
        if (inputArguments.length < _arguments.count(_.required)) {
            throw new IllegalArgumentException("Not enough arguments given see help")
        }
        extractArguments(inputArguments)
    }
    
    /**
     * Prints the help message
     */
    def help: Unit = {
        val builder = new StringBuilder
        builder.append("LogAnalyzer Help\n")
        builder.append("================\n")
        
        for (argument <- _arguments) {
            builder.append("- %-16s%s\n".format(argument.name, argument.description))
        }
        
        println(builder)
    }
        
    /**
     * Extracts command line arguments into a key value Map
     *
     * @since  1.0
     * @param  inputArguments an array of command line arguments
     * @return key vlaue pair of command line arguments
     */
    protected def extractArguments(inputArguments: Array[String]): Map[String,String] = {
        val tempMap = new HashMap[String,String]
        var index: Int = 0
        var tempArg: Argument = null 
        for (value <- inputArguments) {
            tempArg = _arguments(index)
            tempMap.put(tempArg.name, value)
            index += 1
        }
        tempMap.toMap    
    }
}
