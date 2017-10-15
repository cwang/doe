# doe4j
doe4j is a Distance-On-Earth between points calculator written as a simple Java library with no 3rd-party dependencies.

## Use

It takes latitude and longitude of two points on Earth and calculates the distance between them. 

The format of latitude/longitude can be either

 * Signed decimal e.g. `12.3456` or `-78`
 
or 

 * Deg-Min-Sec-Dir separated by space e.g. `12 34 56 N` or `00 88 88 E`
 
or 

 * Deg-Min-Sec plus Dir with respective symbols/signs in between e.g. `12°34′56″S` or `00°88′88″W`
   (Note that the symbols are `"\u00B0"`, `"\u2032"` and `"\u2033"`)
 
And the unit of length can be

 * M for Miles
 * K for Kilometres
 * N for Nautical Miles 
 
doe4j.Calculator is the main class that defines the set of APIs which is a series of overloaded methods named `distance`. 

 * distance(PointOnEarth p1, PointOnEarth p2, UnitOfLength uol)
 * distance(PointOnEarth p1, PointOnEarth p2, String unit)
 * distance(double lat1, double lon1, double lat2, double lon2, String unit)
 * distance(double lat1, double lon1, double lat2, double lon2, UnitOfLength uol)
 * distance(String lat1, String lon1, String lat2, String lon2, String unit)
 * distance(String lat1, String lon1, String lat2, String lon2, UnitOfLength uol)
 
See javadocs for details.

The jar file is also executable with doe4j.Calculator set as the entry point, therefore run 

``java -jar doe4j-VERSION.jar`` 

should print out the usage text when invoking it directly through command line. 

## Develop

The project uses Maven 3 and Java 8 to build and it is recommended to have [sdkman](http://sdkman.io/) installed. 

