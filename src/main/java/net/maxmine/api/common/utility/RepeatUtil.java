package net.maxmine.api.common.utility;

import org.apache.commons.lang.text.StrBuilder;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@SuppressWarnings("all")
public class RepeatUtil {

    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        } else if (repeat <= 0) {
            return "";
        } else {
            int inputLength = str.length();
            if (repeat != 1 && inputLength != 0) {
                if (inputLength == 1 && repeat <= 8192) {
                    return padding(repeat, str.charAt(0));
                } else {
                    int outputLength = inputLength * repeat;
                    switch(inputLength) {
                        case 1:
                            char ch = str.charAt(0);
                            char[] output1 = new char[outputLength];

                            for(int i = repeat - 1; i >= 0; --i) {
                                output1[i] = ch;
                            }

                            return new String(output1);
                        case 2:
                            char ch0 = str.charAt(0);
                            char ch1 = str.charAt(1);
                            char[] output2 = new char[outputLength];

                            for(int i = repeat * 2 - 2; i >= 0; --i) {
                                output2[i] = ch0;
                                output2[i + 1] = ch1;
                                --i;
                            }

                            return new String(output2);
                        default:
                            StrBuilder buf = new StrBuilder(outputLength);

                            for(int i = 0; i < repeat; ++i) {
                                buf.append(str);
                            }

                            return buf.toString();
                    }
                }
            } else {
                return str;
            }
        }
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        } else {
            char[] buf = new char[repeat];

            for (int i = 0; i < buf.length; ++i) {
                buf[i] = padChar;
            }

            return new String(buf);
        }
    }
}
