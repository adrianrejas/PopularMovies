package org.udacity.android.arejas.popularmovies.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Class created with the help of http://www.jsonschema2pojo.org/ online application. It represents
 * a list of people involved in the cast of a movie. It also contains the annotations for defining 
 * entities and columns for allocating both MovieCreditsRestApi.Cast and MovieCreditsRestApi.Crew objects into
 * databases through room persistence library.
 */
public class MovieCreditsRestApi extends MovieElementRestApi implements Parcelable
{

    private Integer id;
    private List<Cast> cast = null;
    private List<Crew> crew = null;
    public final static Creator<MovieCreditsRestApi> CREATOR = new Creator<MovieCreditsRestApi>() {

        public MovieCreditsRestApi createFromParcel(Parcel in) {
            return new MovieCreditsRestApi(in);
        }

        public MovieCreditsRestApi[] newArray(int size) {
            return (new MovieCreditsRestApi[size]);
        }

    };

    private MovieCreditsRestApi(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.cast, (MovieCreditsRestApi.Cast.class.getClassLoader()));
        in.readList(this.crew, (MovieCreditsRestApi.Crew.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieCreditsRestApi() {
    }

    /**
     *
     * @param id
     * @param cast
     * @param crew
     */
    public MovieCreditsRestApi(Integer id, List<Cast> cast, List<Crew> crew) {
        super();
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(cast);
        dest.writeList(crew);
    }

    public int describeContents() {
        return 0;
    }

    public static class Cast implements Parcelable
    {

        private Integer cast_id;
        private String character;
        private String credit_id;
        private Integer gender;
        private Integer id;
        private String name;
        private Integer order;
        private String profile_path;
        public final static Creator<Cast> CREATOR = new Creator<Cast>() {

            public Cast createFromParcel(Parcel in) {
                return new Cast(in);
            }

            public Cast[] newArray(int size) {
                return (new Cast[size]);
            }

        };

        Cast(Parcel in) {
            this.cast_id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.character = ((String) in.readValue((String.class.getClassLoader())));
            this.credit_id = ((String) in.readValue((String.class.getClassLoader())));
            this.gender = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.order = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.profile_path = ((String) in.readValue((String.class.getClassLoader())));
        }

        /**
         * No args constructor for use in serialization
         *
         */
        Cast() {
        }

        /**
         *
         * @param id
         * @param order
         * @param credit_id
         * @param name
         * @param gender
         * @param cast_id
         * @param profile_path
         * @param character
         */
        Cast(Integer cast_id, String character, String credit_id, Integer gender, Integer id, String name, Integer order, String profile_path) {
            super();
            this.cast_id = cast_id;
            this.character = character;
            this.credit_id = credit_id;
            this.gender = gender;
            this.id = id;
            this.name = name;
            this.order = order;
            this.profile_path = profile_path;
        }

        public Integer getCast_id() {
            return cast_id;
        }

        public void setCast_id(Integer cast_id) {
            this.cast_id = cast_id;
        }

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public String getCredit_id() {
            return credit_id;
        }

        public void setCredit_id(String credit_id) {
            this.credit_id = credit_id;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getProfile_path() {
            return profile_path;
        }

        public void setProfile_path(String profile_path) {
            this.profile_path = profile_path;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(cast_id);
            dest.writeValue(character);
            dest.writeValue(credit_id);
            dest.writeValue(gender);
            dest.writeValue(id);
            dest.writeValue(name);
            dest.writeValue(order);
            dest.writeValue(profile_path);
        }

        public int describeContents() {
            return 0;
        }

    }

    public static class Crew implements Parcelable
    {

        private String credit_id;
        private String department;
        private Integer gender;
        private Integer id;
        private String job;
        private String name;
        private String profile_path;
        public final static Creator<Crew> CREATOR = new Creator<Crew>() {

            public Crew createFromParcel(Parcel in) {
                return new Crew(in);
            }

            public Crew[] newArray(int size) {
                return (new Crew[size]);
            }

        };

        Crew(Parcel in) {
            this.credit_id = ((String) in.readValue((String.class.getClassLoader())));
            this.department = ((String) in.readValue((String.class.getClassLoader())));
            this.gender = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.job = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.profile_path = ((String) in.readValue((String.class.getClassLoader())));
        }

        /**
         * No args constructor for use in serialization
         *
         */
        Crew() {
        }

        /**
         *
         * @param id
         * @param credit_id
         * @param department
         * @param name
         * @param job
         * @param gender
         * @param profile_path
         */
        Crew(String credit_id, String department, Integer gender, Integer id, String job, String name, String profile_path) {
            super();
            this.credit_id = credit_id;
            this.department = department;
            this.gender = gender;
            this.id = id;
            this.job = job;
            this.name = name;
            this.profile_path = profile_path;
        }

        public String getCredit_id() {
            return credit_id;
        }

        public void setCredit_id(String credit_id) {
            this.credit_id = credit_id;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfile_path() {
            return profile_path;
        }

        public void setProfile_path(String profile_path) {
            this.profile_path = profile_path;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(credit_id);
            dest.writeValue(department);
            dest.writeValue(gender);
            dest.writeValue(id);
            dest.writeValue(job);
            dest.writeValue(name);
            dest.writeValue(profile_path);
        }

        public int describeContents() {
            return 0;
        }

    }

}
