package com.chang.project_yak_ver2.Api;

import java.io.Serializable;
import java.sql.Array;
import java.util.List;

public class inputData implements Serializable {
    public static final String Base_URL = "http://ec2-13-124-201-28.ap-northeast-2.compute.amazonaws.com:8080/";
    public static final String Get_Post = Base_URL + "infoRequest?SSN=";
    public static final String Get_Delete = Base_URL + "infoDelete?SSN=";
    public static final String Delete_URL="&medName=";

    /*
    {
        "id":"5f48ce0b79660440207a2fa3",
            "name":"lee",
            "pregnant":true,
            "take_med":[{
        "prod_name":"가니레버프리필드시린지주0.25mg/0.5mL (가니렐릭스아세트산염)",
        "take_session":[true, false, true]}],
        "cautions":["임신 중이므로 가니레버프리필드시린지주0.25mg/0.5mL (가니렐릭스아세트산염)복용에 주의가 필요합니다."],
        "ssn":"123123"
    }*/
    public static class Post implements Serializable {
        String id;
        String name;
        boolean pregnant;
        String ssn;
        List<String> cautions;
        List<Take_med> take_med;


        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isPregnant() {
            return pregnant;
        }

        public String getSsn() {
            return ssn;
        }

        public List<String> getCautions() {
            return cautions;
        }

        public List<Take_med> getTake_med() {
            return take_med;
        }


        public static class Take_med implements Serializable {
            String prodName;
            boolean[] take_session;

            public String getProd_name() {
                return prodName;
            }

            public boolean[] getTake_session() {
                return take_session;
            }
        }
    }


}
