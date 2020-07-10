package com.demo.basic.data.beans;

import java.util.List;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class MapBean {

    private String type;
    private List<FeaturesBean> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FeaturesBean> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeaturesBean> features) {
        this.features = features;
    }

    public static class FeaturesBean {
        /**
         * type : Feature
         * properties : {"adcode":110101,"name":"东城区","center":[116.418757,39.917544],"centroid":[116.416739,39.912912],"childrenNum":0,"level":"district","parent":{"adcode":110000},"subFeatureIndex":0,"acroutes":[100000,110000]}
         * geometry : {"type":"MultiPolygon","coordinates":[[[[116.387658,39.96093],[116.389498,39.96314],[116.40788,39.962182],[116.407504,39.973995],[116.411101,39.97146],[116.411415,39.964928],[116.414196,39.962182],[116.424861,39.962279],[116.429002,39.957274],[116.429483,39.950155],[116.436698,39.949245],[116.435422,39.952121],[116.442239,39.9497],[116.440566,39.945295],[116.446338,39.946205],[116.443703,39.936663],[116.443682,39.928664],[116.434314,39.92868],[116.434983,39.913964],[116.436488,39.902042],[116.448722,39.903246],[116.446819,39.900042],[116.447154,39.894186],[116.450876,39.894088],[116.450939,39.890249],[116.444059,39.890038],[116.445648,39.879283],[116.44364,39.87284],[116.442574,39.87188],[116.423209,39.872824],[116.413652,39.871148],[116.41589,39.863645],[116.41246,39.858942],[116.406856,39.859967],[116.3955,39.858682],[116.394956,39.862734],[116.387888,39.867372],[116.380632,39.866054],[116.38059,39.871148],[116.399097,39.872205],[116.397612,39.898675],[116.396086,39.89944],[116.395563,39.907995],[116.392259,39.907881],[116.392175,39.92242],[116.399474,39.923574],[116.396692,39.928306],[116.396169,39.94006],[116.394266,39.940629],[116.393723,39.957371],[116.38678,39.957014],[116.387658,39.96093]]]]}
         */

        private String type;
        private PropertiesBean properties;
        private GeometryBean geometry;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public PropertiesBean getProperties() {
            return properties;
        }

        public void setProperties(PropertiesBean properties) {
            this.properties = properties;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        @Override
        public int hashCode() {
            return type.hashCode() + properties.hashCode();
        }

        public static class PropertiesBean {
            /**
             * adcode : 110101
             * name : 东城区
             * center : [116.418757,39.917544]
             * centroid : [116.416739,39.912912]
             * childrenNum : 0
             * level : district
             * parent : {"adcode":110000}
             * subFeatureIndex : 0
             * acroutes : [100000,110000]
             */

            private int adcode;
            private String name;
            private int childrenNum;
            private String level;
            private ParentBean parent;
            private int subFeatureIndex;
            private List<Double> center;
            private List<Double> centroid;
            private List<Integer> acroutes;

            @Override
            public int hashCode() {
                return adcode;
            }

            public int getAdcode() {
                return adcode;
            }

            public void setAdcode(int adcode) {
                this.adcode = adcode;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getChildrenNum() {
                return childrenNum;
            }

            public void setChildrenNum(int childrenNum) {
                this.childrenNum = childrenNum;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public ParentBean getParent() {
                return parent;
            }

            public void setParent(ParentBean parent) {
                this.parent = parent;
            }

            public int getSubFeatureIndex() {
                return subFeatureIndex;
            }

            public void setSubFeatureIndex(int subFeatureIndex) {
                this.subFeatureIndex = subFeatureIndex;
            }

            public List<Double> getCenter() {
                return center;
            }

            public void setCenter(List<Double> center) {
                this.center = center;
            }

            public List<Double> getCentroid() {
                return centroid;
            }

            public void setCentroid(List<Double> centroid) {
                this.centroid = centroid;
            }

            public List<Integer> getAcroutes() {
                return acroutes;
            }

            public void setAcroutes(List<Integer> acroutes) {
                this.acroutes = acroutes;
            }

            public static class ParentBean {
                /**
                 * adcode : 110000
                 */

                private int adcode;

                public int getAdcode() {
                    return adcode;
                }

                public void setAdcode(int adcode) {
                    this.adcode = adcode;
                }
            }
        }

        public static class GeometryBean {
            /**
             * type : MultiPolygon
             * coordinates : [[[[116.387658,39.96093],[116.389498,39.96314],[116.40788,39.962182],[116.407504,39.973995],[116.411101,39.97146],[116.411415,39.964928],[116.414196,39.962182],[116.424861,39.962279],[116.429002,39.957274],[116.429483,39.950155],[116.436698,39.949245],[116.435422,39.952121],[116.442239,39.9497],[116.440566,39.945295],[116.446338,39.946205],[116.443703,39.936663],[116.443682,39.928664],[116.434314,39.92868],[116.434983,39.913964],[116.436488,39.902042],[116.448722,39.903246],[116.446819,39.900042],[116.447154,39.894186],[116.450876,39.894088],[116.450939,39.890249],[116.444059,39.890038],[116.445648,39.879283],[116.44364,39.87284],[116.442574,39.87188],[116.423209,39.872824],[116.413652,39.871148],[116.41589,39.863645],[116.41246,39.858942],[116.406856,39.859967],[116.3955,39.858682],[116.394956,39.862734],[116.387888,39.867372],[116.380632,39.866054],[116.38059,39.871148],[116.399097,39.872205],[116.397612,39.898675],[116.396086,39.89944],[116.395563,39.907995],[116.392259,39.907881],[116.392175,39.92242],[116.399474,39.923574],[116.396692,39.928306],[116.396169,39.94006],[116.394266,39.940629],[116.393723,39.957371],[116.38678,39.957014],[116.387658,39.96093]]]]
             */

            private String type;
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
    }
}
