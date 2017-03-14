# Word statistics 

## Build:  
  `./gradlew build`

## Run: 
 `java -jar build/libs/demo-0.0.1-SNAPSHOT.jar --library.folder="/library/txtfiles"`         

### Command line parameters:
  - `library.folder`: directory with `*.txt` files for processing.

## Request

  `curl -XGET 'http://localhost:8080/words/{word}/statistic'`
  
  where `{word}` is a placeholder for specific word
