/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test

/**
 * Test suite with some helper methods for factories requiring config files
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 * @param  configPath the configuration path to the app directory
 */
class FactoryTestSuite(configPath: String) extends DefaultTestSuite {
    /**
     * Builds a relative filename to easily load external test files
     **
     * @since  1.0
     * @param  serviceName the name of the service to load
     * return  a concatenated string equal to configPath + fileSeparator + serviceName
     */
    protected def loadTestFile(serviceName: String): String =
        "%s%s%s.config".format(configPath, System.getProperty("file.separator"), serviceName)
}
