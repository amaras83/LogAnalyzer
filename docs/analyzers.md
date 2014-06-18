Analzyers
=========

Analyzers are a way to parse a given log line. They can be used to parse timestamp, exceptions, etc... Think of them like rules they define what we want to pull out of the log file.

### Using Analyzers

To learn how analyzers work lets example a simple exception analyzer. This would scan log lines for exceptions and store any exceptions that got thrown. Here is the basic definition for our analyzer...

```xml
<analyzers>
    <analyzer 
        name="exception-analyzer" 
        category="Exceptions that were thrown" 
        regex="Caught (\w+)" 
        vars="exception">
        - $exception
    </analyzer>
</analzyers>
```

Now let's break down what each attribute means...

- `name` is a unique name given to the analyzer. This name is used when importing analyzers in your parser `.config` file
- `category` this is a human friendly string that your messages will be group under. This is useful when you have multiple analyzers that you want to show up under the same heading
- `regex` this is what actually does the work. This string is matched against every log line and can extract variables. For instance in this example we use `(\w+)` which pulls out the word after `Caught`
- `vars` this attribute allows us to define aliases for the variables we extract from the `regex` attribute. In this example we're creating a variable called `exception` with the value of `(\w+)`.

Inside the `<analyzer>` element you can put whatever text and formatting you want to have display in the analysis file. Remember though that these messages wil always show up under the `Category` heading. Here we use `- $exception` which will print a dash followed by the exception name.

*Sample Print Result*
```
Exceptions that were thrown
- LogicException: 1
```

This is a relatively simple example however if we wanted to have a "prettier" message we could simple change the output message to something like this...

```
The exception "$exception" was thrown
```

Which would render a result like so...

```
Exceptions that were thrown
The exception LogicException was thrown: 1
```

### Count values

Now you may be wondering why are there `: 1` after those log lines? LogAnalyzer will automatically count how many times a given analyzer matches. So if you had 5 matches in the log files your line would end with `: 5`

### Extracting Multiple Regex Variables

It's also worth noting that you can extract multiple text chunks from the log line lets take the example of a http server log file...

*Sample Http Log File*
```
[2014-05-01 12:00:00] GET /my/uri 200
```

With the above example we could use a `regex` string to extract the request method, uri and status code for any request that had a 500 response..

```xml
<analyzer
    name="request-analyzer"
    category="List of Requests"
    regex="(.+) (GET|POST|HEAD|PUT|DELETE) (/\w+)* (500)"
    vars="method|uri|status">
    $uri [$method, $status]
</analyzer>
```

The analyzer above would produce the following text...

```
List of Requests
/my/uri [GET, 200]: 1
```

### Filtering on Timestamps

LogAnalyzer has a great feature of being able to strip out timestamps and filter on them. This is done by using a analyzer with the name `timestamp`..

```xml
<analyzer name="timestamp" category="timestamp" regex="\[(\d+-\d+-\d+) \d+\:\d+\:\d+\]" vars="timestamp">
    $timestamp
</analyzer>
```

The example above assumes that our log lines contain a chunk of text that may look like this...

```
[2014-05-01 04:35:11] Warning: Some info about what went wrong...
```

The LogAnalyzer would match the text `[2014-05-01 04:35:11]` and will extract `2014-05-01` as the variable `$timestamp`.
LogAnalyzer will then use this value to perform filtering when passing in dates from the command line.
