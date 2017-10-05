# weather-observations
https://gist.github.com/dbousamra/66cab2034e4f212553fb

### Prerequisites

- Java 8
- Gradle

### Build Tasks 

```sh
./gradlew build
./gradlew test
```
### Distribution Tasks

```sh
./gradlew distZip
./gradlew distTar
```

### Running the Application

#### Install the Distribution

```sh
./gradlew distZip
cd build/distributions/
unzip weather-observations.zip
```

#### Generate Test Data

```sh
cd weather-observations
./bin/generate-log data.log 1000
```

#### Read Log File

```sh
cd weather-observations
./bin/read-log data.log report.txt US
```

#### Implementation Notes
1. `LogFileGenerator` writes each lines sequentially and can take a while to complete when generating a large file. The performance can be improved by having multiple threads writing to different files and then merging them afterwards.
1. `LogEntryGenerator` can be further improved by exposing the error occurrence constants as configurable properties.
1. Also, for the sake of simplicity `LogEntryGenerator` only generates integer numeric values.
1. `LogEntryParser` requires all fields to be valid in order to parse an entry -- this has been done for the sake of time.
1. `LogFileReader` accepts a "locale" which controls which units to use for **both** distance and temperature.
1. `main()` methods are attached to both `LogFileGenerator` and `LogFileReader`. Ideally, these should be separate classes.