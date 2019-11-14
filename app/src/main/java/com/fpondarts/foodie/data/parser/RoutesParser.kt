package com.fpondarts.foodie.data.parser

import android.graphics.Color
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException



class RoutesParser() {


    fun parse(json:JSONObject):List<PolylineOptions> {

        return getPolylineOptions(parseJSON(json))

    }

    fun getPolylineOptions(result: List<List<HashMap<String, String>>>):ArrayList<PolylineOptions> {
        var points: ArrayList<LatLng>
        var resultOptions = ArrayList<PolylineOptions>()
        // Traversing through all the routes
        for (i in result.indices) {
            points = ArrayList()
            val lineOptions = PolylineOptions()
            // Fetching i-th route
            val path = result[i]
            // Fetching all the points in i-th route
            for (j in path.indices) {
                val point = path[j]
                val lat = java.lang.Double.parseDouble(point["lat"]!!)
                val lng = java.lang.Double.parseDouble(point["lng"]!!)
                val position = LatLng(lat, lng)
                points.add(position)
            }

            lineOptions.addAll(points);

            lineOptions.width(10f)
            lineOptions.color(Color.MAGENTA)

            Log.d("mylog", "onPostExecute lineoptions decoded")

            resultOptions.add(lineOptions)
        }

        return resultOptions
    }

    fun parseJSON(jObject: JSONObject): List<List<HashMap<String, String>>> {

        val routes = ArrayList<List<HashMap<String,String>>>()
        val jRoutes: JSONArray
        var jLegs: JSONArray
        var jSteps: JSONArray
        try {
            jRoutes = jObject.getJSONArray("routes")
            /** Traversing all routes  */
            for (i in 0 until jRoutes.length()) {
                jLegs = (jRoutes.get(i) as JSONObject).getJSONArray("legs")
                val path = ArrayList<HashMap<String,String>>()
                /** Traversing all legs  */
                for (j in 0 until jLegs.length()) {
                    jSteps = (jLegs.get(j) as JSONObject).getJSONArray("steps")

                    /** Traversing all steps  */
                    for (k in 0 until jSteps.length()) {
                        var polyline = ""
                        polyline =
                            ((jSteps.get(k) as JSONObject).get("polyline") as JSONObject).get("points") as String
                        val list = decodePoly(polyline)

                        /** Traversing all points  */
                        for (l in list.indices) {
                            val hm = HashMap<String,String>()
                            hm.put("lat", java.lang.Double.toString(list[l].latitude))
                            hm.put("lng", java.lang.Double.toString(list[l].longitude))
                            path.add(hm)
                        }
                    }
                    routes.add(path)
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {

        }

        return routes
    }


    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private fun decodePoly(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }
}