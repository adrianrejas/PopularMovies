package org.udacity.android.arejas.popularmovies.data.entities;

/* Sealed class representing base entity */
public abstract class EntityElement {

    String dataLanguage;

    EntityElement() {}

    public String getDataLanguage() {
        return dataLanguage;
    }

    public void setDataLanguage(String dataLanguage) {
        this.dataLanguage = dataLanguage;
    }

}
