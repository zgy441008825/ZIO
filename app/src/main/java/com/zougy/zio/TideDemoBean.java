package com.zougy.zio;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
public class TideDemoBean implements Serializable {

    /**
     * name : {"en":"Windy Mountain","zh-Hans":"远山","_tr":"","zh-Hant":"遠山","ja":"遠い山","es":"Montaña ventosa"}
     * icon_url : http://pics.tide.moreless.io/scenes/FgEZaJg-a9smqHGs2S1E2x3KarHK
     * demo_sound_url : http://pics.tide.moreless.io/demo_sounds/FsIxnmcV_lNAABAc7ND2Ao_PnHR2
     * cover_url : http://pics.tide.moreless.io/scenes/FgEZaJg-a9smqHGs2S1E2x3KarHK
     * thumbnail : http://pics.tide.moreless.io/scenes/FgEZaJg-a9smqHGs2S1E2x3KarHK
     * primary_color : 41,104,144,100
     * id : 5c6cf3e982d6520006226db6
     */

    private NameBean name;
    private String icon_url;
    private String demo_sound_url;
    private String cover_url;
    private String thumbnail;
    private String primary_color;
    private String id;

    public NameBean getName() {
        return name;
    }

    public void setName(NameBean name) {
        this.name = name;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getDemo_sound_url() {
        return demo_sound_url;
    }

    public void setDemo_sound_url(String demo_sound_url) {
        this.demo_sound_url = demo_sound_url;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPrimary_color() {
        return primary_color;
    }

    public void setPrimary_color(String primary_color) {
        this.primary_color = primary_color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class NameBean {
        /**
         * en : Windy Mountain
         * zh-Hans : 远山
         * _tr :
         * zh-Hant : 遠山
         * ja : 遠い山
         * es : Montaña ventosa
         */

        private String en;
        @SerializedName("zh-Hans")
        private String zhHans;
        private String _tr;
        @SerializedName("zh-Hant")
        private String zhHant;
        private String ja;
        private String es;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getZhHans() {
            return zhHans;
        }

        public void setZhHans(String zhHans) {
            this.zhHans = zhHans;
        }

        public String get_tr() {
            return _tr;
        }

        public void set_tr(String _tr) {
            this._tr = _tr;
        }

        public String getZhHant() {
            return zhHant;
        }

        public void setZhHant(String zhHant) {
            this.zhHant = zhHant;
        }

        public String getJa() {
            return ja;
        }

        public void setJa(String ja) {
            this.ja = ja;
        }

        public String getEs() {
            return es;
        }

        public void setEs(String es) {
            this.es = es;
        }
    }
}
