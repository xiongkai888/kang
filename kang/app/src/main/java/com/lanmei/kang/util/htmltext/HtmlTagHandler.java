/*
 * Copyright (C) 2013-2014 Dominik Schürmann <dominik@dominikschuermann.de>
 * Copyright (C) 2013 Mohammed Lakkadshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lanmei.kang.util.htmltext;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.View;

import org.xml.sax.XMLReader;

import java.util.Vector;

/**
 * Some parts of this code are based on android.text.Html
 */
public class HtmlTagHandler implements Html.TagHandler {
    private int mListItemCount = 0;
    private OnHtmlImgListener onHtmlImgListener;
    private final Vector<String> mListParents = new Vector<String>();

    private int sIndex = 0;
    private int eIndex = 0;
    private int position = 0;

    private static class Code {
    }

    private static class Center {
    }

    public HtmlTagHandler(OnHtmlImgListener onHtmlImgListener) {
        this.onHtmlImgListener = onHtmlImgListener;
    }

    /*@Override
    public void handleTag(final boolean opening, final String tag, Editable output,
            final XMLReader xmlReader) {
        if (opening) {
            // opening tag
            if (HtmlTextView.DEBUG) {
                Log.d(HtmlTextView.TAG, "opening, output: " + output.toString());
            }

            if (tag.equalsIgnoreCase("ul") || tag.equalsIgnoreCase("ol")
                    || tag.equalsIgnoreCase("dd")) {
                mListParents.add(tag);
                mListItemCount = 0;
            } else if (tag.equalsIgnoreCase("code")) {
                start(output, new Code());
            } else if (tag.equalsIgnoreCase("center")) {
                start(output, new Center());
            }
        } else {
            // closing tag
            if (HtmlTextView.DEBUG) {
                Log.d(HtmlTextView.TAG, "closing, output: " + output.toString());
            }

            if (tag.equalsIgnoreCase("ul") || tag.equalsIgnoreCase("ol")
                    || tag.equalsIgnoreCase("dd")) {
                mListParents.remove(tag);
                mListItemCount = 0;
            } else if (tag.equalsIgnoreCase("li")) {
                handleListTag(output);
            } else if (tag.equalsIgnoreCase("code")) {
                end(output, Code.class, new TypefaceSpan("monospace"), false);
            } else if (tag.equalsIgnoreCase("center")) {
                end(output, Center.class,
                        new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), true);
            }
        }
    }*/

    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        // TODO Auto-generated method stub
        if (tag.toLowerCase().equals("img")) {
            if (opening) {
                sIndex = output.length();
            } else {
                eIndex = output.length();

                output.setSpan(new MSpan(position), eIndex-1, eIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                position++;

            }
        }
    }

    private class MSpan extends ClickableSpan implements View.OnClickListener {
        private int position;

        public MSpan(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View widget) {
            // TODO Auto-generated method stub

            if (onHtmlImgListener!=null)
                onHtmlImgListener.OnHtmlImgListener(position);


        }
    }

    /**
     * Mark the opening tag by using private classes
     * 
     * @param output
     * @param mark
     */
    private void start(Editable output, Object mark) {
        int len = output.length();
        output.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);

        if (HtmlTextView.DEBUG) {
            Log.d(HtmlTextView.TAG, "len: " + len);
        }
    }

    private void end(Editable output, Class kind, Object repl, boolean paragraphStyle) {
        Object obj = getLast(output, kind);
        // start of the tag
        int where = output.getSpanStart(obj);
        // end of the tag
        int len = output.length();

        output.removeSpan(obj);

        if (where != len) {
            // paragraph styles like AlignmentSpan need to end with a new line!
            if (paragraphStyle) {
                output.append("\n");
                len++;
            }
            output.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (HtmlTextView.DEBUG) {
            Log.d(HtmlTextView.TAG, "where: " + where);
            Log.d(HtmlTextView.TAG, "len: " + len);
        }
    }

    /**
     * Get last marked position of a specific tag kind (private class)
     * 
     * @param text
     * @param kind
     * @return
     */
    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        } else {
            for (int i = objs.length; i > 0; i--) {
                if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i - 1];
                }
            }
            return null;
        }
    }

    private void handleListTag(Editable output) {
        if (mListParents.lastElement().equals("ul")) {
            output.append("\n");
            String[] split = output.toString().split("\n");

            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.setSpan(new BulletSpan(15 * mListParents.size()), start, output.length(), 0);
        } else if (mListParents.lastElement().equals("ol")) {
            mListItemCount++;

            output.append("\n");
            String[] split = output.toString().split("\n");

            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.insert(start, mListItemCount + ". ");
            output.setSpan(new LeadingMarginSpan.Standard(15 * mListParents.size()), start,
                    output.length(), 0);
        }
    }
}
