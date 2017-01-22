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
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
        abstract class ClickableImageSpan extends ImageSpan { // ImageSpan - 文本插入图片

            public int getSize(Paint                paint,
                               CharSequence         text,
                               int                  start,
                               int                  end,
                               Paint.FontMetricsInt fontMetricsInt) {
                Drawable drawable = getDrawable();
                Rect rect = drawable.getBounds();

                if (fontMetricsInt != null) {
                    Paint.FontMetricsInt fmPaint    = paint.getFontMetricsInt();
                    int                  fontHeight = fmPaint.bottom - fmPaint.top;
                    int                  drawHeight = rect.bottom - rect.top;

                    int top    = drawHeight / 2 - fontHeight / 4;
                    int bottom = drawHeight / 2 + fontHeight / 4;

                    fontMetricsInt.ascent  = -bottom;
                    fontMetricsInt.top     = -bottom;
                    fontMetricsInt.bottom  = top;
                    fontMetricsInt.descent = top;
                }

                return rect.right;
            }

            @Override
            public void draw(Canvas       canvas,
                             CharSequence text,
                             int          start,
                             int          end,
                             float        x,
                             int          top,
                             int          y,
                             int          bottom,
                             Paint        paint) {
                Drawable drawable = getDrawable();
                canvas.save();

                canvas.translate(x,
                                 (((bottom - top) -
                                 drawable.getBounds().bottom -
                                 getResources().getDimension(R.dimen.margin_9_dp)) / 2 + top));
                drawable.draw(canvas);

                canvas.restore();
            }

            public ClickableImageSpan(Drawable b) {
                super(b);
            }

            abstract void onClick(View view);
        }

        // TextView.setMovementMethod(LinkMovementMethod.getInstance())可点击超链接，
        // 但在 ListView 里点击列表项时没有任何反应了，
        // 所以修改 LinkMovementMethod::onTouchEvent 方法
        class ClickableMovementMethod extends LinkMovementMethod {
            public boolean onTouchEvent(TextView    widget,
                                        Spannable   buffer,
                                        MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off  = layout.getOffsetForHorizontal(line, x);

                    // 获取 TextView 里的超链接对象，并装入 ClickableSpan 数组
                    ClickableSpan[]      link       = buffer.getSpans(off, off, ClickableSpan.class);
                    ClickableImageSpan[] imageSpans = buffer.getSpans(off, off, ClickableImageSpan.class);

                    // 此处是对超链接点击事件进行处理
                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        }
                        else if (action == MotionEvent.ACTION_DOWN) {
                            Selection.setSelection(buffer,
                                                   buffer.getSpanStart(link[0]),
                                                   buffer.getSpanEnd(link[0]));
                        }

                        // 处理结束后返回 true
                        // 这里一定要注意：当返回值为 true 时则表示点击事件已经被处理，不会继续传递了！
                        return true;
                    }
                    else if (imageSpans.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            imageSpans[0].onClick(widget);
                        }
                        else if (action == MotionEvent.ACTION_DOWN) {
                            Selection.setSelection(buffer,
                                                   buffer.getSpanStart(imageSpans[0]),
                                                   buffer.getSpanEnd(imageSpans[0]));
                        }

                        return true;
                    }
                    else {
                        Selection.removeSelection(buffer);
                    }
                }

                // 当返回值为 false 时则表示点击事件未处理，继续传递！
                return false;
            }
        }

        // 计算出 Activity 在字符串的起末位置
        matcher = pattern.matcher("跳转到其它Activity界面\n无下划线且改变字体颜色");

        if (matcher.find()) {
            ((TextView)findViewById(R.id.tv_goto_other_activity)).setMovementMethod(ClickableMovementMethod.getInstance());

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
                Toast.makeText(MainActivity.this, "ListView position = " + position, Toast.LENGTH_LONG).show();
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
            public View getView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = new TextView(MainActivity.this);
                    ListView.LayoutParams lp = new ListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                         ViewGroup.LayoutParams.WRAP_CONTENT);
                    convertView.setLayoutParams(lp);
                }

                if (0 == position) {
                    SpannableString ss = new SpannableString("点击本项空白处无Toast");

                    // 抽象类，需要自己扩展实现
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            // Performs the click action associated with this span.
                            startActivity(new Intent(MainActivity.this, SecondaryActivity.class));
                        }
                    };

                    ss.setSpan(clickableSpan,
                               1, 7,
                               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    ((TextView)convertView).setText(ss);

//                    // 使文本的超连接起作用，都必须设置 LinkMovementMethod 对象
//                    ((TextView)convertView).setMovementMethod(LinkMovementMethod.getInstance());
                }
                else if (2 == position) {
                    SpannableString ss = new SpannableString("点击本项空白处有Toast");

                    // 抽象类，需要自己扩展实现
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            // Performs the click action associated with this span.
                            startActivity(new Intent(MainActivity.this, SecondaryActivity.class));
                        }
                    };

                    ss.setSpan(clickableSpan,
                               1, 7,
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
                               1, 9,
                               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    ((TextView)convertView).setText(ss);

//                    // 使文本的超连接起作用，都必须设置 LinkMovementMethod 对象
//                    ((TextView)convertView).setMovementMethod(ClickableMovementMethod.getInstance());
                }
                else {
                    ((TextView)convertView).setText("分隔上下两个有动作的项");
                }

                // 使文本的超连接起作用，都必须设置 LinkMovementMethod 对象
                ((TextView)convertView).setMovementMethod(ClickableMovementMethod.getInstance());

                return convertView;
            }
        });
        // End -

        // ---------------------------------------------------------------------------------------//
    }
}
