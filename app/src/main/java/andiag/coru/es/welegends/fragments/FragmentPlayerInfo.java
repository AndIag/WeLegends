package andiag.coru.es.welegends.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerInfo extends Fragment {

    private static FragmentPlayerInfo fragmentPlayerInfo;
    private static String region;
    private static long matchId;
    private boolean isLoading = false;

    public FragmentPlayerInfo() {
        // Required empty public constructor
    }

    public static FragmentPlayerInfo newInstance(String region, long matchId) {
        if (fragmentPlayerInfo != null) {
            return fragmentPlayerInfo;
        }
        fragmentPlayerInfo = new FragmentPlayerInfo();
        Bundle args = new Bundle();
        args.putString("region", region);
        args.putLong("matchId", matchId);
        fragmentPlayerInfo.setArguments(args);
        return fragmentPlayerInfo;
    }

    public static FragmentPlayerInfo newInstance() {
        if (fragmentPlayerInfo != null) {
            return fragmentPlayerInfo;
        }
        fragmentPlayerInfo = new FragmentPlayerInfo();
        return fragmentPlayerInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) { //Load saved data in onPause
            matchId = savedInstanceState.getLong("matchId");
            region = savedInstanceState.getString("region");
        } else if (getArguments() != null) {
            region = getArguments().getString("region");
            matchId = getArguments().getLong("matchId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_info, container, false);
    }

    private void getMatch() {
        if (isLoading) return;

        isLoading = true;

        //changeRefreshingValue(true);
        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(getActivity());
        }

        String request = handler.getServer() + region + handler.getMatch() + matchId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                isLoading = false;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String message = getString(R.string.errorDefault);
                    switch (networkResponse.statusCode) {
                        case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                            message = getString(R.string.error500);
                            break;
                        case HttpStatus.SC_SERVICE_UNAVAILABLE:
                            message = getString(R.string.error503);
                            break;
                    }
                    Toast.makeText(getActivity(), message
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        VolleyHelper.getInstance(getActivity()).getRequestQueue().add(jsonObjectRequest);
    }

}
