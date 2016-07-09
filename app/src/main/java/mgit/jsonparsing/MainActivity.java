package mgit.jsonparsing;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Explanation of JSON parsing
// { } JSON objs are represented like this
// [ ] JSON array
/* Every json obj consists of property value pairs seperated by colon and each pair is separated by ','
object starts with { and ends with }
ex: { //obj starts
        "this is prop":"this is value" ,//property value pairs
        "array":[
                    {"prop":"value","prop2":"value2"}
                 ]
      }
 */
public class MainActivity extends AppCompatActivity {
    Button getData;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData=(Button)findViewById(R.id.getData);
        tv=(TextView) findViewById(R.id.tv);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("http://demo.codeofaninja.com/tutorials/json-example-with-php/index.php");
            }
        });
    }
    //it cannot run on main thread so it should run on diff thread in background this can be achieved through async task
    public class JSONTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getApplicationContext());
            builder.setCancelable(false);
            builder.setTitle("Loading");
            builder.setMessage("Plz Wait...");
            builder.show();
        }
        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            HttpURLConnection connection = null;
            try {
                URL url=new URL(params[0]);
                StringBuffer buffer=new StringBuffer();
                connection=(HttpURLConnection) url.openConnection();
                connection.connect(); //it will be connected to server
                //in response we will receive data it should be stored in Input stream line by line
                InputStream stream=connection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));
                String line="";
                while(reader.readLine()!=null){
                    buffer.append(line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tv.setText(result);
        }
    }
}
