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

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.junit.Test;

import java.io.StringReader;

public class CaseGramTokenFilterTests extends BaseTokenStreamTestCase {

    @Test
    public void noSpecialCase() throws Exception {
        StringReader reader = new StringReader("no special case");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"no", "special", "case"});
    }

    @Test
    public void lowerCaseCamelCase() throws Exception {
        StringReader reader = new StringReader("lowerCamelCase word");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"lower", "Camel", "Case", "lowerCamelCase", "word"});
    }

    @Test
    public void upperCaseCamelCase() throws Exception {
        StringReader reader = new StringReader("UpperCamelCase word");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"Upper", "Camel", "Case", "UpperCamelCase", "word"});

    }

    @Test
    public void upperCaseSnakeCase() throws Exception {
        StringReader reader = new StringReader("UPPER_SNAKE_CASE word");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"UPPER", "SNAKE", "CASE", "UPPER_SNAKE_CASE", "word"});
    }

    @Test
    public void lowerCaseSnakeCase() throws Exception {
        StringReader reader = new StringReader("lower_snake_case word");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"lower", "snake", "case", "lower_snake_case", "word"});
    }

    @Test
    public void lowerCaseSnakeCasePrependedWithUnderscore() throws Exception {
        StringReader reader = new StringReader("_lower_snake_case word");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"lower", "snake", "case", "_lower_snake_case", "word"});
    }

    @Test
    public void lowerCaseSnakeCasePrependedWithDigit() throws Exception {
        StringReader reader = new StringReader("1lower_snake_case word");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"1lower", "snake", "case", "1lower_snake_case", "word"});
    }

    @Test
    public void lowerCaseCamelCasePrependedWithDigit() throws Exception {
        StringReader reader = new StringReader("1lowerCamelCase word");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"1lower", "Camel", "Case", "1lowerCamelCase", "word"});
    }

    @Test
    public void delimsOnly() throws Exception {
        StringReader reader = new StringReader("------------");
        final MockTokenizer in = new MockTokenizer(MockTokenizer.WHITESPACE, false);
        in.setReader(reader);
        TokenStream stream = new CaseGramTokenFilter(in);
        assertTokenStreamContents(stream, new String[] {"------------"});
    }

}