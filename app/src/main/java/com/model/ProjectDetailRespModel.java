package com.model;

import com.google.gson.annotations.SerializedName;
import com.VO.MediaGellaryVO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 20-Jan-17.
 */

public class ProjectDetailRespModel  extends BaseRespModel implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private String sector;
        private int is_unit_detail_available;//0 and 1
        private String infra;
        private String location;
        private int no_media_tv;
        private String builder_contactno;
        private boolean is_comment_list_needed;
        private String edc_idc;
        private String gst_vat;
        private ArrayList<Recreation> recreation;
        private ArrayList<String> type;
        private int total_units;
        private String proj_name;
        private String city;
        private int unit_block_no;
        private boolean is_static_unit;
        private String parking_charges;
        private String ratings_average;
        private String description;
        private ArrayList<AmenitiesData>Amenities;
        private String club_charges;
        private String lat_lng;
        private ArrayList<Services>services;
        private String price_one_year;
        private boolean user_favourite;
        private String status;
        private int no_media_construction;
        // private String project_landmark":[],
        private String prop_price_persq;
        private String ibms;
        private String discount;
        private String proj_unit_type;
        private String price;
        private ArrayList<PriceTrends> price_trends;
        private String locality_insights;
        private String developer_name;
        private String availabe_unit;
        private ArrayList<String> unit_ids;
        private String project_url;
        private boolean sample_flat;
        private String max_price;
        private ArrayList<Flooring> flooring;
        private String builder_name;
        private String floor_no;
        private int no_media_video;
        private String area_delevered;
        private String builder_description;
        private String state;
        private String project_logo;
        private String tvadd;
        private String max_area_range;
        private String currency;
        private String builder_logo;
        private String id;
        private String builder_mobile;
        private String project_plan_img;
        private ArrayList<String> wow;
        private String video_walk_through;
        private String min_price;
        private String ifms;
        private ArrayList<MediaGellaryVO> media_gallery;
        private int no_media_broucher;
        private String min_area_range;
        private String possession_dt;
        private String life_style;
        private String establish_year;
        private String needs;
        private String exactlocation;
        @SerializedName("return")
        private String returns;
        private Cords cords;
        private ArrayList<Fitting> fitting;
        private  ArrayList<Walls> walls;
        private ArrayList<Safety> safety;
        private int no_media_image;
        private String address;
        private ArrayList<UnitType> unit_type;
        private ArrayList<CommentsDetail> comments_detail;

        public ArrayList<MediaGellaryVO> getMedia_gallery() {
            return media_gallery;
        }

        public void setMedia_gallery(ArrayList<MediaGellaryVO> media_gallery) {
            this.media_gallery = media_gallery;
        }

        public String getSector() {
            return sector;
        }

        public void setSector(String sector) {
            this.sector = sector;
        }

        public int getIs_unit_detail_available() {
            return is_unit_detail_available;
        }

        public void setIs_unit_detail_available(int is_unit_detail_available) {
            this.is_unit_detail_available = is_unit_detail_available;
        }

        public String getInfra() {
            return infra;
        }

        public void setInfra(String infra) {
            this.infra = infra;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getNo_media_tv() {
            return no_media_tv;
        }

        public void setNo_media_tv(int no_media_tv) {
            this.no_media_tv = no_media_tv;
        }

        public String getBuilder_contactno() {
            return builder_contactno;
        }

        public void setBuilder_contactno(String builder_contactno) {
            this.builder_contactno = builder_contactno;
        }

        public boolean is_comment_list_needed() {
            return is_comment_list_needed;
        }

        public void setIs_comment_list_needed(boolean is_comment_list_needed) {
            this.is_comment_list_needed = is_comment_list_needed;
        }

        public String getEdc_idc() {
            return edc_idc;
        }

        public void setEdc_idc(String edc_idc) {
            this.edc_idc = edc_idc;
        }

        public String getGst_vat() {
            return gst_vat;
        }

        public void setGst_vat(String gst_vat) {
            this.gst_vat = gst_vat;
        }

        public ArrayList<Recreation> getRecreation() {
            return recreation;
        }

        public void setRecreation(ArrayList<Recreation> recreation) {
            this.recreation = recreation;
        }

        public ArrayList<String> getType() {
            return type;
        }

        public void setType(ArrayList<String> type) {
            this.type = type;
        }

        public int getTotal_units() {
            return total_units;
        }

        public void setTotal_units(int total_units) {
            this.total_units = total_units;
        }

        public String getProj_name() {
            return proj_name;
        }

        public void setProj_name(String proj_name) {
            this.proj_name = proj_name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getUnit_block_no() {
            return unit_block_no;
        }

        public void setUnit_block_no(int unit_block_no) {
            this.unit_block_no = unit_block_no;
        }

        public boolean is_static_unit() {
            return is_static_unit;
        }

        public void setIs_static_unit(boolean is_static_unit) {
            this.is_static_unit = is_static_unit;
        }

        public String getParking_charges() {
            return parking_charges;
        }

        public void setParking_charges(String parking_charges) {
            this.parking_charges = parking_charges;
        }

        public String getRatings_average() {
            return ratings_average;
        }

        public void setRatings_average(String ratings_average) {
            this.ratings_average = ratings_average;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ArrayList<AmenitiesData> getAmenities() {
            return Amenities;
        }

        public void setAmenities(ArrayList<AmenitiesData> amenities) {
            Amenities = amenities;
        }

        public String getClub_charges() {
            return club_charges;
        }

        public void setClub_charges(String club_charges) {
            this.club_charges = club_charges;
        }

        public String getLat_lng() {
            return lat_lng;
        }

        public void setLat_lng(String lat_lng) {
            this.lat_lng = lat_lng;
        }

        public ArrayList<Services> getServices() {
            return services;
        }

        public void setServices(ArrayList<Services> services) {
            this.services = services;
        }

        public String getPrice_one_year() {
            return price_one_year;
        }

        public void setPrice_one_year(String price_one_year) {
            this.price_one_year = price_one_year;
        }

        public boolean isUser_favourite() {
            return user_favourite;
        }

        public void setUser_favourite(boolean user_favourite) {
            this.user_favourite = user_favourite;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getNo_media_construction() {
            return no_media_construction;
        }

        public void setNo_media_construction(int no_media_construction) {
            this.no_media_construction = no_media_construction;
        }

        public String getProp_price_persq() {
            return prop_price_persq;
        }

        public void setProp_price_persq(String prop_price_persq) {
            this.prop_price_persq = prop_price_persq;
        }

        public String getIbms() {
            return ibms;
        }

        public void setIbms(String ibms) {
            this.ibms = ibms;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getProj_unit_type() {
            return proj_unit_type;
        }

        public void setProj_unit_type(String proj_unit_type) {
            this.proj_unit_type = proj_unit_type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public ArrayList<PriceTrends> getPrice_trends() {
            return price_trends;
        }

        public void setPrice_trends(ArrayList<PriceTrends> price_trends) {
            this.price_trends = price_trends;
        }

        public String getLocality_insights() {
            return locality_insights;
        }

        public void setLocality_insights(String locality_insights) {
            this.locality_insights = locality_insights;
        }

        public String getDeveloper_name() {
            return developer_name;
        }

        public void setDeveloper_name(String developer_name) {
            this.developer_name = developer_name;
        }

        public String getAvailabe_unit() {
            return availabe_unit;
        }

        public void setAvailabe_unit(String availabe_unit) {
            this.availabe_unit = availabe_unit;
        }

        public ArrayList<String> getUnit_ids() {
            return unit_ids;
        }

        public void setUnit_ids(ArrayList<String> unit_ids) {
            this.unit_ids = unit_ids;
        }

        public String getProject_url() {
            return project_url;
        }

        public void setProject_url(String project_url) {
            this.project_url = project_url;
        }

        public boolean isSample_flat() {
            return sample_flat;
        }

        public void setSample_flat(boolean sample_flat) {
            this.sample_flat = sample_flat;
        }

        public String getMax_price() {
            return max_price;
        }

        public void setMax_price(String max_price) {
            this.max_price = max_price;
        }

        public ArrayList<Flooring> getFlooring() {
            return flooring;
        }

        public void setFlooring(ArrayList<Flooring> flooring) {
            this.flooring = flooring;
        }

        public String getBuilder_name() {
            return builder_name;
        }

        public void setBuilder_name(String builder_name) {
            this.builder_name = builder_name;
        }

        public String getFloor_no() {
            return floor_no;
        }

        public void setFloor_no(String floor_no) {
            this.floor_no = floor_no;
        }

        public int getNo_media_video() {
            return no_media_video;
        }

        public void setNo_media_video(int no_media_video) {
            this.no_media_video = no_media_video;
        }

        public String getArea_delevered() {
            return area_delevered;
        }

        public void setArea_delevered(String area_delevered) {
            this.area_delevered = area_delevered;
        }

        public String getBuilder_description() {
            return builder_description;
        }

        public void setBuilder_description(String builder_description) {
            this.builder_description = builder_description;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getProject_logo() {
            return project_logo;
        }

        public void setProject_logo(String project_logo) {
            this.project_logo = project_logo;
        }

        public String getTvadd() {
            return tvadd;
        }

        public void setTvadd(String tvadd) {
            this.tvadd = tvadd;
        }

        public String getMax_area_range() {
            return max_area_range;
        }

        public void setMax_area_range(String max_area_range) {
            this.max_area_range = max_area_range;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getBuilder_logo() {
            return builder_logo;
        }

        public void setBuilder_logo(String builder_logo) {
            this.builder_logo = builder_logo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBuilder_mobile() {
            return builder_mobile;
        }

        public void setBuilder_mobile(String builder_mobile) {
            this.builder_mobile = builder_mobile;
        }

        public String getProject_plan_img() {
            return project_plan_img;
        }

        public void setProject_plan_img(String project_plan_img) {
            this.project_plan_img = project_plan_img;
        }

        public ArrayList<String> getWow() {
            return wow;
        }

        public void setWow(ArrayList<String> wow) {
            this.wow = wow;
        }

        public String getVideo_walk_through() {
            return video_walk_through;
        }

        public void setVideo_walk_through(String video_walk_through) {
            this.video_walk_through = video_walk_through;
        }

        public String getMin_price() {
            return min_price;
        }

        public void setMin_price(String min_price) {
            this.min_price = min_price;
        }

        public String getIfms() {
            return ifms;
        }

        public void setIfms(String ifms) {
            this.ifms = ifms;
        }



        public int getNo_media_broucher() {
            return no_media_broucher;
        }

        public void setNo_media_broucher(int no_media_broucher) {
            this.no_media_broucher = no_media_broucher;
        }

        public String getMin_area_range() {
            return min_area_range;
        }

        public void setMin_area_range(String min_area_range) {
            this.min_area_range = min_area_range;
        }

        public String getPossession_dt() {
            return possession_dt;
        }

        public void setPossession_dt(String possession_dt) {
            this.possession_dt = possession_dt;
        }

        public String getLife_style() {
            return life_style;
        }

        public void setLife_style(String life_style) {
            this.life_style = life_style;
        }

        public String getEstablish_year() {
            return establish_year;
        }

        public void setEstablish_year(String establish_year) {
            this.establish_year = establish_year;
        }

        public String getNeeds() {
            return needs;
        }

        public void setNeeds(String needs) {
            this.needs = needs;
        }

        public String getExactlocation() {
            return exactlocation;
        }

        public void setExactlocation(String exactlocation) {
            this.exactlocation = exactlocation;
        }

        public String getReturns() {
            return returns;
        }

        public void setReturns(String returns) {
            this.returns = returns;
        }

        public Cords getCords() {
            return cords;
        }

        public void setCords(Cords cords) {
            this.cords = cords;
        }

        public ArrayList<Fitting> getFitting() {
            return fitting;
        }

        public void setFitting(ArrayList<Fitting> fitting) {
            this.fitting = fitting;
        }

        public ArrayList<Walls> getWalls() {
            return walls;
        }

        public void setWalls(ArrayList<Walls> walls) {
            this.walls = walls;
        }

        public ArrayList<Safety> getSafety() {
            return safety;
        }

        public void setSafety(ArrayList<Safety> safety) {
            this.safety = safety;
        }

        public int getNo_media_image() {
            return no_media_image;
        }

        public void setNo_media_image(int no_media_image) {
            this.no_media_image = no_media_image;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public ArrayList<UnitType> getUnit_type() {
            return unit_type;
        }

        public void setUnit_type(ArrayList<UnitType> unit_type) {
            this.unit_type = unit_type;
        }

        public ArrayList<CommentsDetail> getComments_detail() {
            return comments_detail;
        }

        public void setComments_detail(ArrayList<CommentsDetail> comments_detail) {
            this.comments_detail = comments_detail;
        }



        public class Recreation implements Serializable{
            private String image;
            private String title;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public class AmenitiesData  implements Serializable{
            private String image;
            private String title;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
        public class Services  implements Serializable{
            private String image;
            private String title;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public  class PriceTrends implements Serializable{
            private String price_trend_value;
            private String price_trend_date;

            public String getPrice_trend_value() {
                return price_trend_value;
            }

            public void setPrice_trend_value(String price_trend_value) {
                this.price_trend_value = price_trend_value;
            }

            public String getPrice_trend_date() {
                return price_trend_date;
            }

            public void setPrice_trend_date(String price_trend_date) {
                this.price_trend_date = price_trend_date;
            }
        }
        public class Flooring implements Serializable{
            private String type;
            private String name;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public class Cords implements Serializable{
            private String nw;
            private String se;

            public String getNw() {
                return nw;
            }

            public void setNw(String nw) {
                this.nw = nw;
            }

            public String getSe() {
                return se;
            }

            public void setSe(String se) {
                this.se = se;
            }
        }
        public class Fitting implements Serializable{
            private String type;
            private String name;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
        public class Walls implements Serializable{
            private String type;
            private String name;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
        public class Safety  implements Serializable{
            private String image;
            private String title;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public class UnitType  implements Serializable{
            private String size_values;
            private String price_range;
            private String area_range;
            private String unit_ids;
            private ArrayList<FlatImages>flat_images;
            private String wpcf_flat_typology;
            private String wpcf_flat_unit_prop_type;
            private String per_square_price_range;
            private int total_types;
            private int total_units;

            public String getSize_values() {
                return size_values;
            }

            public void setSize_values(String size_values) {
                this.size_values = size_values;
            }

            public String getPrice_range() {
                return price_range;
            }

            public void setPrice_range(String price_range) {
                this.price_range = price_range;
            }

            public String getArea_range() {
                return area_range;
            }

            public void setArea_range(String area_range) {
                this.area_range = area_range;
            }

            public String getUnit_ids() {
                return unit_ids;
            }

            public void setUnit_ids(String unit_ids) {
                this.unit_ids = unit_ids;
            }

            public ArrayList<FlatImages> getFlat_images() {
                return flat_images;
            }

            public void setFlat_images(ArrayList<FlatImages> flat_images) {
                this.flat_images = flat_images;
            }

            public String getWpcf_flat_typology() {
                return wpcf_flat_typology;
            }

            public void setWpcf_flat_typology(String wpcf_flat_typology) {
                this.wpcf_flat_typology = wpcf_flat_typology;
            }

            public String getWpcf_flat_unit_prop_type() {
                return wpcf_flat_unit_prop_type;
            }

            public void setWpcf_flat_unit_prop_type(String wpcf_flat_unit_prop_type) {
                this.wpcf_flat_unit_prop_type = wpcf_flat_unit_prop_type;
            }

            public String getPer_square_price_range() {
                return per_square_price_range;
            }

            public void setPer_square_price_range(String per_square_price_range) {
                this.per_square_price_range = per_square_price_range;
            }

            public int getTotal_types() {
                return total_types;
            }

            public void setTotal_types(int total_types) {
                this.total_types = total_types;
            }

            public int getTotal_units() {
                return total_units;
            }

            public void setTotal_units(int total_units) {
                this.total_units = total_units;
            }

            public class FlatImages implements Serializable{
                private int type;
                private int subtype;
                private String image_name;
                private String url;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getSubtype() {
                    return subtype;
                }

                public void setSubtype(int subtype) {
                    this.subtype = subtype;
                }

                public String getImage_name() {
                    return image_name;
                }

                public void setImage_name(String image_name) {
                    this.image_name = image_name;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }// End of UnitType class

        public class CommentsDetail implements Serializable{
            private String comment_date;
            private String comment_user_name;
            private String rating;
            private String comment_description;

            public String getComment_date() {
                return comment_date;
            }

            public void setComment_date(String comment_date) {
                this.comment_date = comment_date;
            }

            public String getComment_user_name() {
                return comment_user_name;
            }

            public void setComment_user_name(String comment_user_name) {
                this.comment_user_name = comment_user_name;
            }

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }

            public String getComment_description() {
                return comment_description;
            }

            public void setComment_description(String comment_description) {
                this.comment_description = comment_description;
            }
        }
    }

}// End of class
