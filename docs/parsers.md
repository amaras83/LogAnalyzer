Parsers
=======

Parsers provide a logical way to group logfiles with analyzers. Ideally a parser should be concerned with one type of log file structure.

### Using Parsers

Below we have a simple parser definition for `httpd` access logs. There are two important things to note...

1. `<logfiles>` element this is where you define logfile imports
2. `<analyzers>` element this is where you define analyzer imports

```xml
<parsers>
    <parser name="AccessLogParser">
        <logfiles>
            <!-- 
                Note the lazy log loading which allows us to define 
                multiple files for different operating systems. Files
                or paths that don't exist are simply skipped.
            -->
            <logfile src="/var/log/httpd/access_log" />
            <logfile src="C:\Users\Public\apache\log\access.log" />
        </logfiles>
        
        <analyzers>
            <analyzer name="timestamp-analyzer" />
            <analyzer name="request-analyzer" />
            <analyzer name="access-errors-analyzer" />
        </analyzers>
    </parser>
</parsers>
```

### XML Structure

*parsers*
```xml
<parsers>
    <parser>...</parser>
    ...
</parsers>
```

*parser*
```xml
<parser>
    <logfiles>...</logfiles>
    <analyzers>...</analyzers>
</parser>
```

*logfiles*
```xml
<logfiles>
    <logfile src="/path/to/myfile.log" />
</logfiles>
```

The `src` attribute on the `<logfile>` element points to the logfile you want to import and parse.

*analyzers*
```xml
<analyzers>
    <analyzer name="request-analyzer" />
</analyzers>
```

When importing an analyzer you use the `name` attribute to reference its definition from the `analyzers/*.conf` file you created.
