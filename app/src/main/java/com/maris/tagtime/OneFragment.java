package com.maris.tagtime;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.maris.tagtime.sqlite.Comment;
import com.maris.tagtime.sqlite.CommentsDataSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OneFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static OneFragment fragment;

    private View createdView;
    private Activity mainActivity;
    private ListView listView;

    private CommentsDataSource datasource;

    /** Items entered by the user is stored in this ArrayList variable */
    private ArrayList<String> list = new ArrayList<String>();

    /** Declaring an ArrayAdapter to set items to ListView */
    private ArrayAdapter<String> adapter;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OneFragment newInstance(int sectionNumber) {
        fragment = new OneFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;

        //return Fragment.instantiate(MainActivity.this, fragments[sectionNumber]);
    }

    public static OneFragment getInstance() {
        return fragment;
    }

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        createdView = view;
        mainActivity = getActivity();
        initListView();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void initListView() {
        // Get ListView object from xml
        listView = (ListView) createdView.findViewById(R.id.listView_event);

        // Defined Array values to show in ListView
        /*String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };*/

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        /** Defining the ArrayAdapter to set items to ListView */
        /*adapter = new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);*/

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);*/


        datasource = new CommentsDataSource(MyApp.getContext());
        datasource.open();

        List<Comment> values = datasource.getAllComments();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(mainActivity,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        //setListAdapter(adapter);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                //String  itemValue    = (String) listView.getItemAtPosition(position);

                ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) listView.getAdapter();
                Comment comment = adapter.getItem(itemPosition);

                String  itemValue = comment.toString();

                // Show Alert
                Toast.makeText(MyApp.getContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();
                /*
                if (getListAdapter().getCount() > 0) {
                    comment = (Comment) getListAdapter().getItem(0);
                    datasource.deleteComment(comment);
                    adapter.remove(comment);
                  }
                  adapter.notifyDataSetChanged();
                 */
                
            }

        });
    }

    /*@Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }*/

    public void addItem() {
        Time time = new Time();
        time.setToNow();
        Long seconds = time.toMillis(false);

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        String dateString = df.format(seconds);

        String text = dateString + " >> " + Long.toString(seconds);

        //list.add(text);

        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) listView.getAdapter();
        Comment comment = null;
        comment = datasource.createComment(text);
        adapter.add(comment);


        adapter.notifyDataSetChanged();

        /*
        "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
        "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
        "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
        "yyMMddHHmmssZ"-------------------- 010704120856-0700
        "K:mm a, z" ----------------------- 0:08 PM, PDT
        "h:mm a" -------------------------- 12:08 PM
        "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
         */

        //Toast.makeText(getActivity(), "new item.", Toast.LENGTH_SHORT).show();
    }

    /*public ArrayList<String> getList() {
        return list;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
