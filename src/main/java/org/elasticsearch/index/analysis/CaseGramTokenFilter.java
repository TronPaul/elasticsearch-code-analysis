package org.elasticsearch.index.analysis;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;

import java.io.IOException;
import java.util.Arrays;

public class CaseGramTokenFilter extends TokenFilter {
    private static final char[] DELIMS = new char[] {'-', '_'};
    private char[] curTermBuffer;
    private int curTermLength;
    private int newTokStart;
    private int curPos;
    private int caseStart;
    private int curPosInc, curPosLen;
    private int tokStart;
    private int tokEnd;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final PositionIncrementAttribute posIncAtt;
    private final PositionLengthAttribute posLenAtt;
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

    public CaseGramTokenFilter(TokenStream input) {
        super(input);
        posIncAtt = addAttribute(PositionIncrementAttribute.class);
        posLenAtt = addAttribute(PositionLengthAttribute.class);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (curTermBuffer == null) {
            if (!input.incrementToken()) {
                return false;
            } else {
                curTermBuffer = termAtt.buffer().clone();
                curTermLength = termAtt.length();
                newTokStart = -1;
                caseStart = -1;
                curPos = -1;
                curPosInc = posIncAtt.getPositionIncrement();
                curPosLen = posLenAtt.getPositionLength();
                tokStart = offsetAtt.startOffset();
                tokEnd = offsetAtt.endOffset();
            }
        }
        while (curPos++ < curTermLength) {
            if (curPos == curTermLength && newTokStart != 0) {
                clearAttributes();
                termAtt.copyBuffer(curTermBuffer, newTokStart, curPos - newTokStart);
                posIncAtt.setPositionIncrement(curPosInc);
                curPosInc = 0;
                posLenAtt.setPositionLength(curPosLen);
                offsetAtt.setOffset(tokStart, tokEnd);
                return true;
            } else if (Character.isAlphabetic(curTermBuffer[curPos]) || Character.isDigit(curTermBuffer[curPos])) {
                if (newTokStart == -1) {
                    newTokStart = curPos;
                }
                if (caseStart == -1 && Character.isAlphabetic(curTermBuffer[curPos])) {
                    caseStart = curPos;
                }
                if (curPos > caseStart + 1 && Character.isAlphabetic(curTermBuffer[curPos]) &&
                        Character.isLowerCase(curTermBuffer[curPos]) != Character.isLowerCase(curTermBuffer[curPos-1])) {
                    clearAttributes();
                    termAtt.copyBuffer(curTermBuffer, newTokStart, curPos - newTokStart);
                    posIncAtt.setPositionIncrement(curPosInc);
                    curPosInc = 0;
                    posLenAtt.setPositionLength(curPosLen);
                    offsetAtt.setOffset(tokStart, tokEnd);
                    newTokStart = curPos;
                    caseStart = curPos;
                    return true;
                }
            } else if (newTokStart > -1 && Arrays.binarySearch(DELIMS, curTermBuffer[curPos]) > -1) {
                clearAttributes();
                termAtt.copyBuffer(curTermBuffer, newTokStart, curPos - newTokStart);
                posIncAtt.setPositionIncrement(curPosInc);
                curPosInc = 0;
                posLenAtt.setPositionLength(curPosLen);
                offsetAtt.setOffset(tokStart, tokEnd);
                newTokStart = -1;
                caseStart = -1;
                return true;
            }
        }

        clearAttributes();
        termAtt.copyBuffer(curTermBuffer, 0, curTermLength);
        posIncAtt.setPositionIncrement(curPosInc);
        curPosInc = 0;
        posLenAtt.setPositionLength(curPosLen);
        offsetAtt.setOffset(tokStart, tokEnd);
        curTermBuffer = null;
        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        curTermBuffer = null;
    }
}
