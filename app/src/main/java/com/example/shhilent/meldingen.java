package com.example.shhilent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class meldingen extends AppCompatActivity {

    String[] ListTitle = new String[]{
            "70DB in trein: 18324","80DB in trein: 18324","73DB in trein: 18324","71DB in trein: 18324","73DB in trein: 18324","70DB in trein: 18324","70DB in trein: 18324"
    };

    String[] Listdescription = new String[]{
            "70DB in trein 18324. 14:00", "80DB in trein 18324. 14:00 this is also some extra text for long descriptions :)","70DB in trein 18324. 14:00","70DB in trein 18324. 14:00","70DB in trein 18324. 14:00","70DB in trein 18324. 14:00","70DB in trein 18324. 14:00"
    };

    int[] ListImages = new int[]{
            R.drawable.ic_14121_warning_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meldingen);

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i = 0; i < ListTitle.length; i++){
            HashMap<String,String> hm = new HashMap<String, String>();
            hm.put("ListTitle",ListTitle[i]);
            hm.put("ListDescription",Listdescription[i]);
            hm.put("ListImages",Integer.toString(ListImages[0]));
            aList.add(hm);
        }

        String[] from = {"ListImages","ListTitle","ListDescription"};

        int[] to = {R.id.list_images,R.id.Title,R.id.Description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(),aList,R.layout.meldingen_list,from,to);
        ListView simpleListView = (ListView)findViewById(R.id.list_view);
        simpleListView.setAdapter(simpleAdapter);
    }
}
