package doe4j;

/**
 * Great-Circle Distance calculator between two points on Earth.
 *
 * See https://en.wikipedia.org/wiki/Great-circle_distance for details.
 *
 * This code is largely adopted from the sample code made available on http://www.geodatasource.com/developers/java and
 * the original comment header has been copied below. Please note that the API has been changed and having the original
 * here is to show respect to its original author. In terms of licensing, this library has also adopted the original
 * licensing terms which is LGPLv3 to ensure licensing compliance.
 *
 * Another source of inspiration is from http://www.movable-type.co.uk/scripts/latlong.html which has a worked example
 * of similar distance calculator in Javascript and more detailed explanation on the method which is however different
 * from the method used here.
 *
 */
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::                                                                         :*/
/*::  This routine calculates the distance between two points (given the     :*/
/*::  latitude/longitude of those points). It is being used to calculate     :*/
/*::  the distance between two locations using GeoDataSource (TM) prodducts  :*/
/*::                                                                         :*/
/*::  Definitions:                                                           :*/
/*::    South latitudes are negative, east longitudes are positive           :*/
/*::                                                                         :*/
/*::  Passed to function:                                                    :*/
/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
/*::    unit = the unit you desire for results                               :*/
/*::           where: 'M' is statute miles (default)                         :*/
/*::                  'K' is kilometers                                      :*/
/*::                  'N' is nautical miles                                  :*/
/*::  Worldwide cities and other features databases with latitude longitude  :*/
/*::  are available at http://www.geodatasource.com                          :*/
/*::                                                                         :*/
/*::  For enquiries, please contact sales@geodatasource.com                  :*/
/*::                                                                         :*/
/*::  Official Web site: http://www.geodatasource.com                        :*/
/*::                                                                         :*/
/*::           GeoDataSource.com (C) All Rights Reserved 2017                :*/
/*::                                                                         :*/
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
public class Calculator {

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        return distance(lat1, lon1, lat2, lon2, UnitOfLength.valueOf(unit));
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, UnitOfLength uol) {
        return distance(new PointOnEarth(lat1, lon1), new PointOnEarth(lat2, lon2), uol);
    }

    public static double distance(String lat1, String lon1, String lat2, String lon2, String unit) {
        return distance(lat1, lon1, lat2, lon2, UnitOfLength.valueOf(unit));
    }

    public static double distance(String lat1, String lon1, String lat2, String lon2, UnitOfLength uol) {
        return distance(new PointOnEarth(lat1, lon1), new PointOnEarth(lat2, lon2), uol);
    }


    public static double distance(PointOnEarth p1, PointOnEarth p2, UnitOfLength uol) {
        double theta = p1.getLongitude() - p2.getLongitude();
        double dist = Math.sin(Math.toRadians(p1.getLatitude())) * Math.sin(Math.toRadians(p2.getLatitude()))
                + Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude())) * Math.cos(Math.toRadians(theta));
//        dist = Math.acos(dist);
//        dist = Math.toDegrees(dist);
//        dist = dist * 60 * 1.1515 * uol.getRatio();

        dist = Math.toDegrees(Math.acos(dist)) * 60 * 1.1515 * uol.getRatio();

        return dist;
    }
}
