package andiag.coru.es.welegends.utils.champions;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

import andiag.coru.es.welegends.DTOs.championsDTOs.ChampionDto;
import andiag.coru.es.welegends.activities.ActivitySplashScreen;

/**
 * Created by Iago on 27/07/2015.
 */
public class TaskGetChamImg extends AsyncTask<Void, Integer, Void> {

    private final String fst_url = "http://ddragon.leagueoflegends.com/cdn/";
    private final String snd_url = "/img/champion/";

    //Atributos privados
    private Activity activity;
    private HashMap<Integer, ChampionDto> champions;
    private Set<Integer> keys;
    private String version;

    private float totalChamps;
    private int count = 0;

    public TaskGetChamImg(Activity activity, HashMap<Integer, ChampionDto> champions, String version) {
        this.activity = activity;
        this.champions = champions;
        this.version = version;
        keys = champions.keySet();
        totalChamps = keys.size();
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... a) {
        String half_url = fst_url + version + snd_url;

        ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
        File directory = cw.getDir("Images", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }

        ChampionDto championDto;
        String final_url;
        URL url;
        File imgpath;
        InputStream is;
        FileOutputStream fos;
        Bitmap bitmap;
        for (Integer i : keys) {
            count++;
            publishProgress(count);

            championDto = champions.get(i);
            final_url = half_url + championDto.getKey() + ".png";
            Log.d("URL", final_url);
            try {
                imgpath = new File(directory, championDto.getId() + ".png");
                if (imgpath.exists()) {
                    Log.d("Exists", "imagepah exist");
                    continue;
                }

                imgpath.getParentFile().mkdirs();

                url = new URL(final_url);
                URLConnection ucon = url.openConnection();

                is = ucon.getInputStream();
                fos = new FileOutputStream(imgpath);

                bitmap = BitmapFactory.decodeStream(is);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... i) {
        int actual = i[0];

        ((ActivitySplashScreen) activity).setProgressText("Loading Champ Images "
                + String.format("%.2f", ((float) actual / totalChamps) * 100) + "%");

    }

    @Override
    protected void onPostExecute(Void varTerminarBackground) {
        ((ActivitySplashScreen) activity).startActivity();
    }

}