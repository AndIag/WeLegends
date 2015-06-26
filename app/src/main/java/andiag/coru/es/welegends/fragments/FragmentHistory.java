package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.adapters.AdapterHistory;
import andiag.coru.es.welegends.entities.Match;
import andiag.coru.es.welegends.entities.Summoner;
import andiag.coru.es.welegends.utils.requests.GsonRequest;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentHistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHistory extends Fragment {


    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private AdapterHistory recyclerAdapter;
    private GridLayoutManager layoutManager;

    private final int INCREMENT = 10;
    private int BEGININDEX;
    private int ENDINDEX;

    private String region;
    private long summoner_id;


    // TODO: Rename and change types and number of parameters
    public static FragmentHistory newInstance(String region,long id) {
        FragmentHistory fragment = new FragmentHistory();
        Bundle args = new Bundle();
        args.putString("region",region);
        args.putLong("id",id);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHistory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            region = getArguments().getString("region");
            summoner_id = getArguments().getLong("id");
        }
        recyclerAdapter = new AdapterHistory(getActivity());
        getSummonerHistory2(0, INCREMENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_history, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        recyclerView.setHasFixedSize(true);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 300);

        layoutManager = new GridLayoutManager(getActivity(), columns);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    // GET DATA CODE PART

    private void getSummonerHistory(int beginIndex, int endIndex){
        APIHandler handler = APIHandler.getInstance(getActivity());

        String request = "https://" + region + handler.getServer() + region
                + handler.getMatchHistory() + summoner_id + "?beginIndex=" + beginIndex + "&endIndex=" + endIndex + "&api_key=" +  handler.getKey();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPUESTA", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleyHelper.getInstance(getActivity()).getRequestQueue().add(jsonObjectRequest);
    }

    private void getSummonerHistory2(int beginIndex, int endIndex){
        final Gson gson = new Gson();
        APIHandler handler = APIHandler.getInstance(getActivity());

        String request = "https://" + region + handler.getServer() + region
                + handler.getMatchHistory() + summoner_id + "?beginIndex=" + beginIndex + "&endIndex=" + endIndex + "&api_key=" +  handler.getKey();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPUESTA", response.toString());
                        JSONArray arrayMatches = null;
                        ArrayList<Match> list = new ArrayList<>();
                        try {
                            arrayMatches = response.getJSONArray("matches");
                            for(int i=0;i<arrayMatches.length();i++){
                                Log.d("MATCH i",arrayMatches.get(i).toString());
                                Match match = (Match) gson.fromJson(arrayMatches.get(i).toString(), Match.class);
                                list.add(match);
                            }
                            Log.d("MATCHES ON LIST","N = "+list.size());
                            recyclerAdapter.updateHistory(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR",error.toString());
            }
        });

        VolleyHelper.getInstance(getActivity()).getRequestQueue().add(jsonObjectRequest);
    }

}
