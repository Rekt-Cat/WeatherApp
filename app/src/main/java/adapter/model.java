package adapter;

import android.widget.ImageView;

public class model {
    String time;
    String temp1;
    String windspeed;
    public model(String time, String temp1, String windspeed) {
        this.time = time;
        this.temp1 = temp1;
        this.windspeed = windspeed;
    }



    public String gettime() {
        return time;
    }

    public void settime(String currtime) {
        this.time = currtime;
    }

    public String gettemp1() {
        return temp1;
    }

    public void setCurretemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }
}
