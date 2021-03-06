package com.rs.mobile.wportal.biz.xsgr;

import java.util.List;

public class BotBannerBeans {
    /**
     * data : {"BotBanner":[{"ad_id":"20181027163154579","ad_title":"소상공인 하단 메인 배너 1","ad_image":"http://fileserver.gigawon.cn:8588/Banner/banner_test_07.png","link_url":"","custom_code":"01071390031abcde","tot_click_count":"0"},{"ad_id":"20181027163232743","ad_title":"소상공인 하단 메인 배너 2","ad_image":"http://fileserver.gigawon.cn:8588/Banner/banner_test_08.png","link_url":"","custom_code":"01071390076abcde","tot_click_count":"0"},{"ad_id":"20181027163442492","ad_title":"소상공인 하단 메인 배너 3","ad_image":"http://fileserver.gigawon.cn:8588/Banner/banner_test_09.png","link_url":"","custom_code":"01071390115abcde","tot_click_count":"0"}]}
     * status : 1
     * flag : 1501
     * msg : 操作完成
     */

    private DataBean data;
    private String status;
    private String flag;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<BotBannerBean> BotBanner;

        public List<BotBannerBean> getBotBanner() {
            return BotBanner;
        }

        public void setBotBanner(List<BotBannerBean> BotBanner) {
            this.BotBanner = BotBanner;
        }

        public static class BotBannerBean {
            /**
             * ad_id : 20181027163154579
             * ad_title : 소상공인 하단 메인 배너 1
             * ad_image : http://fileserver.gigawon.cn:8588/Banner/banner_test_07.png
             * link_url :
             * custom_code : 01071390031abcde
             * tot_click_count : 0
             */

            private String ad_id;
            private String ad_title;
            private String ad_image;
            private String link_url;
            private String custom_code;
            private String tot_click_count;

            public String getAd_id() {
                return ad_id;
            }

            public void setAd_id(String ad_id) {
                this.ad_id = ad_id;
            }

            public String getAd_title() {
                return ad_title;
            }

            public void setAd_title(String ad_title) {
                this.ad_title = ad_title;
            }

            public String getAd_image() {
                return ad_image;
            }

            public void setAd_image(String ad_image) {
                this.ad_image = ad_image;
            }

            public String getLink_url() {
                return link_url;
            }

            public void setLink_url(String link_url) {
                this.link_url = link_url;
            }

            public String getCustom_code() {
                return custom_code;
            }

            public void setCustom_code(String custom_code) {
                this.custom_code = custom_code;
            }

            public String getTot_click_count() {
                return tot_click_count;
            }

            public void setTot_click_count(String tot_click_count) {
                this.tot_click_count = tot_click_count;
            }
        }
    }
}
