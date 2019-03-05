package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private int[] weathers1 = {R.drawable.sunny_small, R.drawable.windy_small,  R.drawable.rainy_small,R.drawable.partly_sunny_small};
    private int mark=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DownloadUpdate().execute();
    }
    //static int strlength;


    public void btnClick(View view) {
        mark=0;
        new DownloadUpdate().execute();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "https://api.seniverse.com/v3/weather/daily.json?key=oinfsi9rdydhl1b0&location=tianjin&language=en&unit=c&start=0&days=5";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {

            final Calendar c = Calendar.getInstance();
            int mWay = Integer.parseInt(String.valueOf(c.get(Calendar.DAY_OF_WEEK)));
            int name = temperature.indexOf("name");
            int h = temperature.indexOf("high");
            //String High,low;
            //char i,j;
            //int l=  temperature.indexOf("low");
            int b = temperature.indexOf("date");
            String []date = new String[5];
            for (int i = 0; i < 5; i++) {
                date[i] = getWeekOfDate(mWay);
                mWay++;
            }
            String []weather= new String[5];
            int []Weather = new int[5];
            for (int i = 0; i < 5; i++) {

                int k = temperature.indexOf("text_day",(mark+1));
                mark=k;
                weather[i] = temperature.substring(k + 11, k + 14);
                Weather[i] = getWeatherOfDate(weather[i]);
            }


            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature.substring(h + 7, h + 9));
            ((ImageView) findViewById(R.id.img_weather_condition)).setImageDrawable(getResources().getDrawable(Weather[0]));
            ((ImageView) findViewById(R.id.image1)).setImageDrawable(getResources().getDrawable(Weather[1]));
            ((ImageView) findViewById(R.id.image2)).setImageDrawable(getResources().getDrawable(Weather[2]));
            ((ImageView) findViewById(R.id.image3)).setImageDrawable(getResources().getDrawable(Weather[3]));
            ((ImageView) findViewById(R.id.image4)).setImageDrawable(getResources().getDrawable(Weather[4]));
            ((TextView) findViewById(R.id.tv_location)).setText(temperature.substring(name + 7, name + 14));
            //((TextView) findViewById(R.id.tv_location)).setText(weather[0]);
            ((TextView) findViewById(R.id.tv_date)).setText(temperature.substring(b + 7, b + 17));
            ((TextView) findViewById(R.id.date1)).setText(date[1]);
            ((TextView) findViewById(R.id.date2)).setText(date[2]);
            ((TextView) findViewById(R.id.date3)).setText(date[3]);
            ((TextView) findViewById(R.id.date4)).setText(date[4]);

        }
    }

    public String getWeekOfDate(int k) {
        String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        int w = k;
        if (w > 6){
            w=w-7;
        }
            return weekDays[w];
    }

    public int getWeatherOfDate(String k) {

        String s = k;
        int w = 0;
        if (s.equals("Sun")) {
            w = 0;
        } else if (s.equals("Win")) {
            w = 1;
        } else if (s.equals("Lig")) {
            w = 2;
        } else {
            w = 3;
        }

        return weathers1[w];
    }


}
