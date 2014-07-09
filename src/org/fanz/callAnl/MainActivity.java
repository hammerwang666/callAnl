package org.fanz.callAnl;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import org.fanz.cardui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends FragmentActivity implements
        ActionBar.OnNavigationListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    private int[] imageIds = new int[]
            {R.drawable.tiger, 0
                    , R.drawable.qingzhao, R.drawable.libai, R.drawable.nongyu
                    , R.drawable.qingzhao, R.drawable.libai, R.drawable.nongyu
                    , R.drawable.qingzhao, R.drawable.libai, R.drawable.nongyu
                    , R.drawable.qingzhao, R.drawable.libai};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
//        actionBar.setTitle(getString(R.string.action_title_main));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setIcon(R.drawable.ic);
        actionBar.setTitle("");
        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, new String[]{
                        getString(R.string.title_section1),
                        getString(R.string.title_section2),
                        getString(R.string.title_section3),}), this);


        //����һ��List���ϣ�List���ϵ�Ԫ����Map
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < names.length; i++)
//        {
//            Map<String, Object> listItem = new HashMap<String, Object>();
//            listItem.put("num", "#"+ (i+1));
//            listItem.put("header", imageIds[i]);
//            listItem.put("personName", names[i]);
//            listItem.put("content", content[i]);
//            listItems.add(listItem);
//        }


        ContentResolver cr = getContentResolver();
        CallLogHandler cLHandle = new CallLogHandler();
        List<Map> callLogList = cLHandle.getCallLog(cr);

        int cLSize = callLogList.size();
        for (int i = 0; i < cLSize; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("num", "#" + (i + 1));
            listItem.put("header", 0);
            listItem.put("personName", callLogList.get(i).get("name") + "(" + callLogList.get(i).get("number") + ")");
            listItem.put("content", "ͨ���ܴ���: " + callLogList.get(i).get("faq")
                    + "\nͨ����ʱ��: " + callLogList.get(i).get("durStr")
                    + "\n���ͨ��: " + callLogList.get(i).get("time"));
            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this
                , listItems
                , R.layout.main
                , new String[]{"num", "personName", "header", "content"}
                , new int[]{R.id.num, R.id.name, R.id.header, R.id.content});
        ListView list = (ListView) findViewById(R.id.mylist);
        list.setDividerHeight(0);
        list.setAdapter(simpleAdapter);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
                .getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        Fragment fragment = new DummySectionFragment();
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();


//        Context context = getApplicationContext();
//        CharSequence text = "Hello toast!";
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();

//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
//        TextView text = (TextView) layout.findViewById(R.id.text);
//        text.setText("��ʾ��~");
//
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();

        return true;
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Create a new TextView and set its text to the fragment's section
            // number argument value.
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            textView.setText(Integer.toString(getArguments().getInt(
                    ARG_SECTION_NUMBER)));
            Integer pictureId = 0;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    pictureId = R.drawable.haitun;
                    break;
                case 2:
                    pictureId = R.drawable.jiyan;
                    break;
                case 3:
                    pictureId = R.drawable.tuzi;
                    break;
            }
            CallLogHandler handler = new CallLogHandler();
            ContentResolver cr = getContentResolver();
            handler.getCallLog(cr);
            textView.setBackgroundResource(pictureId);
            textView.setText("CallLog" + pictureId);
            return textView;
        }
    }
}
