# doe4j
doe4j is a Distance-On-Earth between points calculator written as a simple Java library with no 3rd-party dependencies.

## Use

It takes latitude and longitude of two points on Earth and calculates the distance between them. 

The format of latitude/longitude can be either

 * Signed decimal degrees
 
or 

 * Deg-Min-Sec-Dir separated by space
 
And the unit of length can be

 * M for Miles
 * K for Kilometres
 * N for Nautical Miles 
 
doe4j.Calculator is the main class that defines the set of APIs.

## Develop

The project uses Maven 3 and Java 8 to build and it is recommended to have [sdkman](http://sdkman.io/) installed. 

