package com.gcrj.sidesliplistview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.gcrj.sidesliplistviewlibrary.SideslipItemLayout;
import com.gcrj.sidesliplistviewlibrary.SideslipListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent shareIntent = new Intent(this, HeyActivity.class);
//		shareIntent.setAction("com.gcrj.test");
//		shareIntent.setAction(Intent.ACTION_SEND);
//		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//		shareIntent.setType("image/jpeg");
//		startActivity(Intent.createChooser(shareIntent, "共享图片"));
//        startActivity(shareIntent);

        SideslipListView lv = (SideslipListView) findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "ItemClick = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "ItemLongClick = " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.a));
        final ArrayList alist = new ArrayList(list);
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alist);
        lv.setOnSideItemClickListener(new SideslipItemLayout.onSideItemClickListener() {
            @Override
            public void onSideItemClick(AdapterView<?> parent, View view, int itemPosition, int sidePosition) {
                Toast.makeText(MainActivity.this, "itemPosition = " + itemPosition + " sidePosition = " + sidePosition, Toast.LENGTH_SHORT).show();

                if (sidePosition == 2) {
                    alist.remove(itemPosition);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        lv.setMenu(new int[]{R.drawable.ic_action_good, R.drawable.ic_action_share, R.mipmap.ic_launcher}, new int[]{Color.RED, Color.GREEN});
        lv.setAdapter(adapter);
    }

}
