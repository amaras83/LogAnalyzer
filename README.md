LogAnalyzer
===========

Log analyzer and summary builder written in Scala built for JVM projects


### Overview

Log analyzer uses a set of analyzer, parsers, and services to parse logfiles into condensed summaries. To understand this better
lets look at each piece individually..

#### Condense log files

Convert <a href="https://github.com/mcross1882/LogAnalyzer/tree/master/examples/sample.txt">bloated log files</a> into clean and concise summaries...

```
Demo Diagnostics Report
=======================
Date Range
Parsing records that occurred on 2014-05-01: 12
Parsing records that occurred on 2014-05-02: 16

Emergency
- Whoops something went wrong!: 2

Failed Logins
someone@test.com experienced a failed login: 1

Debugging Noise
Suppressed: 25
```

#### Analyzers

Analyzers are a set of rules that match a regex expression against a single line that is read in from the logfile. They also store simple meta
data like extracted regex text, counters, and the category to which the analyzer belongs too. Analyzers are written in XML (see sample below)
and are located in `conf/dist/analyzers`.

```xml
<analyzers>
    <analyzer name="timestamp" category="timestamp" regex="\[(\d+-\d+-\d+) \d+\:\d+\:\d+\]" vars="timestamp">
        $timestamp
    </analyzer>
    
    <analyzer name="date-analyzer" category="Date Range" regex="\[(\d+-\d+-\d+) \d+\:\d+\:\d+\]" vars="timestamp">
        Parsing records that occurred on $timestamp
    </analyzer>
    
    <analyzer name="emergency-analyzer" category="Emergency" regex="Emergency: (.*)" vars="message">
        - $message
    </analyzer>
    
    <analyzer name="login-analyzer" category="Failed Logins" regex="login (\w+) for (\w+@\w+.\w+)" vars="status|email">
        $email experienced a $status login
    </analyzer>
    
    <analyzer name="noise-analyzer" category="Debugging Noise" regex="Debug: (.*)">
        Suppressed
    </analyzer>
</analyzers>
```

#### Parsers

Parsers are simply a collection of analyzers. They are reusable building blocks that define the types of parsing that will be done on the
input files.

```xml
<parsers>
    <parser name="DemoParser" type="SimpleParser">
        <logfiles>
            <logfile src="examples/sample.txt" />
        </logfiles>

        <analyzers>
            <analyzer name="timestamp" />
            <analyzer name="date-analyzer" />
            <analyzer name="emergency-analyzer" />
            <analyzer name="login-analyzer" />
            <analyzer name="noise-analyzer" />
        </analyzers>
    </parser>
</parsers>
```


#### Services

Services represent an application or service that you want to perform log analysis on (e.g. httpd, php, etc...). They contain a list of files
that should be parsed as well as a list of parsers that should be used on the input.

```xml
<services>
    <service name="demo" title="Demo Diagnostics Report">
        <parsers>
            <parser name="DemoParser" />
        </parsers>
        
        <exports>
            <file src="examples/output.txt" />

            <email name="gmail" to="recipient@gmail.com" from="sender@gmail.com" subject="Demo Diagnostics Demo" />
        </exports>
    </service>
</services>
```

### Exports

Services support the ability to export your data in different formats such as...

- FileExports which write your log analysis to local files
- EmailExports which send your log analysis to a given email

### Extending the API

Need a custom parser or analyzer implementation? No problem simply extend the respective trait (e.g. `Parser`, `Analyzer`, or `Service`) and
set the `type` attribute within the XML files to use your newly created class.
