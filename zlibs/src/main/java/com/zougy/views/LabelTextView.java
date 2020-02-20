package com.zougy.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.zougy.ziolib.R;

/**
 * Description:用于在textView的前、后添加一个标签。例如必填项的*、表格输入的:<br>
 * Author:邹高原<br>
 * Date:02/20 0020<br>
 * Email:441008824@qq.com
 */
public class LabelTextView extends AppCompatTextView {

    /**
     * 标记字符串
     */
    private String labelString;

    /**
     * 标记的位置。true在前面，false 在最后。
     */
    private boolean labelPlace = true;

    /**
     * 标记文字的颜色
     */
    private int labelColor = Color.RED;

    public LabelTextView(Context context) {
        this(context, null);
    }

    public LabelTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
            labelString = typedArray.getString(R.styleable.LabelTextView_labelString);
            labelColor = typedArray.getColor(R.styleable.LabelTextView_labelColor, Color.RED);
            labelPlace = typedArray.getBoolean(R.styleable.LabelTextView_labelPlace, true);
            if (!TextUtils.isEmpty(labelString)) {
                setText(getText());
            }
            typedArray.recycle();
        }
    }

    /**
     * 设置标记文本
     */
    public LabelTextView setLabelString(String labelString) {
        this.labelString = labelString;
        setText(getText());
        return this;
    }

    /**
     * 设置标记的颜色<br>
     * 注：需在调用{@link LabelTextView#setLabelString}之前调用
     */
    public LabelTextView setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        return this;
    }

    /**
     * 设置标记的位置
     * 注：需在调用{@link LabelTextView#setLabelString}之前调用
     *
     * @param labelPlace true，在最前面，false 在最后面
     */
    public LabelTextView setLabelPlace(boolean labelPlace) {
        this.labelPlace = labelPlace;
        return this;
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(labelString)) {
            String label;
            if (labelPlace) {
                label = "<html><font color=" + labelColor + ">" + labelString + "</font>" + text + "</html>";
            } else {
                label = "<html>" + text + "<font color=" + labelColor + ">" + labelString + "</font></html>";
            }
            super.setText(Html.fromHtml(label), type);
        } else {
            super.setText(text, type);
        }
    }
}
