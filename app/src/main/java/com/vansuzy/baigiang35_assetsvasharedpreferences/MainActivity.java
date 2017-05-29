package com.vansuzy.baigiang35_assetsvasharedpreferences;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView txtFont;

    ListView lvFont;
    ArrayList<String> dsFont;
    ArrayAdapter<String> fontAdapter;

    // Chú thích: 1, 2, 3: Assets; 4, 5, 6: SharedPreferences

    // 4
    String tenLuuTru = "TrangThaiFont"; // biến tenLuuTru này chúng ta đặt tên là "TrangThaiFont" tức là khi chúng ta tạo file thì hệ thống nó sẽ tự động tạo ra file xml tên là TrangThaiFont.xml, nếu mà chúng ta không đặt tên cho nó thì lập tức nó tự lấy tên của Activity này làm file xml (tức là MainActivity.xml) (tại vì bản chất của file SharedPreferences đó là lưu file với định dạng xml).

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
        // 2
        lvFont.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xuLyDoiFontChu(position);
            }
        });
    }

    // 3
    private void xuLyDoiFontChu(int position) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/" + dsFont.get(position));  // lệnh "font/" + dsFont.get(position) trả về đúng đường dẫn từ Assets đến thư mục font chứa các tập tin font chữ của chúng ta
        txtFont.setTypeface(typeface);

        // 5
        SharedPreferences preferences = getSharedPreferences(tenLuuTru, MODE_PRIVATE);  // Hàm getSharedPreferences() có 2 đối số: đối số 1 là tên tập tin chúng ta muốn đặt và đối số 2 là MODE_PRIVATE (tức là chỉ mình ứng dụng này được sử dụng). Khi mà muốn cho các ứng dụng khác cùng sử dụng thì thông thường người ta lưu vào trong sdcard.
        SharedPreferences.Editor editor = preferences.edit();   // Mục đích của editor: cho phép chúng ta lưu dữ liệu xuống file (cụ thể là file xml)
        editor.putString("FONT_CHU", "font/" + dsFont.get(position));   // lưu font chữ người sử dụng chọn
        editor.commit();
    }

    private void addControls() {
        txtFont = (TextView) findViewById(R.id.txtFont);
        lvFont = (ListView) findViewById(R.id.lvFont);
        dsFont = new ArrayList<>();
        fontAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                dsFont
        );
        lvFont.setAdapter(fontAdapter);

        // 1
        try {
            AssetManager assetManager = getAssets();    // lấy toàn bộ tài nguyên trong thư mục assets (ở đây là 2 thư mục font và music).
            String[] arrFontName = assetManager.list("font");   // truy xuất tài nguyên trong thư mục font (trả về danh sách font chữ)
            dsFont.addAll(Arrays.asList(arrFontName));  // chuyển từ Arrays sang ArrayList thông qua Arrays.asList()
            fontAdapter.notifyDataSetChanged();

            // 6
            SharedPreferences preferences = getSharedPreferences(tenLuuTru, MODE_PRIVATE);
            String font = preferences.getString("FONT_CHU", "");
            if (font.length() > 0) {
                Typeface typeface = Typeface.createFromAsset(getAssets(), font);
                txtFont.setTypeface(typeface);
            }
        } catch (IOException e) {
            Log.e("LOI_FONT", e.toString());
        }
    }
}
