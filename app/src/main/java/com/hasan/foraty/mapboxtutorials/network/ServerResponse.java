package com.hasan.foraty.mapboxtutorials.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class ServerResponse {

    /**
     * type
     */
    @SerializedName("type")
    private String type;
    /**
     * totalFeatures
     */
    @SerializedName("totalFeatures")
    private String totalFeatures;
    /**
     * features
     */
    @SerializedName("features")
    private List<FeaturesDTO> features;
    /**
     * crs
     */
    @SerializedName("crs")
    private CrsDTO crs;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotalFeatures() {
        return totalFeatures;
    }

    public void setTotalFeatures(String totalFeatures) {
        this.totalFeatures = totalFeatures;
    }

    public List<FeaturesDTO> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeaturesDTO> features) {
        this.features = features;
    }

    public CrsDTO getCrs() {
        return crs;
    }

    public void setCrs(CrsDTO crs) {
        this.crs = crs;
    }

    public static class CrsDTO {
        /**
         * type
         */
        @SerializedName("type")
        private String type;
        /**
         * properties
         */
        @SerializedName("properties")
        private PropertiesDTO properties;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public PropertiesDTO getProperties() {
            return properties;
        }

        public void setProperties(PropertiesDTO properties) {
            this.properties = properties;
        }

        public static class PropertiesDTO {
            /**
             * name
             */
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class FeaturesDTO {
        /**
         * type
         */
        @SerializedName("type")
        private String type;
        /**
         * id
         */
        @SerializedName("id")
        private String id;
        /**
         * geometry
         */
        @SerializedName("geometry")
        private GeometryDTO geometry;
        /**
         * geometryName
         */
        @SerializedName("geometry_name")
        private String geometryName;
        /**
         * properties
         */
        @SerializedName("properties")
        private PropertiesDTO properties;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public GeometryDTO getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryDTO geometry) {
            this.geometry = geometry;
        }

        public String getGeometryName() {
            return geometryName;
        }

        public void setGeometryName(String geometryName) {
            this.geometryName = geometryName;
        }

        public PropertiesDTO getProperties() {
            return properties;
        }

        public void setProperties(PropertiesDTO properties) {
            this.properties = properties;
        }

        public static class GeometryDTO {
            /**
             * type
             */
            @SerializedName("type")
            private String type;
            /**
             * coordinates
             */
            @SerializedName("coordinates")
            private List<List<List<List<Double>>>> coordinates;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<List<List<List<Double>>>> getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(List<List<List<List<Double>>>> coordinates) {
                this.coordinates = coordinates;
            }
        }

        public static class PropertiesDTO {
            /**
             * name
             */
            @SerializedName("name")
            private String name;
            /**
             * description
             */
            @SerializedName("description")
            private String description;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }
}
