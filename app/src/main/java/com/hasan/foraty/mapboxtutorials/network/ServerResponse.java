package com.hasan.foraty.mapboxtutorials.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class ServerResponse {
    public ServerResponse(String type, String totalFeatures, List<FeaturesDTO> features, CrsDTO crs) {
        this.type = type;
        this.totalFeatures = totalFeatures;
        this.features = features;
        this.crs = crs;
    }

    /**
     * type
     */
    @SerializedName("type")
    public String type;
    /**
     * totalFeatures
     */
    @SerializedName("totalFeatures")
    public String totalFeatures;
    /**
     * features
     */
    @SerializedName("features")
    public List<FeaturesDTO> features;
    /**
     * crs
     */
    @SerializedName("crs")
    public CrsDTO crs;

    public static class CrsDTO {
        /**
         * type
         */
        @SerializedName("type")
        public String type;
        /**
         * properties
         */
        @SerializedName("properties")
        public PropertiesDTO properties;

        public static class PropertiesDTO {
            /**
             * name
             */
            @SerializedName("name")
            public String name;
        }
    }

    public static class FeaturesDTO {
        /**
         * type
         */
        @SerializedName("type")
        public String type;
        /**
         * id
         */
        @SerializedName("id")
        public String id;
        /**
         * geometry
         */
        @SerializedName("geometry")
        public GeometryDTO geometry;
        /**
         * geometryName
         */
        @SerializedName("geometry_name")
        public String geometryName;
        /**
         * properties
         */
        @SerializedName("properties")
        public PropertiesDTO properties;

        public static class GeometryDTO {
            /**
             * type
             */
            @SerializedName("type")
            public String type;
            /**
             * coordinates
             */
            @SerializedName("coordinates")
            public List<List<List<List<Double>>>> coordinates;
        }

        public static class PropertiesDTO {
            /**
             * name
             */
            @SerializedName("name")
            public Object name;
            /**
             * description
             */
            @SerializedName("description")
            public Object description;
        }
    }
}
