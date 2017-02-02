package com.wzhnsc.testvariousspan;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Begin - 跳到我的博客
        TextView tv = (TextView)findViewById(R.id.tv_goto_my_blog);
        SpannableStringBuilder ssb = new SpannableStringBuilder("点击进入我的博客");
        ssb.setSpan(// 点击文字，可以打开一个URL
                    new URLSpan( "http://blog.sina.com.cn/wzhnsc" ),
                    // 样式的开始与结束位置
                    0,  8,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb);
        // 使文本的超连接起作用，都必须设置 LinkMovementMethod 对象
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        // End - 跳到我的博客

        // ---------------------------------------------------------------------------------------//

        Pattern pattern = Pattern.compile("Activity", // 要操作的关键字
                                          Pattern.LITERAL | Pattern.CASE_INSENSITIVE);

        // Begin - 改变指定关键的颜色
        // 计算出 Activity 在字符串的起末位置
        Matcher matcher = pattern.matcher("改变指定关键Activity的颜色");

        if (matcher.find()) {
            // 在 TextView 的 setText(CharSequence text) 方法中，要求参数是一个 CharSequence 对象，
            // 因此，我们可以通过 Spannable 对象来直接完成文本的设置。
            // 在使用中通常使用如下语句或者通过 SpannableStringBuilder 对象来进行构建。
            SpannableString ss = new SpannableString("改变指定关键Activity的颜色");

            // 进行样式的设置
            ss.setSpan(// 具体样式的实现对象 - ForegroundColorSpan 改变字符颜色
                       new ForegroundColorSpan(getResources().getColor(R.color.colorSpecific)),
                       // 样式的开始与结束位置
                       matcher.start(), matcher.end(),
                       // Spanned.SPAN_INCLUSIVE_INCLUSIVE - 包含　 start 和 end 对应的字符
                       // Spanned.SPAN_INCLUSIVE_EXCLUSIVE - 包含　 start ，但不包含 end 对应的字符
                       // Spanned.SPAN_EXCLUSIVE_INCLUSIVE - 不包含 start ，但包含　 end 对应的字符
                       // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE - 不包含 start 和 end 对应的字符
                       Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ((TextView)findViewById(R.id.tv_change_keyword_color)).setText(ss);
        }
        // End - 改变指定关键的颜色

        // ---------------------------------------------------------------------------------------//

        // Begin - 跳转到其它Activity界面，默认有下划线\n关键字颜色为样式colorAccent指定的
        // 计算出 Activity 在字符串的起末位置
        matcher = pattern.matcher(getString(R.string.goto_other_activity));

        if (matcher.find()) {
            SpannableString ss = new SpannableString(getString(R.string.goto_other_activity));

            // 抽象类，需要自己扩展实现
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // Performs the click action associated with this span.
                    startActivity(new Intent(MainActivity.this, SecondaryActivity.class));
                }
            };

            ss.setSpan(clickableSpan,
                       matcher.start(), matcher.end(),
                       Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ((TextView)findViewById(R.id.tv_goto_other_activity_underline)).setText(ss);

            // 使文本的超连接起作用，都必须设置 LinkMovementMethod 对象
            ((TextView)findViewById(R.id.tv_goto_other_activity_underline)).setMovementMethod(LinkMovementMethod.getInstance());
        }
        // End - 跳转到其它Activity界面，默认有下划线\n关键字颜色为样式colorAccent指定的

        // ---------------------------------------------------------------------------------------//

        // Begin - 跳转到其它Activity界面
        // 计算出 Activity 在字符串的起末位置
        matcher = pattern.matcher("跳转到其它Activity界面\n无下划线且改变字体颜色");

        if (matcher.find()) {
            ((TextView)findViewById(R.id.tv_goto_other_activity)).setMovementMethod(LinkMovementMethod.getInstance());

            SpannableString ss = new SpannableString("跳转到其它Activity界面\n无下划线且改变字体颜色");

            // 抽象类，需要自己扩展实现
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // Performs the click action associated with this span.
                    startActivity(new Intent(MainActivity.this, SecondaryActivity.class));
                }
            };

            ss.setSpan(clickableSpan,
                    matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // 设置文字下划线
            UnderlineSpan underlineSpan = new UnderlineSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    // 这里是取消下划线
                    ds.setUnderlineText(false);
                    // 改变文字颜色
                    ds.setColor(Color.parseColor("#303F9F"));
                }
            };

            ss.setSpan(underlineSpan,
                       matcher.start(), matcher.end(),
                       Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ((TextView)findViewById(R.id.tv_goto_other_activity)).setText(ss);
        }
        // End - 跳转到其它Activity界面

        // ---------------------------------------------------------------------------------------//

        // Begin -
        ListView lv = (ListView)findViewById(R.id.lv_click_item);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "onItemClick - ListView position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    LinearLayout ll = new LinearLayout(MainActivity.this);
                    ListView.LayoutParams lp = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,
                                                                         ListView.LayoutParams.MATCH_PARENT);
                    ll.setLayoutParams(lp);
                    ll.setGravity(Gravity.CENTER);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setBackgroundResource(R.drawable.selector_listview_item_click_bg);
                    // 正是这一项导致所有条目都要抢占焦点，所以ListView的OnItemClickListener.onItemClick点击事件失效
                    //ll.setClickable(true);
                    // 加了如下侦听 onItemClick 就没有响应了
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "LinearLayout.OnClickListener - ListView position = " + position, Toast.LENGTH_SHORT).show();
                        }
                    });

                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                                  LinearLayout.LayoutParams.WRAP_CONTENT);
                    llp.setMargins(5, 5, 5, 5);
                    llp.gravity = Gravity.CENTER;

                    TextView tv = new TextView(MainActivity.this);
                    tv.setLayoutParams(llp);
                    tv.setText(String.valueOf(position));
                    // 下面三句加上了连没用ClickableSpan的列表项也无onItemClick响应了
                    //tv.setFocusable(true);
                    //tv.setClickable(true);
                    //tv.setLongClickable(true);

                    ll.addView(tv);

                    convertView = ll;
                }

                if (0 == position) {
                    SpannableString ss = new SpannableString("点击除了此处会弹Toast");

                    // 抽象类，需要自己扩展实现
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            // Performs the click action associated with this span.
                            startActivity(new Intent(MainActivity.this, SecondaryActivity.class));
                        }
                    };

                    ss.setSpan(clickableSpan,
                               2, 6,
                               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    ((TextView)((LinearLayout)convertView).getChildAt(0)).setText(ss);

                    // 使文本的超连接起作用，都必须设置 LinkMovementMethod 对象
                    ((TextView)((LinearLayout)convertView).getChildAt(0)).setMovementMethod(LinkMovementMethod.getInstance());
                }
                else if (2 == position) {
                    SpannableString ss = new SpannableString("点击除了此处会弹Toast");

                    // 抽象类，需要自己扩展实现
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            // Performs the click action associated with this span.
                            startActivity(new Intent(MainActivity.this, SecondaryActivity.class));
                        }
                    };

                    ss.setSpan(clickableSpan,
                               2, 6,
                               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // 设置文字下划线
                    UnderlineSpan underlineSpan = new UnderlineSpan() {
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // 这里是取消下划线
                            ds.setUnderlineText(false);
                            // 改变文字颜色
                            ds.setColor(Color.parseColor("#303F9F"));
                        }
                    };

                    ss.setSpan(underlineSpan,
                               2, 6,
                               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    ((TextView)((LinearLayout)convertView).getChildAt(0)).setText(ss);

                    // 使文本的超连接起作用，都必须设置 LinkMovementMethod 对象
                    ((TextView)((LinearLayout)convertView).getChildAt(0)).setMovementMethod(LinkMovementMethod.getInstance());
                }
                else {
                    ((TextView)((LinearLayout)convertView).getChildAt(0)).setText("分隔上下两个有动作的项");
                }

                return convertView;
            }
        });
        // End -

        // ---------------------------------------------------------------------------------------//
    }
}
