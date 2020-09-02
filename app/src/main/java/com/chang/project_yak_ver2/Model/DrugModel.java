package com.chang.project_yak_ver2.Model;

import java.io.Serializable;

public class DrugModel implements Serializable {
    private static final long serialVersionUID = 1L;
    String DrugName;

    public DrugModel(String drugName) {
        DrugName = drugName;
    }

    public String getDrugName() {
        return DrugName;
    }

    public void setDrugName(String drugName) {
        DrugName = drugName;
    }
}
